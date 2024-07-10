package com.example.project3.Model;

import com.google.gson.annotations.SerializedName;

public class DayOffRequest {
    @SerializedName("id")
    private int id;
    @SerializedName("student_name")
    private String student_name;
    @SerializedName("student_avatar")
    private String student_avatar;
    @SerializedName("class_code")
    private int class_code;
    @SerializedName("class_name")
    private String class_name;
    @SerializedName("day")
    private String day;
    @SerializedName("created_time")
    private String created_time;
    @SerializedName("elapsed_time")
    private String elapsed_time;
    @SerializedName("reason")
    private String reason;
    @SerializedName("status")
    private String status;
    @SerializedName("is_read")
    private String is_read;

    public DayOffRequest(int id, String student_name, String student_avatar, int class_code, String class_name, String day, String created_time, String elapsed_time, String reason, String status, String is_read) {
        this.id = id;
        this.student_name = student_name;
        this.student_avatar = student_avatar;
        this.class_code = class_code;
        this.class_name = class_name;
        this.day = day;
        this.created_time = created_time;
        this.elapsed_time = elapsed_time;
        this.reason = reason;
        this.status = status;
        this.is_read = is_read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_avatar() {
        return student_avatar;
    }

    public void setStudent_avatar(String student_avatar) {
        this.student_avatar = student_avatar;
    }

    public int getClass_code() {
        return class_code;
    }

    public void setClass_code(int class_code) {
        this.class_code = class_code;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getElapsed_time() {
        return elapsed_time;
    }

    public void setElapsed_time(String elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }
}
