package com.example.project3.Response;

import com.example.project3.Model.Course;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchCourseResponse {
    @SerializedName("course")
    private List<Course> course;

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }
}
