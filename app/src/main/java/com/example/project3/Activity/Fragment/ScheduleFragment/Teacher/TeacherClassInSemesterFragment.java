package com.example.project3.Activity.Fragment.ScheduleFragment.Teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project3.Activity.Common.CourseClassDetailsActivity;
import com.example.project3.Adapter.CourseClassInSemesterAdapter;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.CourseClassInSemesterResponse;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherClassInSemesterFragment extends Fragment {
    ListView lvCourseClassInSemester;
    private ApiInterface apiInterface;
    private CourseClassInSemesterAdapter courseClassInSemesterAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_class_in_semester, container, false);

        lvCourseClassInSemester = view.findViewById(R.id.lvCourseClassInSemester);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + "-" + month + "-" + dayOfMonth;

        apiResult(token, date);

        return view;
    }

    private void apiResult(String authToken, String date) {
        Call<CourseClassInSemesterResponse> call = apiInterface.getTeacherCourseClassInSemester("Bearer " + authToken, date);
        call.enqueue(new Callback<CourseClassInSemesterResponse>() {
            @Override
            public void onResponse(Call<CourseClassInSemesterResponse> call, Response<CourseClassInSemesterResponse> response) {
                if (response.isSuccessful()) {
                    CourseClassInSemesterResponse courseClassInSemesterResponse = response.body();
                    if (courseClassInSemesterResponse != null) {
                        Log.d("A",courseClassInSemesterResponse.getCourseClassesInSemester().get(0).getName());
                        courseClassInSemesterAdapter = new CourseClassInSemesterAdapter(requireContext(),courseClassInSemesterResponse.getCourseClassesInSemester());
                        lvCourseClassInSemester.setAdapter(courseClassInSemesterAdapter);
                        lvCourseClassInSemester.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int class_code = courseClassInSemesterAdapter.getClassCodeAtPosition(i);
                                Intent intent = new Intent(getContext(), CourseClassDetailsActivity.class);
                                intent.putExtra("classCode", class_code);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CourseClassInSemesterResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load schedule", t);
                Toast.makeText(requireActivity(), "Failed to load schedule", Toast.LENGTH_SHORT).show();
            }
        });
    }
}