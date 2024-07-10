package com.example.project3.Body;

import com.google.gson.annotations.SerializedName;

public class NewRequestBody {
    @SerializedName("day")
    private String day;
    @SerializedName("class_code")
    private int class_code;
    @SerializedName("reason")
    private String reason;

    public NewRequestBody(String day, int class_code, String reason) {
        this.day = day;
        this.class_code = class_code;
        this.reason = reason;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getClass_code() {
        return class_code;
    }

    public void setClass_code(int class_code) {
        this.class_code = class_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
