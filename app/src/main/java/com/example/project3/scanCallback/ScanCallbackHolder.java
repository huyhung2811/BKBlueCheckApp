package com.example.project3.scanCallback;

import android.bluetooth.le.ScanCallback;
import android.util.SparseArray;

public class ScanCallbackHolder {
    private static SparseArray<ScanCallback> scanCallbackMap = new SparseArray<>();

    public static synchronized void setScanCallback(int classCode, ScanCallback callback) {
        scanCallbackMap.put(classCode, callback);
    }

    public static synchronized ScanCallback getScanCallback(int classCode) {
        return scanCallbackMap.get(classCode);
    }

    public static synchronized void removeScanCallback(int classCode) {
        scanCallbackMap.remove(classCode);
    }
}
