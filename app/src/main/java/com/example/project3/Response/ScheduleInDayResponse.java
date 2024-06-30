package com.example.project3.Response;

import com.example.project3.Model.DaySchedule;
import com.example.project3.Model.Schedule;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ScheduleInDayResponse {
    @SerializedName("is_holiday")
    private boolean is_holiday;
    @SerializedName("schedule_in_day")
    private List<Schedule> schedule;
    @SerializedName("description")
    private String description;

    public ScheduleInDayResponse(boolean is_holiday, List<Schedule> schedule, String description) {
        this.is_holiday = is_holiday;
        this.schedule = schedule;
        this.description = description;
    }

    public boolean isIs_holiday() {
        return is_holiday;
    }

    public void setIs_holiday(boolean is_holiday) {
        this.is_holiday = is_holiday;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
