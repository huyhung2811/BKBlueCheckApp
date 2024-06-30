package com.example.project3.Activity.Fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project3.Activity.Schedule.ScheduleActivity;
import com.example.project3.Activity.Schedule.TeacherScheduleActivity;
import com.example.project3.Activity.Student.AttendanceActivity;
import com.example.project3.Activity.Student.StudentClassActivity;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.ProfileStudentResponse;
import com.example.project3.Response.ProfileTeacherResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherHomeFragment extends Fragment {
    private TextView name;
    private ImageView avatar;
    private LinearLayout layoutCalendar, layoutAttendance, layoutStudentClass, layoutSearch;
    private ImageView calendar;
    private ApiInterface apiInterface;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_home, container, false);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        name = view.findViewById(R.id.textName);
        avatar = view.findViewById(R.id.imageAvatar);
        layoutStudentClass = view.findViewById(R.id.layoutStudentClass);
        layoutCalendar = view.findViewById(R.id.layoutCalendar);
        layoutAttendance = view.findViewById(R.id.layoutAttendance);

        loadUserProfile(token);

        layoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), TeacherScheduleActivity.class);
                startActivity(intent);
            }
        });

//        layoutAttendance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(requireActivity(), AttendanceActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        layoutStudentClass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(requireActivity(), StudentClassActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    private void loadUserProfile(String authToken) {
        Call<ProfileTeacherResponse> call = apiInterface.getTeacherProfile("Bearer " + authToken);
        call.enqueue(new Callback<ProfileTeacherResponse>() {
            @Override
            public void onResponse(Call<ProfileTeacherResponse> call, Response<ProfileTeacherResponse> response) {
                if (response.isSuccessful()) {
                    ProfileTeacherResponse teacherProfileResponse = response.body();
                    String userName = teacherProfileResponse.getName();
                    String userAvatar = teacherProfileResponse.getAvatar();
                    name.setText(userName);
                    Picasso.get().load(userAvatar).into(avatar);
                } else {
                    try {
                        Log.d("a", String.valueOf(response));
                        String errorMessage = response.errorBody().string();
                        Log.e("API Error", "Error message: " + errorMessage);
                        Toast.makeText(requireActivity(), "Failed to load user profile: " + errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Failed to parse error message", e);
                        Toast.makeText(requireActivity(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileTeacherResponse> call, Throwable t) {
                // Handle the error when the API call fails
                Log.e("API Error", "Failed to load user profile", t);
                Toast.makeText(requireActivity(), "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}