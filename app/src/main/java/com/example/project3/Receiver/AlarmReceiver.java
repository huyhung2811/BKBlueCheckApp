package com.example.project3.Receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.project3.Body.AttendanceTimeBody;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.StoreAttendanceTimeResponse;
import com.example.project3.scanCallback.ScanCallbackHolder;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmReceiver extends BroadcastReceiver {
    private BluetoothAdapter bluetoothAdapter;
    private static final String CHANNEL_ID = "BLE_CHANNEL";
    private LocalDateTime firstScanTime;
    private ApiInterface apiInterface;
    private String token;
    private int classCode;
    boolean isAttendanced;
    boolean isHasDevice;

    @Override
    public void onReceive(Context context, Intent intent) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.e("AlarmReceiver", "BluetoothAdapter is null");
            return;
        }

        if (intent != null && "START_SERVICE_ACTION".equals(intent.getAction())) {
            classCode = intent.getIntExtra("classCode", classCode);
            ScanCallback mScanCallback = ScanCallbackHolder.getScanCallback(classCode);
            startScan(mScanCallback);
        } else if (intent != null && "STOP_SERVICE_ACTION".equals(intent.getAction())) {
            classCode = intent.getIntExtra("classCode", classCode);
            token = intent.getStringExtra("token");
            isAttendanced = intent.getBooleanExtra("isAttendance", isAttendanced);
            isHasDevice = intent.getBooleanExtra("isHasDevice",isHasDevice);
            apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
            ScanCallback mScanCallback = ScanCallbackHolder.getScanCallback(classCode);
            stopScan(mScanCallback);
            boolean success = false;
            if(isAttendanced == false) {
                LocalDateTime endAttendanceTime = LocalDateTime.now();
                DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String day = endAttendanceTime.format(dayFormatter);
                String time = null;
                String bluetoothAddress = null;
                storeEndAttendance(classCode, day, time, bluetoothAddress);
            } else {
                if(isHasDevice){
                    LocalDateTime endAttendanceTime = LocalDateTime.now();
                    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String day = endAttendanceTime.format(dayFormatter);
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String time = endAttendanceTime.format(timeFormatter);
                    String bluetoothAddress = bluetoothAdapter.getAddress();
                    storeEndAttendance(classCode, day, time, bluetoothAddress);
                    success = true;
                } else {
                    LocalDateTime endAttendanceTime = LocalDateTime.now();
                    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String day = endAttendanceTime.format(dayFormatter);
                    String time = null;
                    String bluetoothAddress = null;
                    storeEndAttendance(classCode, day, time, bluetoothAddress);
                }
            }
            showAttendanceNotification(context, classCode, success);
        }
    }

    public void startScan(ScanCallback scanCallback) {
        ScanFilter scanFilter = new ScanFilter.Builder().build();

        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build();

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.getBluetoothLeScanner().startScan(Arrays.asList(scanFilter), scanSettings, scanCallback);
            Log.d("StartScan", "Bluetooth LE scan started");
        } else {
            Log.e("StartScan", "BluetoothAdapter is null or not enabled");
        }
    }

    public void stopScan(ScanCallback scanCallback) {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
            firstScanTime = null;
            Log.d("StopScan", "Bluetooth LE scan stopped");
        } else {
            Log.e("StopScan", "BluetoothAdapter is null or not enabled");
        }
    }

    private void storeEndAttendance(int class_code, String day, String time, String bluetooth_address) {
        String type = "end_time";
        Call<StoreAttendanceTimeResponse> call = apiInterface.storeAttendance("Bearer" + token, new AttendanceTimeBody(class_code, day, time, bluetooth_address, type));
        call.enqueue(new Callback<StoreAttendanceTimeResponse>() {
            @Override
            public void onResponse(Call<StoreAttendanceTimeResponse> call, Response<StoreAttendanceTimeResponse> response) {
                if (response.isSuccessful()) {
                    StoreAttendanceTimeResponse storeAttendanceTimeResponse = response.body();
                    if (storeAttendanceTimeResponse != null) {
                        Log.d("Message", storeAttendanceTimeResponse.getMessage());
                    } else {
                        Log.e("storeAttendance", "Phản hồi không hợp lệ từ máy chủ");
                    }
                } else {
                    try {
                        String errorBodyString = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorBodyString);
                        String errorMessage = errorObject.getString("message");
                    } catch (Exception e) {
                        Log.e("JSON Parsing Error", "Failed to parse JSON response", e);
                        Log.e("JSON Parsing Error", "Error Message: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreAttendanceTimeResponse> call, Throwable t) {
                Log.e("storeAttendance", "Lỗi: " + t.getMessage());
            }
        });
    }

    private void showAttendanceNotification(Context context, int classCode, boolean success) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String contentText = success ? "Điểm danh lớp " + classCode + " thành công.\nTrạng thái: Đi học"
                : "Điểm danh lớp " + classCode + " thất bại.\nTrạng thái: Không đi học";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo2)
                .setContentTitle("Điểm danh cuối giờ")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Attendance Status", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = ("cuoigio" + classCode).hashCode();
        notificationManager.notify(notificationId, builder.build());
    }

}
