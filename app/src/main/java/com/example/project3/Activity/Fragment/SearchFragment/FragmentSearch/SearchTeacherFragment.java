package com.example.project3.Activity.Fragment.SearchFragment.FragmentSearch;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.Activity.Common.StudentDetailsActivity;
import com.example.project3.Activity.Common.TeacherDetailsActivity;
import com.example.project3.Adapter.TeacherAdapter;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Model.Teacher;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.SearchTeacherResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchTeacherFragment extends Fragment {

    private EditText searchInput;
    private Button searchButton;
    private TextView resultText;
    private ListView teacherListView;
    private TeacherAdapter searchTeachersAdapter;
    private ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_teacher, container, false);

        searchInput = view.findViewById(R.id.search_teacher_input);
        searchButton = view.findViewById(R.id.search_button);
        resultText = view.findViewById(R.id.result_text);
        teacherListView = view.findViewById(R.id.lv_teacher_result);

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
        Call<SearchTeacherResponse> call = apiInterface.getSearchTeacher("Bearer " + authToken, input);
        call.enqueue(new Callback<SearchTeacherResponse>() {
            @Override
            public void onResponse(Call<SearchTeacherResponse> call, Response<SearchTeacherResponse> response) {
                if (response.isSuccessful()) {
                    SearchTeacherResponse searchTeacherResponse = response.body();
                    List<Teacher> teachers = searchTeacherResponse.getTeachers();
                    if (teachers != null && !teachers.isEmpty()) {
                        resultText.setText("Kết quả " + "( " + teachers.size()  + " )");
                        resultText.setVisibility(View.VISIBLE);
                        searchTeachersAdapter = new TeacherAdapter(requireContext(), teachers);
                        teacherListView.setAdapter(searchTeachersAdapter);
                        teacherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String teacher_code = searchTeachersAdapter.getTeacherCodeAtPosition(i);
                                Intent intent = new Intent(requireActivity(), TeacherDetailsActivity.class);
                                intent.putExtra("teacherCode", teacher_code);
                                startActivity(intent);
                            }
                        });
                        teacherListView.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        String errorBodyString = response.errorBody().string();
                        JSONObject errorObject = new JSONObject(errorBodyString);
                        String errorMessage = errorObject.getString("message");
                        resultText.setText(errorMessage);
                        resultText.setVisibility(View.VISIBLE);
                        teacherListView.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                        Toast.makeText(requireActivity(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchTeacherResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load user profile", t);
                Toast.makeText(requireActivity(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}