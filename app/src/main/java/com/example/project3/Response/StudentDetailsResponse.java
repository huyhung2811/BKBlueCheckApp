package com.example.project3.Response;

import com.example.project3.Model.Student;
import com.google.gson.annotations.SerializedName;

public class StudentDetailsResponse {
    @SerializedName("student")
    private Student student;

    public StudentDetailsResponse(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
