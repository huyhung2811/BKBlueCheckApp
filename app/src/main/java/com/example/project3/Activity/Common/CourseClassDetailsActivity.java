package com.example.project3.Activity.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.Activity.Schedule.ScheduleActivity;
import com.example.project3.Activity.Student.StudentClassActivity;
import com.example.project3.Activity.Student.StudentMainActivity;
import com.example.project3.Adapter.ClassStudentsAdapter;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.Student;
import com.example.project3.Model.System;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.CourseClassDetailsResponse;
import com.example.project3.Response.SearchCourseClassResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseClassDetailsActivity extends AppCompatActivity {
    private int classCode;
    private ListView lvCourseClassStudents;
    private ClassStudentsAdapter classStudentsAdapter;
    TextView details_title, details_class_code, details_course_type, details_course_system, details_course_semester, details_class_teacher, details_class_type, details_description, details_absenses, details_number_students;
    private ApiInterface apiInterface;
    private Toolbar sc_details_toolbar;
    private LinearLayout ln_absenses, ln_nb_students;
    private String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_class_details);

        Intent intent = getIntent();
        classCode = intent.getIntExtra("classCode", classCode);
        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        role = prefs.getString("role",null);

        details_title = findViewById(R.id.details_title);
        details_class_code = findViewById(R.id.details_class_code);
        details_course_type = findViewById(R.id.details_course_type);
        details_course_system = findViewById(R.id.details_course_system);
        details_course_semester = findViewById(R.id.details_course_semester);
        details_class_teacher = findViewById(R.id.details_class_teacher);
        details_class_type = findViewById(R.id.details_class_type);
        details_description = findViewById(R.id.details_description);
        details_absenses = findViewById(R.id.details_absenses);
        details_number_students = findViewById(R.id.details_number_students);
        sc_details_toolbar = findViewById(R.id.sc_details_toolbar);
        lvCourseClassStudents = findViewById(R.id.lvCourseClassStudents);
        ln_absenses = findViewById(R.id.layout_absenses);
        ln_nb_students = findViewById(R.id.layout_nb_students);

        sc_details_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getResults(token,classCode);
    }

    private void getResults(String authToken, int classCode) {
        Call<CourseClassDetailsResponse> call = apiInterface.getCourseClassDetails("Bearer " + authToken, classCode);
        call.enqueue(new Callback<CourseClassDetailsResponse>() {
            @Override
            public void onResponse(Call<CourseClassDetailsResponse> call, Response<CourseClassDetailsResponse> response) {
                if (response.isSuccessful()) {
                    CourseClassDetailsResponse courseClassDetailsResponse = response.body();
                    if (courseClassDetailsResponse != null ) {
                        details_title.setText(courseClassDetailsResponse.getCourseCode() + " - " + courseClassDetailsResponse.getName());
                        details_class_code.setText(String.valueOf(courseClassDetailsResponse.getClassCode()));
                        details_course_type.setText(courseClassDetailsResponse.getType());
                        List<System> systems = courseClassDetailsResponse.getSystems();
                        StringBuilder systemNamesBuilder = new StringBuilder();
                        for (System system : systems) {
                            String systemName = system.getName();
                            systemNamesBuilder.append(systemName);
                            systemNamesBuilder.append(", ");
                        }
                        if (systemNamesBuilder.length() > 0) {
                            systemNamesBuilder.deleteCharAt(systemNamesBuilder.length() - 1);
                            systemNamesBuilder.deleteCharAt(systemNamesBuilder.length() - 1);
                        }
                        String systemNames = systemNamesBuilder.toString();
                        details_course_system.setText(systemNames);
                        details_course_semester.setText(courseClassDetailsResponse.getSemester());
                        details_class_teacher.setText(courseClassDetailsResponse.getTeacherName());
                        details_class_type.setText(courseClassDetailsResponse.getEducationFormat());
                        if(role.equals("student")){
                            if(courseClassDetailsResponse.getAbsenses_number() >= 0){
                                details_absenses.setText(String.valueOf(courseClassDetailsResponse.getAbsenses_number()));
                                ln_absenses.setVisibility(View.VISIBLE);
                            }
                            if(courseClassDetailsResponse.getStudent_numbers() >= 0){
                                details_number_students.setText(String.valueOf(courseClassDetailsResponse.getStudent_numbers()));
                                ln_nb_students.setVisibility(View.VISIBLE);
                            }
                        }
                        details_description.setText(courseClassDetailsResponse.getDescription() != null ? courseClassDetailsResponse.getDescription() : "Chưa có");
                        List<Student> courseClassStudents = courseClassDetailsResponse.getStudents();
                        classStudentsAdapter = new ClassStudentsAdapter(CourseClassDetailsActivity.this, courseClassStudents);
                        lvCourseClassStudents.setAdapter(classStudentsAdapter);
                        lvCourseClassStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String student_code = classStudentsAdapter.getStudentCodeAtPosition(i);
                                Intent intent = new Intent(CourseClassDetailsActivity.this, StudentDetailsActivity.class);
                                intent.putExtra("studentCode", student_code);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    try {
                        String errorBodyString = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorBodyString);
                        String errorMessage = errorObject.getString("message");
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CourseClassDetailsResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load course search", t);
            }
        });
    }
}