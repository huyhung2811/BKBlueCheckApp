package com.example.project3.Response;

import com.example.project3.Model.Student;
import com.example.project3.Model.System;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseClassDetailsResponse {
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
    @SerializedName("school_day")
    private String school_day;
    @SerializedName("start_time")
    private String start_time;
    @SerializedName("end_time")
    private String end_time;
    @SerializedName("room")
    private String room;
    @SerializedName("students")
    private List<Student> students;
    @SerializedName("students_number")
    private int student_numbers;
    @SerializedName("number_of_absenses")
    private int absenses_number;

    public CourseClassDetailsResponse(int classCode, String courseCode, String name, String type, List<System> systems, String semester, String educationFormat, String teacherName, String description, String school_day, String start_time, String end_time, String room, List<Student> students, int student_numbers, int absenses_number) {
        this.classCode = classCode;
        this.courseCode = courseCode;
        this.name = name;
        this.type = type;
        this.systems = systems;
        this.semester = semester;
        this.educationFormat = educationFormat;
        this.teacherName = teacherName;
        this.description = description;
        this.school_day = school_day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.room = room;
        this.students = students;
        this.student_numbers = student_numbers;
        this.absenses_number = absenses_number;
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

    public void setSemester(String semester) {
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getSchool_day() {
        return school_day;
    }

    public void setSchool_day(String school_day) {
        this.school_day = school_day;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getStudent_numbers() {
        return student_numbers;
    }

    public void setStudent_numbers(int student_numbers) {
        this.student_numbers = student_numbers;
    }

    public int getAbsenses_number() {
        return absenses_number;
    }

    public void setAbsenses_number(int absenses_number) {
        this.absenses_number = absenses_number;
    }
}
