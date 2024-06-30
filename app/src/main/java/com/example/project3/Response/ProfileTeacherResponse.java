package com.example.project3.Response;

import com.google.gson.annotations.SerializedName;

public class ProfileTeacherResponse {
    @SerializedName("name")
    private String name;
    @SerializedName("teacher_code")
    private String teacher_code;
    @SerializedName("email")
    private String email;
    @SerializedName("unit")
    private String unit;
    @SerializedName("avatar")
    private String avatar;

    public ProfileTeacherResponse(String name, String teacher_code, String email, String unit, String avatar) {
        this.name = name;
        this.teacher_code = teacher_code;
        this.email = email;
        this.unit = unit;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher_code() {
        return teacher_code;
    }

    public void setTeacher_code(String teacher_code) {
        this.teacher_code = teacher_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
