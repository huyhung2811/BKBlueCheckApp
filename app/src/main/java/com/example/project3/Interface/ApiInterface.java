package com.example.project3.Interface;

import com.example.project3.Activity.Common.StudentDetailsActivity;
import com.example.project3.Body.AttendanceTimeBody;
import com.example.project3.Body.LoginBody;
import com.example.project3.Body.NewRequestBody;
import com.example.project3.Body.StudentAttendanceBody;
import com.example.project3.Body.UpdateRequestBody;
import com.example.project3.Response.ClassStudentsResponse;
import com.example.project3.Response.CourseClassDetailsResponse;
import com.example.project3.Response.CourseClassInSemesterResponse;
import com.example.project3.Response.CreateRequestResponse;
import com.example.project3.Response.DayOffRequestDetailsResponse;
import com.example.project3.Response.DayOffRequestResponse;
import com.example.project3.Response.LoginResponse;
import com.example.project3.Response.LogoutResponse;
import com.example.project3.Response.ProfileStudentResponse;
import com.example.project3.Response.ProfileTeacherResponse;
import com.example.project3.Response.ScheduleInDayResponse;
import com.example.project3.Response.ScheduleInMonthResponse;
import com.example.project3.Response.SearchCourseClassResponse;
import com.example.project3.Response.SearchCourseResponse;
import com.example.project3.Response.SearchStudentResponse;
import com.example.project3.Response.SearchTeacherResponse;
import com.example.project3.Response.StoreAttendanceTimeResponse;
import com.example.project3.Response.StudentAttendanceResponse;
import com.example.project3.Response.StudentClassResponse;
import com.example.project3.Response.StudentDetailsResponse;
import com.example.project3.Response.TeacherDetailsResponse;
import com.example.project3.Response.UpdateRequestResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginBody loginBody);
    @POST("/api/logout")
    Call<LogoutResponse> logout(@Header("Authorization") String authToken);

    //Student
    @GET("/api/student/profile")
    Call<ProfileStudentResponse> getStudentProfile(@Header("Authorization") String authToken);
    @GET("/api/student/class")
    Call<StudentClassResponse> getStudentClass(@Header("Authorization") String authToken);
    @GET("/api/student/course-class")
    Call<CourseClassInSemesterResponse> getCourseClassInSemester(@Header("Authorization") String authToken, @Query("date") String date);
    @GET("/api/student/schedule-in-month")
    Call<ScheduleInMonthResponse> getScheduleInMonth(@Header("Authorization") String authToken, @Query("date") String date);
    @GET("/api/student/schedule-in-day")
    Call<ScheduleInDayResponse> getScheduleInDay(@Header("Authorization") String authToken, @Query("date") String date);
    @POST("/api/student/create-day-attendance")
    Call<StudentAttendanceResponse> createDayAttendance(@Header("Authorization") String authToken, @Body StudentAttendanceBody studentAttendanceBody);
    @PUT("/api/student/attendance")
    Call<StoreAttendanceTimeResponse> storeAttendance(@Header("Authorization") String authToken, @Body AttendanceTimeBody attendanceTimeBody);

    //Teacher
    @GET("/api/teacher/profile")
    Call<ProfileTeacherResponse> getTeacherProfile(@Header("Authorization") String authToken);
    @GET("/api/teacher/schedule-in-month")
    Call<ScheduleInMonthResponse> getTeacherScheduleInMonth(@Header("Authorization") String authToken, @Query("date") String date);
    @GET("/api/teacher/schedule-in-day")
    Call<ScheduleInDayResponse> getTeacherScheduleInDay(@Header("Authorization") String authToken, @Query("date") String date);
    @GET("/api/teacher/course-class")
    Call<CourseClassInSemesterResponse> getTeacherCourseClassInSemester(@Header("Authorization") String authToken, @Query("date") String date);

    //Search
    @POST("/api/search/student")
    Call<SearchStudentResponse> getSearchStudent(@Header("Authorization") String authToken, @Query("input") String input);
    @POST("/api/search/teacher")
    Call<SearchTeacherResponse> getSearchTeacher(@Header("Authorization") String authToken, @Query("input") String input);
    @POST("/api/search/course")
    Call<SearchCourseResponse> getSearchCourse(@Header("Authorization") String authToken, @Query("input") String input);
    @POST("/api/search/course-class")
    Call<SearchCourseClassResponse> getSearchCourseClass(@Header("Authorization") String authToken, @Query("input") String input);

    //Common
    @POST("/api/course-class-details")
    Call<CourseClassDetailsResponse> getCourseClassDetails(@Header("Authorization") String authToken, @Query("class_code") int classCode);
    @POST("/api/student-details")
    Call<StudentDetailsResponse> getStudentDetails(@Header("Authorization") String authToken, @Query("student_code") String studentCode);
    @POST("/api/teacher-details")
    Call<TeacherDetailsResponse> getTeacherDetails(@Header("Authorization") String authToken, @Query("teacher_code") String teacherCode);
    @POST("/api/class-students")
    Call<ClassStudentsResponse> getClassStudents(@Header("Authorization") String authToken, @Query("class_id")  int classId);
    @GET("/api/day-off/show")
    Call<DayOffRequestResponse> getDayOffRequest(@Header("Authorization") String authToken);
    @POST("/api/day-off/create")
    Call<CreateRequestResponse> createDayOffRequest(@Header("Authorization") String authToken, @Body NewRequestBody newRequestBody);
    @GET("/api/day-off/request-details")
    Call<DayOffRequestDetailsResponse> getDayOffRequestDetails(@Header("Authorization") String authToken, @Query("request_id") int id);
    @POST("/api/day-off/change-request-status")
    Call<UpdateRequestResponse> updateDayOffRequest(@Header("Authorization") String authToken, @Body UpdateRequestBody updateRequestBody);
}
