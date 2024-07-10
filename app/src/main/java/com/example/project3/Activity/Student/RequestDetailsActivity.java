package com.example.project3.Activity.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.RequestDetails;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.DayOffRequestDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDetailsActivity extends AppCompatActivity {
    private int id;
    private Toolbar requestDetailsToolbar;
    private TextView className, classCode, courseCode, learnTime, studentName, studentCode, reason, createdTime, status, teacherName, updatedTime;
    private ApiInterface apiInterface;
    private String token;
    private LinearLayout layoutTeacherName, layoutUpdatedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        requestDetailsToolbar = findViewById(R.id.request_details_toolbar);
        className = findViewById(R.id.details_class_name);
        classCode = findViewById(R.id.details_class_code);
        courseCode = findViewById(R.id.details_course_code);
        learnTime = findViewById(R.id.details_learn_time);
        studentName = findViewById(R.id.details_student_name);
        studentCode = findViewById(R.id.details_student_code);
        reason = findViewById(R.id.details_reason);
        createdTime = findViewById(R.id.details_created_time);
        status = findViewById(R.id.details_status);
        teacherName = findViewById(R.id.details_teacher_name);
        updatedTime = findViewById(R.id.details_updated_time);
        layoutTeacherName = findViewById(R.id.layoutTeacherName);
        layoutUpdatedTime = findViewById(R.id.layoutUpdatedTime);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", id);
        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        token = prefs.getString("token", null);

        loadRequestDetails(id);

        requestDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailsActivity.this, RequestActivity.class);
                startActivity(intent);
                RequestDetailsActivity.this.finish();
            }
        });
    }

    private void loadRequestDetails(Integer id) {
        Call<DayOffRequestDetailsResponse> call = apiInterface.getDayOffRequestDetails("Bearer " + token,id);
        call.enqueue(new Callback<DayOffRequestDetailsResponse>() {
            @Override
            public void onResponse(Call<DayOffRequestDetailsResponse> call, Response<DayOffRequestDetailsResponse> response) {
                if (response.isSuccessful()) {
                    DayOffRequestDetailsResponse dayOffRequestDetailsResponse = response.body();
                    RequestDetails requestDetails = dayOffRequestDetailsResponse.getRequestDetails();
                    className.setText(requestDetails.getClassName());
                    classCode.setText(String.valueOf(requestDetails.getClassCode()));
                    courseCode.setText(requestDetails.getCourseCode());
                    learnTime.setText(requestDetails.getStartTime() + " - " + requestDetails.getEndTime());
                    studentName.setText(requestDetails.getStudentName());
                    studentCode.setText(String.valueOf(requestDetails.getStudentCode()));
                    reason.setText(requestDetails.getReason());
                    createdTime.setText(requestDetails.getCreatedTime());
                    status.setText(requestDetails.getStatus());
                    switch (requestDetails.getStatus()) {
                        case "Duyệt":
                            status.setBackgroundColor(Color.parseColor("#66FF99"));
                            break;
                        case "Từ chối":
                            status.setBackgroundColor(Color.parseColor("#FF9999"));
                            break;
                        case "Chờ duyệt":
                            status.setBackgroundColor(Color.parseColor("#FFFF99"));
                            break;
                        default:
                            status.setTextColor(Color.parseColor("#000000"));
                            break;
                    }
                    if(!requestDetails.getStatus().equals("Chờ duyệt")){
                        teacherName.setText(requestDetails.getTeacherName());
                        layoutTeacherName.setVisibility(View.VISIBLE);
                        updatedTime.setText(requestDetails.getUpdatedTime());
                        layoutUpdatedTime.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error message: " + errorMessage);

                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<DayOffRequestDetailsResponse> call, Throwable t) {
                Log.e("API Error", "Failed to load user profile", t);
            }
        });
    }
}