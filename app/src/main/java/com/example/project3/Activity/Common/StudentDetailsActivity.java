package com.example.project3.Activity.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project3.Activity.Schedule.ScheduleActivity;
import com.example.project3.Activity.Student.StudentMainActivity;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.ProfileStudentResponse;
import com.example.project3.Response.StudentDetailsResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDetailsActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private String studentCode;
    private ImageView avatar;
    private TextView name, email,student_code, student_unit, student_system, student_class;
    private Toolbar studentDetailsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        Intent intent = getIntent();
        studentCode = intent.getStringExtra("studentCode");
        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        studentDetailsToolbar = findViewById(R.id.student_details_toolbar);
        name = findViewById(R.id.dt_student_name);
        avatar = findViewById(R.id.dt_student_avatar);
        email = findViewById(R.id.dt_student_email);
        student_code = findViewById(R.id.dt_student_code);
        student_unit = findViewById(R.id.dt_student_unit);
        student_system = findViewById(R.id.dt_student_system);
        student_class = findViewById(R.id.dt_student_class);

        studentDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getStudentDetails(token,studentCode);
    }

    private void getStudentDetails(String authToken, String studentCode) {
        Call<StudentDetailsResponse> call = apiInterface.getStudentDetails("Bearer " + authToken,studentCode);
        call.enqueue(new Callback<StudentDetailsResponse>() {
            @Override
            public void onResponse(Call<StudentDetailsResponse> call, Response<StudentDetailsResponse> response) {
                if (response.isSuccessful()) {
                    StudentDetailsResponse student = response.body();
                    String data_name = student.getStudent().getName();
                    String data_email = student.getStudent().getEmail();
                    String data_student_code = String.valueOf(student.getStudent().getStudent_code());
                    String data_student_unit = student.getStudent().getUnit();
                    String data_student_system = student.getStudent().getSystem();
                    String data_student_class = student.getStudent().getClass_name();
                    String data_student_avatar = student.getStudent().getAvatar();
                    name.setText(data_name);
                    email.setText(data_email);
                    student_code.setText(data_student_code);
                    student_unit.setText(data_student_unit);
                    student_system.setText(data_student_system);
                    student_class.setText(data_student_class);
                    Picasso.get().load(data_student_avatar).into(avatar);

                } else {
                    try {
                        Log.d("a", String.valueOf(response));
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error message: " + errorMessage);
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);;
                    }
                }
            }

            @Override
            public void onFailure(Call<StudentDetailsResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load user profile", t);
            }
        });
    }
}
