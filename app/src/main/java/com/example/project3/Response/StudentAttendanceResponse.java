package com.example.project3.Response;

import com.google.gson.annotations.SerializedName;

public class StudentAttendanceResponse {
    @SerializedName("message")
    private String message;

    public StudentAttendanceResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
