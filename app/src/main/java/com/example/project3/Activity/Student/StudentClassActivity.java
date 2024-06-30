package com.example.project3.Activity.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.Activity.Common.CourseClassDetailsActivity;
import com.example.project3.Activity.Common.StudentDetailsActivity;
import com.example.project3.Adapter.ClassStudentsAdapter;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.Student;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.ClassStudentsResponse;
import com.example.project3.Response.ProfileStudentResponse;
import com.example.project3.Response.StudentClassResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentClassActivity extends AppCompatActivity {
    private Toolbar studetnClassToolbar;
    TextView student_class, sc_system, sc_unit;
    ListView lvClassStudents;
    private ApiInterface apiInterface;
    private ClassStudentsAdapter classStudentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class);

        studetnClassToolbar = findViewById(R.id.student_class_toolbar);
        student_class = findViewById(R.id.sc_student_class);
        sc_system = findViewById(R.id.sc_system);
        sc_unit = findViewById(R.id.sc_unit);
        lvClassStudents = findViewById(R.id.lvClassStudents);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        loadStudentClass(token);

        studetnClassToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentClassActivity.this, StudentMainActivity.class);
                startActivity(intent);
                StudentClassActivity.this.finish();
            }
        });
    }

    private void loadStudentClass(String authToken) {
        Call<StudentClassResponse> call = apiInterface.getStudentClass("Bearer " + authToken);
        call.enqueue(new Callback<StudentClassResponse>() {
            @Override
            public void onResponse(Call<StudentClassResponse> call, Response<StudentClassResponse> response) {
                if (response.isSuccessful()) {
                    StudentClassResponse studentClassResponse = response.body();
                    int classId = studentClassResponse.getClass_id();
                    String className = studentClassResponse.getClass_name();
                    String unit = studentClassResponse.getUnit();
                    String system = studentClassResponse.getSystem();
                    student_class.setText(className);
                    sc_system.setText(system);
                    sc_unit.setText(unit);
                    loadClassStudents(authToken,classId);
                } else {
                    try {
                        Log.d("a", String.valueOf(response));
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error message: " + errorMessage);
                        Toast.makeText(StudentClassActivity.this, "Failed to load user profile: " + errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                        Toast.makeText(StudentClassActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<StudentClassResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load user profile", t);
                Toast.makeText(StudentClassActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadClassStudents(String authToken, int classId) {
        Call<ClassStudentsResponse> call = apiInterface.getClassStudents("Bearer " + authToken, classId);
        call.enqueue(new Callback<ClassStudentsResponse>() {
            @Override
            public void onResponse(Call<ClassStudentsResponse> call, Response<ClassStudentsResponse> response) {
                if (response.isSuccessful()) {
                    ClassStudentsResponse classStudentsResponse = response.body();
                    List<Student> students = classStudentsResponse.getStudents();
                    if (students != null && !students.isEmpty()) {
                        String firstStudentName = students.get(0).getName();
                        Log.d("First Student Name", firstStudentName);
                    }
                    classStudentsAdapter = new ClassStudentsAdapter(StudentClassActivity.this, students);
                    lvClassStudents.setAdapter(classStudentsAdapter);
                    lvClassStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String student_code = classStudentsAdapter.getStudentCodeAtPosition(i);
                            Intent intent = new Intent(StudentClassActivity.this, StudentDetailsActivity.class);
                            intent.putExtra("studentCode", student_code);
                            startActivity(intent);
                        }
                    });
                } else {
                    try {
                        Log.d("a", String.valueOf(response));
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error message: " + errorMessage);
                        Toast.makeText(StudentClassActivity.this, "Failed to load user profile: " + errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                        Toast.makeText(StudentClassActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ClassStudentsResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load class students", t);
                Toast.makeText(StudentClassActivity.this, "Failed to load class students", Toast.LENGTH_SHORT).show();
            }
        });
    }
}