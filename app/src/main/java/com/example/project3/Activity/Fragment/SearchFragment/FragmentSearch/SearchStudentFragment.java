package com.example.project3.Activity.Fragment.SearchFragment.FragmentSearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.project3.Activity.Common.StudentDetailsActivity;
import com.example.project3.Adapter.ClassStudentsAdapter;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.Student;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.SearchStudentResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchStudentFragment extends Fragment {

    private EditText searchInput;
    private Button searchButton;
    private TextView resultText;
    private ListView studentListView;
    private ClassStudentsAdapter searchStudentsAdapter;
    private ApiInterface apiInterface;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_student, container, false);

        searchInput = view.findViewById(R.id.search_input);
        searchButton = view.findViewById(R.id.search_button);
        resultText = view.findViewById(R.id.result_text);
        studentListView = view.findViewById(R.id.lv_student_result);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = searchInput.getText().toString();
                if (!input.isEmpty()) {
                    getResults(token,input);
                } else {
                    Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getResults(String authToken, String input) {
        Call<SearchStudentResponse> call = apiInterface.getSearchStudent("Bearer " + authToken, input);
        call.enqueue(new Callback<SearchStudentResponse>() {
            @Override
            public void onResponse(Call<SearchStudentResponse> call, Response<SearchStudentResponse> response) {
                if (response.isSuccessful()) {
                    SearchStudentResponse searchStudentResponse = response.body();
                    List<Student> students = searchStudentResponse.getStudents();
                    if (students != null && !students.isEmpty()) {
                        resultText.setText("Kết quả " + "( " + students.size()  + " )");
                        resultText.setVisibility(View.VISIBLE);
                        searchStudentsAdapter = new ClassStudentsAdapter(requireContext(), students);
                        studentListView.setAdapter(searchStudentsAdapter);
                        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String student_code = searchStudentsAdapter.getStudentCodeAtPosition(i);
                                Intent intent = new Intent(requireActivity(), StudentDetailsActivity.class);
                                intent.putExtra("studentCode", student_code);
                                startActivity(intent);
                            }
                        });
                        studentListView.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        String errorBodyString = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorBodyString);
                        String errorMessage = errorObject.getString("message");
                        Log.d("D",errorBodyString);
                        resultText.setText(errorMessage);
                        resultText.setVisibility(View.VISIBLE);
                        studentListView.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("Messgae", "Failed to parse error message", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchStudentResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("API Error", "Failed to load user profile" + errorMessage);
            }
        });
    }
}