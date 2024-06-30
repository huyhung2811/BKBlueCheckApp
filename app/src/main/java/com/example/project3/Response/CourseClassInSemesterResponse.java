package com.example.project3.Response;

import com.example.project3.Model.CourseClass;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseClassInSemesterResponse {
    @SerializedName("course_classes")
    List<CourseClass> courseClassesInSemester;

    public CourseClassInSemesterResponse(List<CourseClass> courseClassesInSemester) {
        this.courseClassesInSemester = courseClassesInSemester;
    }

    public List<CourseClass> getCourseClassesInSemester() {
        return courseClassesInSemester;
    }

    public void setCourseClassesInSemester(List<CourseClass> courseClassesInSemester) {
        this.courseClassesInSemester = courseClassesInSemester;
    }
}
