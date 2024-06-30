package com.example.project3.Response;

import com.example.project3.Model.Student;

import java.util.List;

public class SearchStudentResponse {
    private List<Student> students;

    public SearchStudentResponse(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
