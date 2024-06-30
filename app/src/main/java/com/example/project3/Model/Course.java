package com.example.project3.Model;

import java.util.List;
import java.util.Map;

public class Course {
    private String course_code;
    private String name;
    private String type;
    private int number_of_credit;
    private List<CourseClass> classes;
    private List<System> systems;

    public Course(String course_code, String name, String type, int number_of_credit, List<CourseClass> classes, List<System> systems) {
        this.course_code = course_code;
        this.name = name;
        this.type = type;
        this.number_of_credit = number_of_credit;
        this.classes = classes;
        this.systems = systems;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
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

    public int getNumber_of_credit() {
        return number_of_credit;
    }

    public void setNumber_of_credit(int number_of_credit) {
        this.number_of_credit = number_of_credit;
    }

    public List<CourseClass> getClasses() {
        return classes;
    }

    public void setClasses(List<CourseClass> classes) {
        this.classes = classes;
    }

    public List<System> getSystems() {
        return systems;
    }

    public void setSystems(List<System> systems) {
        this.systems = systems;
    }
}
