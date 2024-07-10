package com.example.project3.Body;

import com.google.gson.annotations.SerializedName;

public class UpdateRequestBody {
    @SerializedName("request_id")
    private int requestId;
    @SerializedName("status")
    private String status;

    public UpdateRequestBody(int requestId, String status) {
        this.requestId = requestId;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
