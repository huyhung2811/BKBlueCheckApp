package com.example.project3.Activity.Student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project3.Activity.Common.CourseClassDetailsActivity;
import com.example.project3.Activity.LoginActivity;
import com.example.project3.Activity.Teacher.TeacherMainActivity;
import com.example.project3.Adapter.DayScheduleAdapter;
import com.example.project3.Adapter.RequestAdapter;
import com.example.project3.Body.LoginBody;
import com.example.project3.Body.NewRequestBody;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.DayOffRequest;
import com.example.project3.Model.Schedule;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.CreateRequestResponse;
import com.example.project3.Response.DayOffRequestResponse;
import com.example.project3.Response.LoginResponse;
import com.example.project3.Response.ScheduleInDayResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;

public class RequestActivity extends AppCompatActivity {
    private Toolbar requestToolbar;
    private ListView lvRequest;
    private FloatingActionButton btnAddRequest;
    private ApiInterface apiInterface;
    private RequestAdapter requestAdapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        requestToolbar = findViewById(R.id.request_toolbar);
        lvRequest = findViewById(R.id.request_list_view);
        btnAddRequest = findViewById(R.id.fab_add);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        token = prefs.getString("token", null);
        loadDayOffRequest(token);

        requestToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestActivity.this, StudentMainActivity.class);
                startActivity(intent);
                RequestActivity.this.finish();
            }
        });

        btnAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddRequestDialog();
            }
        });
    }

    private void loadDayOffRequest(String authToken) {
        Call<DayOffRequestResponse> call = apiInterface.getDayOffRequest("Bearer " + authToken);
        call.enqueue(new Callback<DayOffRequestResponse>() {
            @Override
            public void onResponse(Call<DayOffRequestResponse> call, Response<DayOffRequestResponse> response) {
                if (response.isSuccessful()) {
                    DayOffRequestResponse dayOffRequestResponse = response.body();
                    if (dayOffRequestResponse != null) {
                        List<DayOffRequest> dayOffRequests = dayOffRequestResponse.getDayOffRequests();
                        if (dayOffRequests == null || dayOffRequests.isEmpty()) {
                        } else {
                            requestAdapter = new RequestAdapter(RequestActivity.this, dayOffRequests);
                            lvRequest.setAdapter(requestAdapter);
                            lvRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    int id = requestAdapter.getRequestIdAtPosition(i);
                                    Intent intent = new Intent(RequestActivity.this, RequestDetailsActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        }
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
            public void onFailure(Call<DayOffRequestResponse> call, Throwable t) {
                Log.e("API Error", "Failed to load user profile", t);
            }
        });
    }

    private void showAddRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_request, null);

        EditText editTextDate = view.findViewById(R.id.edit_text_request_date);
        Spinner spinnerClass = view.findViewById(R.id.spinner_request_class);
        EditText editTextReason = view.findViewById(R.id.edit_text_request_reason);
        Button btnSubmit = view.findViewById(R.id.btn_submit_request);

        // Initially set the spinner to "Chọn Lớp" and disable it
        String[] initialClassOption = {"Chọn Lớp"};
        ArrayAdapter<String> initialAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, initialClassOption);
        initialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(initialAdapter);
        spinnerClass.setEnabled(false);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                                editTextDate.setText(date);
                                loadDaySchedule(token, date, spinnerClass, editTextReason);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = editTextDate.getText().toString();
                String selectedClass = spinnerClass.getSelectedItem().toString();
                String[] parts = selectedClass.split(" - ");
                int classCode = Integer.parseInt(parts[0]);
                String reason = editTextReason.getText().toString().trim();

                createNewRequest(date, classCode, reason, dialog);
            }
        });

        dialog.show();
    }

    private void loadDaySchedule(String authToken, String date, Spinner spinnerClass, EditText editTextReason) {
        Call<ScheduleInDayResponse> call = apiInterface.getScheduleInDay("Bearer " + authToken, date);
        call.enqueue(new Callback<ScheduleInDayResponse>() {
            @Override
            public void onResponse(Call<ScheduleInDayResponse> call, Response<ScheduleInDayResponse> response) {
                if (response.isSuccessful()) {
                    ScheduleInDayResponse scheduleResponse = response.body();
                    List<Schedule> schedules = scheduleResponse.getSchedule();
                    List<String> classTitles = new ArrayList<>();
                    classTitles.add("Chọn lớp");
                    for (Schedule schedule : schedules) {
                        classTitles.add(schedule.getClassCode() + " - " + schedule.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RequestActivity.this, android.R.layout.simple_spinner_item, classTitles);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerClass.setAdapter(adapter);
                    spinnerClass.setEnabled(true);
                    editTextReason.setEnabled(true);
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error message: " + errorMessage);
                        String[] classOption = {"Không có lịch học"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RequestActivity.this, android.R.layout.simple_spinner_item, classOption);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerClass.setAdapter(adapter);
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ScheduleInDayResponse> call, Throwable t) {
                Log.e("API Error", "Failed to load user profile", t);
            }
        });
    }

    private void createNewRequest(String day, int class_code, String reason, Dialog dialog) {
        Call<CreateRequestResponse> call = apiInterface.createDayOffRequest("Bearer " + token,new NewRequestBody(day,class_code,reason));
        call.enqueue(new Callback<CreateRequestResponse>() {
            @Override
            public void onResponse(Call<CreateRequestResponse> call, Response<CreateRequestResponse> response) {
                if (response.isSuccessful()) {
                    CreateRequestResponse createRequestResponse = response.body();
                    Toast.makeText(RequestActivity.this, createRequestResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    loadDayOffRequest(token);
                    dialog.hide();
                } else {
                    Toast.makeText(RequestActivity.this, "Tạo mới thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateRequestResponse> call, Throwable t) {
                Toast.makeText(RequestActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}