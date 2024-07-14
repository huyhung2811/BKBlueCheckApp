    package com.example.project3.Service;
    
    import android.annotation.SuppressLint;
    import android.app.AlarmManager;
    import android.app.Notification;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.app.Service;
    import android.bluetooth.BluetoothAdapter;
    import android.bluetooth.BluetoothDevice;
    import android.bluetooth.le.ScanCallback;
    import android.bluetooth.le.ScanResult;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.Intent;
    import android.content.IntentFilter;
    import android.content.SharedPreferences;
    import android.content.pm.ServiceInfo;
    import android.os.Build;
    import android.os.IBinder;
    import android.os.ParcelUuid;
    import android.util.Log;
    
    import androidx.core.app.NotificationCompat;
    
    import com.example.project3.Activity.Student.AttendanceActivity;
    import com.example.project3.Body.AttendanceTimeBody;
    import com.example.project3.Body.StudentAttendanceBody;
    import com.example.project3.Interface.ApiInterface;
    import com.example.project3.Model.Schedule;
    import com.example.project3.Network.RetrofitClientInstance;
    import com.example.project3.R;
    import com.example.project3.Receiver.AlarmReceiver;
    import com.example.project3.Receiver.GetDailyScheduleReceiver;
    import com.example.project3.Response.ScheduleInDayResponse;
    import com.example.project3.Response.StoreAttendanceTimeResponse;
    import com.example.project3.Response.StudentAttendanceResponse;
    import com.example.project3.scanCallback.ScanCallbackHolder;
    
    import org.json.JSONObject;
    
    import java.nio.charset.Charset;
    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;
    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    
    public class ScanService extends Service {
        BluetoothAdapter bluetoothAdapter;
        private static final int NOTIFICATION_ID = 1;
        private static final String CHANNEL_ID = "BLE_CHANNEL";
        private ApiInterface apiInterface;
        private BroadcastReceiver apiStartReceiver;
        private String token;
        private HashMap<Integer, Boolean> attendanceMap = new HashMap<>();
        private HashMap<Integer, String> endTimeMap = new HashMap<>();
        private HashMap<Integer, Integer> requestCodeMap = new HashMap<>();
        private Boolean isHasDevice = false;
        private static final UUID DATA_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    
        @Override
        public void onCreate() {
            super.onCreate();
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
            SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
            token = prefs.getString("token", null);
    
            setDailyAlarm();
    
            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            if (currentHour >= 6) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                String date = year + "-" + month + "-" + dayOfMonth;
                apiResultDay(token, date);
            }
    
            apiStartReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if ("com.example.project3.START_API".equals(intent.getAction())) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        String date = year + "-" + month + "-" + dayOfMonth;
                        apiResultDay(token, date);
                    }
                }
            };
            IntentFilter filter = new IntentFilter("com.example.project3.START_API");
            registerReceiver(apiStartReceiver, filter, RECEIVER_EXPORTED);
        }
    
        private void setDailyAlarm() {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, GetDailyScheduleReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
    
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
    
            long triggerTimeMillis = calendar.getTimeInMillis();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTimeMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    
        private void apiResultDay(String authToken, String date) {
            Call<ScheduleInDayResponse> call = apiInterface.getScheduleInDay("Bearer " + authToken, date);
            call.enqueue(new Callback<ScheduleInDayResponse>() {
                @Override
                public void onResponse(Call<ScheduleInDayResponse> call, Response<ScheduleInDayResponse> response) {
                    if (response.isSuccessful()) {
                        ScheduleInDayResponse scheduleInDayResponse = response.body();
                        if (scheduleInDayResponse != null) {
                            List<Schedule> scheduleList = scheduleInDayResponse.getSchedule();
                            if (scheduleList != null || !scheduleList.isEmpty()) {
                                for (int i = 0; i < scheduleList.size(); i++) {
                                    Schedule schedule = scheduleList.get(i);
                                    LocalDateTime currentTime = LocalDateTime.now();
                                    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    String day = currentTime.format(dayFormatter);
                                    createStudentAttendance(schedule.getClassCode(),day);
                                    String[] startTimeParts = schedule.getStartTime().split(":");
                                    int start_hour = Integer.parseInt(startTimeParts[0]);
                                    int start_minute = Integer.parseInt(startTimeParts[1]);
                                    int start_second = Integer.parseInt(startTimeParts[2]);
                                    endTimeMap.put(schedule.getClassCode(),schedule.getEndTime());
                                    requestCodeMap.put(schedule.getClassCode(),i);
                                    setAlarmForClassStart(start_hour, start_minute, start_second, i, schedule.getRoom(), schedule.getClassCode());
                                    setAlarmForClassEnd(schedule.getEndTime(), schedule.getClassCode());
                                    showClassNotification(schedule);
                                }
                            }
                        }
                    } else {
                        try {
                            String errorBodyString = response.errorBody().string();
                            JSONObject errorObject = new JSONObject(errorBodyString);
                            String errorMessage = errorObject.getString("message");
                        } catch (Exception e) {
                            Log.e("Messgae", "Failed to parse error message", e);
                        }
                    }
                }
    
                @Override
                public void onFailure(Call<ScheduleInDayResponse> call, Throwable t) {
                    Log.e("API Error", "Failed to load schedule", t);
                }
            });
        }
        private void showClassNotification(Schedule schedule) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo2)
                    .setContentTitle("Lịch học hôm nay")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Lớp: " + schedule.getName() + "\nPhòng: " + schedule.getRoom() + "\nThời gian: " + schedule.getStartTime() + " - " + schedule.getEndTime()))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
    
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Class Schedule", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
    
            int notificationId = schedule.getClassCode();
            notificationManager.notify(notificationId, builder.build());
        }
    
        private void createStudentAttendance(int class_code, String day) {
            Call<StudentAttendanceResponse> call = apiInterface.createDayAttendance("Bearer" + token, new StudentAttendanceBody(class_code,day));
            call.enqueue(new Callback<StudentAttendanceResponse>() {
                @Override
                public void onResponse(Call<StudentAttendanceResponse> call, Response<StudentAttendanceResponse> response) {
                    if (response.isSuccessful()) {
                        StudentAttendanceResponse studentAttendanceResponse = response.body();
                        if (studentAttendanceResponse != null) {
                            attendanceMap.put(class_code, true);
                            setAlarmForClassEnd(endTimeMap.get(class_code),class_code);
                        } else {
                            Log.e("storeStudentAttendance", "Phản hồi không hợp lệ từ máy chủ");
                        }
                    } else {
                        try {
                            String errorBodyString = response.errorBody().string();
                            JSONObject errorObject = new JSONObject(errorBodyString);
                            String errorMessage = errorObject.getString("message");
                        } catch (Exception e) {
                            Log.e("JSON Parsing Error", "Failed to parse JSON response", e);
                            // In ra thông báo lỗi trong logcat
                            Log.e("JSON Parsing Error", "Error Message: " + e.getMessage());
                            // In ra stack trace của lỗi trong logcat
                            e.printStackTrace();
                        }
                    }
                }
    
                @Override
                public void onFailure(Call<StudentAttendanceResponse> call, Throwable t) {
                    Log.e("createStudentAttendance", "Lỗi: " + t.getMessage());
                }
            });
        }
    
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                startForeground(NOTIFICATION_ID, createNotification());
            } else {
                startForeground(NOTIFICATION_ID, createNotification(),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
            }
            return START_STICKY;
        }
    
        @SuppressLint("ScheduleExactAlarm")
        private void setAlarmForClassStart(int hour, int minute, int second, int requestCode, String room, int classCode) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
    
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
    
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
    
            long triggerTimeMillis = calendar.getTimeInMillis();
            boolean isAttendanced = false;
            attendanceMap.put(classCode, isAttendanced);
            initializeScanCallback(classCode, room);
            ScanCallbackHolder.setScanCallback(classCode,initializeScanCallback(classCode, room));
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.setAction("START_SERVICE_ACTION");
            intent.putExtra("classCode", classCode);
            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                flags |= PendingIntent.FLAG_IMMUTABLE;
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, flags);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
        }
    
        @SuppressLint("ScheduleExactAlarm")
        private void setAlarmForClassEnd(String endTime, int classCode) {
            String[] endTimeParts = endTime.split(":");
            int end_hour = Integer.parseInt(endTimeParts[0]);
            int end_minute = Integer.parseInt(endTimeParts[1]);
            int end_second = Integer.parseInt(endTimeParts[2]);
            int requestCode = requestCodeMap.get(classCode);
            Log.d("device", String.valueOf(isHasDevice));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
    
            calendar.set(Calendar.HOUR_OF_DAY, end_hour);
            calendar.set(Calendar.MINUTE, end_minute);
            calendar.set(Calendar.SECOND, end_second);
    
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
    
            long triggerTimeMillis = calendar.getTimeInMillis();
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.setAction("STOP_SERVICE_ACTION");
            intent.putExtra("classCode", classCode);
            intent.putExtra("token",token);
            intent.putExtra("isHasDevice",isHasDevice);
            intent.putExtra("isAttendance", attendanceMap.get(classCode));
            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                flags |= PendingIntent.FLAG_IMMUTABLE;
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, flags);
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
    
            pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, flags);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
        }
    
        @Override
        public void onDestroy() {
            super.onDestroy();
            stopForeground(true);
            unregisterReceiver(apiStartReceiver);
            apiStartReceiver = null;
        }
    
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    
        private Notification createNotification() {
            createNotificationChannel();
            Intent notificationIntent = new Intent(this, AttendanceActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
    
            return new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Chức năng điểm danh đang bật")
                    .setContentText("Nhấn để tắt chức năng điểm danh")
                    .setSmallIcon(R.drawable.logo2)
                    .setContentIntent(pendingIntent)
                    .build();
        }
    
        private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "BLE Channel";
                String description = "Channel for BLE notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
    
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
    
        private class MyScanCallback extends ScanCallback {
            private final String room;
            private final int classCode;
            private LocalDateTime firstScanTime = null;
            private Boolean isHasDevice = false;
            private Map<String, DeviceScanTime> deviceMap = new HashMap<>();
            BluetoothDevice device;
            BluetoothDevice attendanceDevice;
            public MyScanCallback(String room, int classCode) {
                this.room = room;
                this.classCode = classCode;
            }
    
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                device = result.getDevice();
                byte[] serviceData = result.getScanRecord().getServiceData(
                        ParcelUuid.fromString(String.valueOf(DATA_UUID)));
                if (device != null && serviceData != null) {
                    String message = new String(serviceData, Charset.forName("UTF-8"));
                    if (message != null && message.equals(room)) {
                        updateDeviceList(device, LocalDateTime.now());
                        if (firstScanTime == null) {
                            attendanceDevice = device;
                            isHasDevice = true;
                            firstScanTime = LocalDateTime.now();
                            DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String day = firstScanTime.format(dayFormatter);
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            String time = firstScanTime.format(timeFormatter);
                            String bluetoothAddress = bluetoothAdapter.getAddress();
                            storeStartAttendance(classCode, day, time, bluetoothAddress);
                        }
                    }
                }
                if (attendanceDevice != null && deviceMap.containsKey(attendanceDevice.getAddress())) {
                    LocalDateTime scanTime = deviceMap.get(attendanceDevice.getAddress()).getScanTime();
                    LocalDateTime now = LocalDateTime.now();
                    Duration duration = Duration.between(scanTime, now);
                    if (duration.toMinutes() < 1) {
                        isHasDevice = true;
                        if(!ScanService.this.isHasDevice){
                            ScanService.this.isHasDevice = this.isHasDevice;
                            setAlarmForClassEnd(endTimeMap.get(classCode),classCode);
                        }
                    } else {
                        isHasDevice = false;
                        if(ScanService.this.isHasDevice){
                            ScanService.this.isHasDevice = this.isHasDevice;
                            setAlarmForClassEnd(endTimeMap.get(classCode),classCode);
                        }
                    }
                }

            }
    
            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e("ScanClass", "Bluetooth LE scan failed with error code: " + errorCode);
            }
    
            private void updateDeviceList(BluetoothDevice device, LocalDateTime scanTime) {
                String deviceAddress = device.getAddress();
                if (deviceMap.containsKey(deviceAddress)) {
                    deviceMap.get(deviceAddress).setScanTime(scanTime);
                } else {
                    deviceMap.put(deviceAddress, new DeviceScanTime(device, scanTime));
                }
            }
    
            private class DeviceScanTime {
                private BluetoothDevice device;
                private LocalDateTime scanTime;
    
                public DeviceScanTime(BluetoothDevice device, LocalDateTime scanTime) {
                    this.device = device;
                    this.scanTime = scanTime;
                }
    
                public BluetoothDevice getDevice() {
                    return device;
                }
    
                public LocalDateTime getScanTime() {
                    return scanTime;
                }
    
                public void setScanTime(LocalDateTime scanTime) {
                    this.scanTime = scanTime;
                }
            }
        }
    
        private ScanCallback initializeScanCallback(int classCode, String room) {
            ScanCallback scanCallback = new MyScanCallback(room, classCode);
            return scanCallback;
        }
    
        private void storeStartAttendance(int class_code, String day, String time, String bluetooth_address) {
            String type = "start_time";
            Call<StoreAttendanceTimeResponse> call = apiInterface.storeAttendance("Bearer" + token, new AttendanceTimeBody(class_code, day, time, bluetooth_address, type));
            call.enqueue(new Callback<StoreAttendanceTimeResponse>() {
                @Override
                public void onResponse(Call<StoreAttendanceTimeResponse> call, Response<StoreAttendanceTimeResponse> response) {
                    if (response.isSuccessful()) {
                        StoreAttendanceTimeResponse storeAttendanceTimeResponse = response.body();
                        if (storeAttendanceTimeResponse != null) {
                            attendanceMap.put(class_code, true);
                            showAttendanceNotification(class_code);
                            setAlarmForClassEnd(endTimeMap.get(class_code),class_code);
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
    
        private void showAttendanceNotification(int classCode) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo2)
                    .setContentTitle("Điểm danh đầu giờ")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Điểm danh lớp " + classCode + " thành công.\nTrạng thái: Đi học"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
    
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Attendance Success", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
    
            int notificationId = ("daugio" + classCode).hashCode();
            notificationManager.notify(notificationId, builder.build());
        }
    
    }
    
