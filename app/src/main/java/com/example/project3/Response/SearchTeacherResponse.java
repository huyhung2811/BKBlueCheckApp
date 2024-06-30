package com.example.project3.Response;

import com.example.project3.Model.Teacher;

import java.util.List;

public class SearchTeacherResponse {
    private List<Teacher> teachers;
    public SearchTeacherResponse(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}
