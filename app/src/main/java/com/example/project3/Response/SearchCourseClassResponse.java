package com.example.project3.Response;

import com.example.project3.Model.System;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchCourseClassResponse {
    @SerializedName("class_code")
    private int classCode;
    @SerializedName("course_code")
    private String courseCode;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("systems")
    private List<System> systems;
    @SerializedName("semester")
    private String semester;
    @SerializedName("education_format")
    private String educationFormat;
    @SerializedName("teacher_name")
    private String teacherName;
    @SerializedName("description")
    private String description;

    public SearchCourseClassResponse(int classCode, String courseCode, String name, String type, List<System> systems, String semester, String educationFormat, String teacherName, String description) {
        this.classCode = classCode;
        this.courseCode = courseCode;
        this.name = name;
        this.type = type;
        this.systems = systems;
        this.semester = semester;
        this.educationFormat = educationFormat;
        this.teacherName = teacherName;
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<System> getSystems() {
        return systems;
    }

    public void setSystems(List<System> systems) {
        this.systems = systems;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semesterId) {
        this.semester = semester;
    }

    public String getEducationFormat() {
        return educationFormat;
    }

    public void setEducationFormat(String educationFormat) {
        this.educationFormat = educationFormat;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
