package com.example.project3.Body;

import com.google.gson.annotations.SerializedName;

public class AttendanceTimeBody {
    @SerializedName("class_code")
    private int class_code;
    @SerializedName("day")
    private String day;
    @SerializedName("time")
    private String time;
    @SerializedName("bluetooth_address")
    private String bluetooth_address;
    @SerializedName("type")
    private String type;

    public AttendanceTimeBody(int class_code, String day, String time, String bluetooth_address, String type) {
        this.class_code = class_code;
        this.day = day;
        this.time = time;
        this.bluetooth_address = bluetooth_address;
        this.type = type;
    }

    public int getClass_code() {
        return class_code;
    }

    public void setClass_code(int class_code) {
        this.class_code = class_code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBluetooth_address() {
        return bluetooth_address;
    }

    public void setBluetooth_address(String bluetooth_address) {
        this.bluetooth_address = bluetooth_address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
