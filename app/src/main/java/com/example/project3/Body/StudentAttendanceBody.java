package com.example.project3.Body;

import com.google.gson.annotations.SerializedName;

public class StudentAttendanceBody {
    @SerializedName("class_code")
    private int class_code;
    @SerializedName("day")
    private String day;

    public StudentAttendanceBody(int class_code, String day) {
        this.class_code = class_code;
        this.day = day;
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
}
