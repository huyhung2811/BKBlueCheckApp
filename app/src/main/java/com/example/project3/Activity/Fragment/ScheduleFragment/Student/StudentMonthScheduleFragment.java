package com.example.project3.Activity.Fragment.ScheduleFragment.Student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.Activity.Common.CourseClassDetailsActivity;
import com.example.project3.Adapter.DayScheduleAdapter;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.DaySchedule;
import com.example.project3.Model.Schedule;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.ScheduleInDayResponse;
import com.example.project3.Response.ScheduleInMonthResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentMonthScheduleFragment extends Fragment {

    private CalendarView scheduleCalendar;
    private TextView txtCurrentDate, txtResultError;
    private ListView lvScheduleClasses;
    private ApiInterface apiInterface;
    private DayScheduleAdapter dayScheduleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_month_schedule, container, false);

        scheduleCalendar = view.findViewById(R.id.monthScheduleCalendar);
        txtCurrentDate = view.findViewById(R.id.txtCurrentDate);
        lvScheduleClasses = view.findViewById(R.id.lvScheduleClass);
        txtResultError = view.findViewById(R.id.txtResult);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + "-" + month + "-" + dayOfMonth;
        scheduleCalendar.setDate(calendar.getTimeInMillis());
        txtCurrentDate.setText(date);
        apiResultMonth(token, date);
        apiResultDay(token, date);

        scheduleCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                txtCurrentDate.setText(selectedDate);
                apiResultMonth(token, selectedDate);
                apiResultDay(token,selectedDate);
            }
        });

        return view;
    }

    private void apiResultMonth(String authToken, String date) {
        Call<ScheduleInMonthResponse> call = apiInterface.getScheduleInMonth("Bearer " + authToken, date);
        call.enqueue(new Callback<ScheduleInMonthResponse>() {
            @Override
            public void onResponse(Call<ScheduleInMonthResponse> call, Response<ScheduleInMonthResponse> response) {
                if (response.isSuccessful()) {
                    ScheduleInMonthResponse scheduleInMonthResponse = response.body();
                    if (scheduleInMonthResponse != null) {
                        Map<String, DaySchedule> scheduleInMonth = scheduleInMonthResponse.getSchedule_in_month();
                        List<String> daysWithSchedule = new ArrayList<>();
                        for (Map.Entry<String, DaySchedule> entry : scheduleInMonth.entrySet()) {
                            String day = entry.getKey();
                            DaySchedule daySchedule = entry.getValue();

                            if (!daySchedule.getSchedule().isEmpty()) {
                                daysWithSchedule.add(day);

                            }
                        }

                        Calendar calendar = Calendar.getInstance();

                        for (String day : daysWithSchedule) {
                            String[] parts = day.split("-");
                            int year = Integer.parseInt(parts[0]);
                            int month = Integer.parseInt(parts[1]) - 1;
                            int dayOfMonth = Integer.parseInt(parts[2]);

                            calendar.set(year, month, dayOfMonth);

                            long timeInMillis = calendar.getTimeInMillis();

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ScheduleInMonthResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load schedule", t);
                Toast.makeText(requireActivity(), "Failed to load schedule", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void apiResultDay(String authToken, String date) {
        Call<ScheduleInDayResponse> call = apiInterface.getScheduleInDay("Bearer " + authToken, date);
        call.enqueue(new Callback<ScheduleInDayResponse>() {
            @Override
            public void onResponse(Call<ScheduleInDayResponse> call, Response<ScheduleInDayResponse> response) {
                if (response.isSuccessful()) {
                    txtResultError.setVisibility(View.GONE);
                    ScheduleInDayResponse scheduleInDayResponse = response.body();
                    if (scheduleInDayResponse != null) {
                        List<Schedule> scheduleList = scheduleInDayResponse.getSchedule();
                        if(scheduleList == null || scheduleList.isEmpty()){
                            txtResultError.setText(scheduleInDayResponse.getDescription());
                            txtResultError.setVisibility(View.VISIBLE);
                            lvScheduleClasses.setVisibility(View.GONE);
                        } else {
                            dayScheduleAdapter = new DayScheduleAdapter(requireContext(), scheduleInDayResponse.getSchedule());
                            lvScheduleClasses.setAdapter(dayScheduleAdapter);
                            lvScheduleClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    int class_code = dayScheduleAdapter.getClassCodeAtPosition(i);
                                    Intent intent = new Intent(getContext(), CourseClassDetailsActivity.class);
                                    intent.putExtra("classCode", class_code);
                                    startActivity(intent);
                                }
                            });
                            lvScheduleClasses.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    try {
                        String errorBodyString = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorBodyString);
                        String errorMessage = errorObject.getString("message");
                        Log.d("D",errorBodyString);
                        txtResultError.setText(errorMessage);
                        txtResultError.setVisibility(View.VISIBLE);
                        lvScheduleClasses.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("Messgae", "Failed to parse error message", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ScheduleInDayResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load schedule", t);
                Toast.makeText(requireActivity(), "Failed to load schedule", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
