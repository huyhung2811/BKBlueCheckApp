package com.example.project3.Model;

public class CourseClass {
    private int class_code;
    private String name;
    private int semester_id;
    private String course_code;
    private String teacher_code;
    private String room;
    private String education_format;
    private String description;
    private String class_type;
    private String school_day;
    private String start_time;
    private String end_time;

    public CourseClass(int class_code, String name, int semester_id, String course_code, String teacher_code, String room, String education_format, String description, String class_type, String school_day, String start_time, String end_time) {
        this.class_code = class_code;
        this.name = name;
        this.semester_id = semester_id;
        this.course_code = course_code;
        this.teacher_code = teacher_code;
        this.room = room;
        this.education_format = education_format;
        this.description = description;
        this.class_type = class_type;
        this.school_day = school_day;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClass_code() {
        return class_code;
    }

    public void setClass_code(int class_code) {
        this.class_code = class_code;
    }

    public int getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(int semester_id) {
        this.semester_id = semester_id;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getTeacher_code() {
        return teacher_code;
    }

    public void setTeacher_code(String teacher_code) {
        this.teacher_code = teacher_code;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getEducation_format() {
        return education_format;
    }

    public void setEducation_format(String education_format) {
        this.education_format = education_format;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClass_type() {
        return class_type;
    }

    public void setClass_type(String class_type) {
        this.class_type = class_type;
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
}
