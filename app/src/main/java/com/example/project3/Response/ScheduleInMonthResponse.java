package com.example.project3.Response;

import com.example.project3.Model.DaySchedule;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ScheduleInMonthResponse {
    @SerializedName("schedule_in_month")
    private Map<String, DaySchedule> schedule_in_month;

    public ScheduleInMonthResponse(Map<String, DaySchedule> schedule_in_month) {
        this.schedule_in_month = schedule_in_month;
    }

    public Map<String, DaySchedule> getSchedule_in_month() {
        return schedule_in_month;
    }

    public void setSchedule_in_month(Map<String, DaySchedule> schedule_in_month) {
        this.schedule_in_month = schedule_in_month;
    }
}
