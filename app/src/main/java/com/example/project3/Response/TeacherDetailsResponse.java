package com.example.project3.Response;

import com.example.project3.Model.Student;
import com.example.project3.Model.Teacher;
import com.google.gson.annotations.SerializedName;

public class TeacherDetailsResponse {
    @SerializedName("teacher")
    private Teacher teacher;

    public TeacherDetailsResponse(Teacher teacher) {
        this.teacher = teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
