package com.example.project3.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GetDailyScheduleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent broadcastIntent = new Intent("com.example.project3.START_API");
        context.sendBroadcast(broadcastIntent);
    }
}
