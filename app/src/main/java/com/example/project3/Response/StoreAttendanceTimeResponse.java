package com.example.project3.Response;

import com.google.gson.annotations.SerializedName;

public class StoreAttendanceTimeResponse {
    @SerializedName("message")
    private String message;
    public StoreAttendanceTimeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
