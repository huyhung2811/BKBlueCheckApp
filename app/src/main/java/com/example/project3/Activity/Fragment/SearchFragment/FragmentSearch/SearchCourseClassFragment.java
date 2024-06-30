package com.example.project3.Activity.Fragment.SearchFragment.FragmentSearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.System;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.SearchCourseClassResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchCourseClassFragment extends Fragment {
    private EditText searchInput;
    private Button searchButton;
    private CardView courseCard;
    TextView search_title, search_class_code, search_course_type, search_course_system, search_course_semester, search_class_teacher, search_class_type, search_description, resultText;
    private ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_course_class, container, false);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        searchInput = view.findViewById(R.id.search_input);
        searchButton = view.findViewById(R.id.search_button);
        search_title = view.findViewById(R.id.search_title);
        courseCard = view.findViewById(R.id.course_card);
        search_class_code = view.findViewById(R.id.search_class_code);
        search_course_type = view.findViewById(R.id.search_course_type);
        search_course_system = view.findViewById(R.id.search_course_system);
        search_course_semester = view.findViewById(R.id.search_course_semester);
        search_class_teacher = view.findViewById(R.id.search_class_teacher);
        search_class_type = view.findViewById(R.id.search_class_type);
        search_description = view.findViewById(R.id.search_description);
        resultText = view.findViewById(R.id.result_text);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = searchInput.getText().toString();
                if (!input.isEmpty()) {
                    getResults(token,input);
                } else {
                    Toast.makeText(getContext(), "Hãy nhập mã lớp học phần", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getResults(String authToken, String input) {
        Call<SearchCourseClassResponse> call = apiInterface.getSearchCourseClass("Bearer " + authToken, input);
        call.enqueue(new Callback<SearchCourseClassResponse>() {
            @Override
            public void onResponse(Call<SearchCourseClassResponse> call, Response<SearchCourseClassResponse> response) {
                if (response.isSuccessful()) {
                    SearchCourseClassResponse searchCourseClassResponse = response.body();
                    if (searchCourseClassResponse != null ) {
                        courseCard.setVisibility(View.VISIBLE);
                        search_title.setText(searchCourseClassResponse.getCourseCode() + " - " + searchCourseClassResponse.getName());
                        search_class_code.setText(String.valueOf(searchCourseClassResponse.getClassCode()));
                        search_course_type.setText(searchCourseClassResponse.getType());
                        List<System> systems = searchCourseClassResponse.getSystems();
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
                        search_course_system.setText(systemNames);
                        search_course_semester.setText(searchCourseClassResponse.getSemester());
                        search_class_teacher.setText(searchCourseClassResponse.getTeacherName());
                        search_class_type.setText(searchCourseClassResponse.getEducationFormat());
                        search_description.setText(searchCourseClassResponse.getDescription() != null ? searchCourseClassResponse.getDescription() : "Chưa có");
                    }
                } else {
                    try {
                        String errorBodyString = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorBodyString);
                        String errorMessage = errorObject.getString("message");
                        resultText.setText(errorMessage);
                        resultText.setVisibility(View.VISIBLE);
                        courseCard.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                        Toast.makeText(requireActivity(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    
            @Override
            public void onFailure(Call<SearchCourseClassResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load course search", t);
                Toast.makeText(requireActivity(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}