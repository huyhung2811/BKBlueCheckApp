package com.example.project3.Response;

import com.example.project3.Model.Student;

import java.util.List;

public class ClassStudentsResponse {
    private List<Student> students;

    public ClassStudentsResponse(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
