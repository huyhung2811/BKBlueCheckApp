package com.example.project3.Activity.Fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project3.Activity.LoginActivity;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.LogoutResponse;
import com.example.project3.Response.ProfileStudentResponse;
import com.example.project3.Response.ProfileTeacherResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherProfileFragment extends Fragment {
    private ApiInterface apiInterface;
    private TextView name, email, teacher_code, teacher_unit;
    private ImageView avatar;
    private Toolbar teacherDetailsToolbar;
    private Button btnLogout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        teacherDetailsToolbar = view.findViewById(R.id.teacher_profile_toolbar);
        name = view.findViewById(R.id.teacher_name);
        email = view.findViewById(R.id.teacher_email);
        avatar = view.findViewById(R.id.teacher_avatar);
        teacher_code = view.findViewById(R.id.teacher_code);
        teacher_unit = view.findViewById(R.id.teacher_unit);
        btnLogout = view.findViewById(R.id.btnLogout);

        loadUserProfile(token);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(token);
            }
        });

        return view;
    }

    private void loadUserProfile(String authToken) {
        Call<ProfileTeacherResponse> call = apiInterface.getTeacherProfile("Bearer " + authToken);
        call.enqueue(new Callback<ProfileTeacherResponse>() {
            @Override
            public void onResponse(Call<ProfileTeacherResponse> call, Response<ProfileTeacherResponse> response) {
                if (response.isSuccessful()) {
                    ProfileTeacherResponse teacherProfileResponse = response.body();
                    String data_name = teacherProfileResponse.getName();
                    String data_email = teacherProfileResponse.getEmail();
                    String data_teacher_code = String.valueOf(teacherProfileResponse.getTeacher_code());
                    String data_teacher_unit = teacherProfileResponse.getUnit();
                    String data_teacher_avatar = teacherProfileResponse.getAvatar();
                    name.setText(data_name);
                    email.setText(data_email);
                    teacher_code.setText(data_teacher_code);
                    teacher_unit.setText(data_teacher_unit);
                    Picasso.get().load(data_teacher_avatar).into(avatar);

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

    private void logout(String token) {
        Call<LogoutResponse> call = apiInterface.logout("Bearer" + token);
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    LogoutResponse logoutResponse = response.body();
                    clearSharedPreferences();
                    Log.d("AA",logoutResponse.getMessage());
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else {
                    Toast.makeText(requireContext(), "Phản hồi không hợp lệ từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearSharedPreferences() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}