package com.example.project3.Model;

import java.util.List;

public class DaySchedule {
    private boolean is_holiday;
    private List<Schedule> schedule;
    private String description;

    public boolean isHoliday() {
        return is_holiday;
    }

    public void setHoliday(boolean holiday) {
        is_holiday = holiday;
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
