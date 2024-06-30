package com.example.project3.Model;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("student_code")
    private String student_code;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("class")
    private String class_name;
    @SerializedName("system")
    private String system;
    @SerializedName("unit")
    private String unit;

    public Student(String student_code, String name, String address, String email, String avatar, String class_name, String system, String unit) {
        this.student_code = student_code;
        this.name = name;
        this.address = address;
        this.email = email;
        this.avatar = avatar;
        this.class_name = class_name;
        this.system = system;
        this.unit = unit;
    }

    public String getStudent_code() {
        return student_code;
    }

    public void setStudent_code(String student_code) {
        this.student_code = student_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
