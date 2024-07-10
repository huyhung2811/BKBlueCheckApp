package com.example.project3.Activity.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.project3.Activity.Student.RequestDetailsActivity;
import com.example.project3.Activity.Student.StudentMainActivity;
import com.example.project3.Adapter.RequestAdapter;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.DayOffRequest;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.DayOffRequestResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestActivity extends AppCompatActivity {

    private Toolbar requestToolbar;
    private ListView lvRequest;
    private ApiInterface apiInterface;
    private RequestAdapter requestAdapter;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_teacher);

        requestToolbar = findViewById(R.id.request_toolbar);
        lvRequest = findViewById(R.id.request_list_view);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
        token = prefs.getString("token", null);

        requestToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestActivity.this, TeacherMainActivity.class);
                startActivity(intent);
                RequestActivity.this.finish();
            }
        });
        loadDayOffRequest(token);

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
                                    Intent intent = new Intent(RequestActivity.this, StudentRequestDetailsActivity.class);
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

}