package com.example.project3.Response;

import com.example.project3.Model.RequestDetails;
import com.google.gson.annotations.SerializedName;

public class DayOffRequestDetailsResponse {
    @SerializedName("request_details")
    private RequestDetails requestDetails;

    public DayOffRequestDetailsResponse(RequestDetails requestDetails) {
        this.requestDetails = requestDetails;
    }

    public RequestDetails getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(RequestDetails requestDetails) {
        this.requestDetails = requestDetails;
    }
}
