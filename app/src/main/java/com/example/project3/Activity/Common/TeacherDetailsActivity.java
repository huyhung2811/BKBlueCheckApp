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

import com.example.project3.Interface.ApiInterface;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.StudentDetailsResponse;
import com.example.project3.Response.TeacherDetailsResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherDetailsActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private String teacherCode;
    private ImageView avatar;
    private TextView name, email, teacher_code, teacher_unit;
    private Toolbar teacherDetailsToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);

        Intent intent = getIntent();
        teacherCode = intent.getStringExtra("teacherCode");
        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        teacherDetailsToolbar = findViewById(R.id.teacher_details_toolbar);
        name = findViewById(R.id.dt_teacher_name);
        email =findViewById(R.id.dt_teacher_email);
        avatar = findViewById(R.id.dt_teacher_avatar);
        teacher_code = findViewById(R.id.dt_teacher_code);
        teacher_unit = findViewById(R.id.dt_teacher_unit);

        teacherDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getTeacherDetails(token,teacherCode);
    }

    private void getTeacherDetails(String authToken, String teacherCode) {
        Call<TeacherDetailsResponse> call = apiInterface.getTeacherDetails("Bearer " + authToken,teacherCode);
        call.enqueue(new Callback<TeacherDetailsResponse>() {
            @Override
            public void onResponse(Call<TeacherDetailsResponse> call, Response<TeacherDetailsResponse> response) {
                if (response.isSuccessful()) {
                    TeacherDetailsResponse teacher = response.body();
                    String data_name = teacher.getTeacher().getName();
                    String data_email = teacher.getTeacher().getEmail();
                    String data_student_code = String.valueOf(teacher.getTeacher().getTeacher_code());
                    String data_student_unit = teacher.getTeacher().getUnit();
                    String data_student_avatar = teacher.getTeacher().getAvatar();
                    name.setText(data_name);
                    email.setText(data_email);
                    teacher_code.setText(data_student_code);
                    teacher_unit.setText(data_student_unit);
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
            public void onFailure(Call<TeacherDetailsResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load user profile", t);
            }
        });
    }
}