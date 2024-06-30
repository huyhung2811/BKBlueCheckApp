package com.example.project3.Model;

public class Schedule {
    private int class_code;
    private String name;
    private String course_code;
    private String room;
    private String school_day;
    private String start_time;
    private String end_time;

    public int getClassCode() {
        return class_code;
    }

    public void setClassCode(int class_code) {
        this.class_code = class_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return course_code;
    }

    public void setCourseCode(String course_code) {
        this.course_code = course_code;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSchoolDay() {
        return school_day;
    }

    public void setSchoolDay(String school_day) {
        this.school_day = school_day;
    }

    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }
}
