package com.example.project3.Response;

import com.example.project3.Model.DayOffRequest;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DayOffRequestResponse {
    @SerializedName("day_off_request")
    private List<DayOffRequest> dayOffRequests;

    public DayOffRequestResponse(List<DayOffRequest> dayOffRequests) {
        this.dayOffRequests = dayOffRequests;
    }

    public List<DayOffRequest> getDayOffRequests() {
        return dayOffRequests;
    }

    public void setDayOffRequests(List<DayOffRequest> dayOffRequests) {
        this.dayOffRequests = dayOffRequests;
    }
}
