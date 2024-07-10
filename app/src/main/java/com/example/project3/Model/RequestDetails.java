package com.example.project3.Model;

import com.google.gson.annotations.SerializedName;

public class RequestDetails {
    @SerializedName("id")
    private int id;
    @SerializedName("student_name")
    private String studentName;
    @SerializedName("student_code")
    private int studentCode;
    @SerializedName("student_avatar")
    private String studentAvatar;
    @SerializedName("class_name")
    private String className;
    @SerializedName("class_code")
    private int classCode;
    @SerializedName("course_code")
    private String courseCode;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("teacher_name")
    private String teacherName;
    @SerializedName("teacher_code")
    private String teacherCode;
    @SerializedName("teacher_avatar")
    private String teacherAvatar;
    @SerializedName("day")
    private String day;
    @SerializedName("created_time")
    private String createdTime;
    @SerializedName("updated_time")
    private String updatedTime;
    @SerializedName("reason")
    private String reason;
    @SerializedName("status")
    private String status;

    public RequestDetails(int id, String studentName, int studentCode, String studentAvatar, String className, int classCode, String courseCode, String startTime, String endTime, String teacherName, String teacherCode, String teacherAvatar, String day, String createdTime, String updatedTime, String reason, String status) {
        this.id = id;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.studentAvatar = studentAvatar;
        this.className = className;
        this.classCode = classCode;
        this.courseCode = courseCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teacherName = teacherName;
        this.teacherCode = teacherCode;
        this.teacherAvatar = teacherAvatar;
        this.day = day;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.reason = reason;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(int studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassCode() {
        return classCode;
    }

    public void setClassCode(int classCode) {
        this.classCode = classCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
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
}
