package com.example.project3.Activity.Fragment.SearchFragment.FragmentSearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.Activity.Common.CourseClassDetailsActivity;
import com.example.project3.Adapter.CourseClassAdapter;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.Course;
import com.example.project3.Model.System;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.SearchCourseResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchCourseFragment extends Fragment {
    private EditText searchInput;
    private Button searchButton;
    private TextView resultText, searchCourseName, searchCourseType, searchCourseSystem, searchCourseCredit;
    private ListView lvCourseClasses;
    private CardView cardCourseInfo;
    private CourseClassAdapter courseClassAdapter;
    private ApiInterface apiInterface;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search_course, container, false);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        searchInput = view.findViewById(R.id.search_input);
        searchButton = view.findViewById(R.id.search_button);
        resultText = view.findViewById(R.id.result_text);
        searchCourseName = view.findViewById(R.id.search_course_name);
        searchCourseType = view.findViewById(R.id.search_course_type);
        searchCourseSystem = view.findViewById(R.id.search_course_system);
        searchCourseCredit = view.findViewById(R.id.search_course_credit);
        cardCourseInfo = view.findViewById(R.id.course_card);
        lvCourseClasses = view.findViewById(R.id.lv_course_classes);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = searchInput.getText().toString();
                if (!input.isEmpty()) {
                    getResults(token,input);
                } else {
                    Toast.makeText(getContext(), "Hãy nhập mã học phần", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getResults(String authToken, String input) {
        Call<SearchCourseResponse> call = apiInterface.getSearchCourse("Bearer " + authToken, input);
        call.enqueue(new Callback<SearchCourseResponse>() {
            @Override
            public void onResponse(Call<SearchCourseResponse> call, Response<SearchCourseResponse> response) {
                if (response.isSuccessful()) {
                    SearchCourseResponse searchCourseResponse = response.body();
                    List<Course> courses = searchCourseResponse.getCourse();
                    if (courses != null && !courses.isEmpty()) {
                        Log.d("Course Name", courses.get(0).getName());
                        searchCourseName.setText(courses.get(0).getName());
                        searchCourseType.setText(courses.get(0).getType());

                        List<System> systems = courses.get(0).getSystems();
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

                        searchCourseSystem.setText(systemNames);
                        searchCourseCredit.setText(String.valueOf(courses.get(0).getNumber_of_credit()));

                        courseClassAdapter = new CourseClassAdapter(requireContext(), courses.get(0).getClasses(), courses.get(0).getName());
                        lvCourseClasses.setAdapter(courseClassAdapter);
                        lvCourseClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int class_code = courseClassAdapter.getClassCodeAtPosition(i);
                                Intent intent = new Intent(getContext(), CourseClassDetailsActivity.class);
                                intent.putExtra("classCode", class_code);
                                startActivity(intent);
                            }
                        });

                        cardCourseInfo.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        String errorBodyString = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorBodyString);
                        String errorMessage = errorObject.getString("message");
                        resultText.setText(errorMessage);
                        resultText.setVisibility(View.VISIBLE);
                        cardCourseInfo.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                        Toast.makeText(requireActivity(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchCourseResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load course search", t);
                Toast.makeText(requireActivity(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}