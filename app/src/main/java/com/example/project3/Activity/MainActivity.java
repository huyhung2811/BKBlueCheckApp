package com.example.project3.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.project3.Activity.Student.StudentMainActivity;
import com.example.project3.Activity.Teacher.TeacherMainActivity;
import com.example.project3.R;

import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_SCAN_PERMISSION = 2;
    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Toast.makeText(MainActivity.this, "Bluetooth OK", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Bluetooth not OK", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale locale = new Locale("vi", "VN");
        Locale.setDefault(locale);
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        TimeZone.setDefault(timeZone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.BLUETOOTH_SCAN},
                        REQUEST_SCAN_PERMISSION);
            }
        }

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        String role = prefs.getString("role", null);
        if (token != null) {
            if (role.equals("student")) {
                startActivity(new Intent(this, StudentMainActivity.class));
            }
            if (role.equals("teacher")) {
                startActivity(new Intent(this, TeacherMainActivity.class));
            }
            finish();
        } else {
            startActivity(new Intent(this, FirstActivity.class));
            finish();
        }
        setupBluetooth();
    }

    private void setupBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivityResultLauncher.launch(enableBTIntent);
        }
    }
}