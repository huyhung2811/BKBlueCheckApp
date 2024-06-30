package com.example.project3.Activity.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.Activity.Schedule.ScheduleActivity;
import com.example.project3.R;
import com.example.project3.Service.ScanService;

public class AttendanceActivity extends AppCompatActivity {
    private Toolbar attendanceToolbar;
    Button btnScan;
    TextView txtAttendance;
    ConstraintLayout notice;
    int state=0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2;
    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 3;
    private static final String PREFS_NAME = "AttendancePrefs";
    private static final String STATE_KEY = "state";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        attendanceToolbar = findViewById(R.id.attendance_toolbar);
        txtAttendance = findViewById(R.id.txtAttendance);
        btnScan = findViewById(R.id.btnScan);
        notice = findViewById(R.id.notice);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        state = prefs.getInt(STATE_KEY, 0);

        if (state == 1) {
            btnScan.setText("Stop Scan");
        } else {
            btnScan.setText("Start Scan");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_SCAN}, REQUEST_BLUETOOTH_SCAN_PERMISSION);
            }
        }

        attendanceToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, StudentMainActivity.class);
                startActivity(intent);
                AttendanceActivity.this.finish();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state==0){
                    checkLocationPermissionsAndStartService();
                } else {
                    clickStopService();
                }
            }
        });
    }

    private void checkLocationPermissionsAndStartService() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                clickStartService();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermissionsAndStartService();
            } else {
                showPermissionDialog();
            }
        } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                clickStartService();
            } else {
                showPermissionDialog();
            }
        }

    }
    private void clickStartService() {
        state = 1;
        saveStateToPreferences();
        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
        Intent serviceIntent = new Intent(this, ScanService.class);
        startService(serviceIntent);
        btnScan.setText("Stop Scan");
    }

    private void clickStopService() {
        state = 0;
        saveStateToPreferences();
        Intent intent = new Intent(this, ScanService.class);
        stopService(intent);
        btnScan.setText("Start Scan");
    }

    private void saveStateToPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(STATE_KEY, state);
        editor.apply();
    }

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quyền Truy Cập Thông Tin Vị Trí");
        builder.setMessage("Ứng dụng cần quyền truy cập thông tin vị trí background để thực hiện điểm danh. Vui lòng cấp quyền vị trí thành luôn được phép trong cài đặt.");
        builder.setPositiveButton("Cài Đặt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}