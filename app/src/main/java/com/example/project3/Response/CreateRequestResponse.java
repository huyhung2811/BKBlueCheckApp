package com.example.project3.Response;

import com.google.gson.annotations.SerializedName;

public class CreateRequestResponse {
    @SerializedName("message")
    private String message;

    public CreateRequestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
