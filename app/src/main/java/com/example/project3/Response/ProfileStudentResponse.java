package com.example.project3.Response;

import com.google.gson.annotations.SerializedName;

public class ProfileStudentResponse {
    @SerializedName("name")
    private String name;
    @SerializedName("student_code")
    private int studentCode;
    @SerializedName("birth_date")
    private String birthDate;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private String address;
    @SerializedName("home_town")
    private String homeTown;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("student_class")
    private String studentClass;
    @SerializedName("student_course")
    private int studentCourse;
    @SerializedName("system")
    private String system;
    @SerializedName("unit")
    private String unit;

    public ProfileStudentResponse(String name, int studentCode, String birthDate, String phone, String address, String homeTown, String email, String avatar, String studentClass, int studentCourse, String system, String unit) {
        this.name = name;
        this.studentCode = studentCode;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.homeTown = homeTown;
        this.email = email;
        this.avatar = avatar;
        this.studentClass = studentClass;
        this.studentCourse = studentCourse;
        this.system = system;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(int studentCode) {
        this.studentCode = studentCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public int getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(int studentCourse) {
        this.studentCourse = studentCourse;
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
