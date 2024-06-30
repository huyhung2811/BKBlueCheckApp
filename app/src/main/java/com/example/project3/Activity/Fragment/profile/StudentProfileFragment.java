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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project3.Activity.LoginActivity;
import com.example.project3.Activity.Student.StudentMainActivity;
import com.example.project3.Body.LoginBody;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;
import com.example.project3.Response.LoginResponse;
import com.example.project3.Response.LogoutResponse;
import com.example.project3.Response.ProfileStudentResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileFragment extends Fragment {
    private TextView name, email,student_code, phone_number, student_unit, student_system, student_class;
    private ImageView avatar;
    private Button btnLogout;
    private ApiInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        name = view.findViewById(R.id.student_name);
        avatar = view.findViewById(R.id.student_avatar);
        email = view.findViewById(R.id.student_email);
        student_code = view.findViewById(R.id.student_code);
        phone_number = view.findViewById(R.id.phone_number);
        student_unit = view.findViewById(R.id.student_unit);
        student_system = view.findViewById(R.id.student_system);
        student_class = view.findViewById(R.id.student_class);
        btnLogout = view.findViewById(R.id.btnLogout);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);
        SharedPreferences prefs = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
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
        Call<ProfileStudentResponse> call = apiInterface.getStudentProfile("Bearer " + authToken);
        call.enqueue(new Callback<ProfileStudentResponse>() {
            @Override
            public void onResponse(Call<ProfileStudentResponse> call, Response<ProfileStudentResponse> response) {
                if (response.isSuccessful()) {
                    ProfileStudentResponse studentProfileResponse = response.body();
                    String data_name = studentProfileResponse.getName();
                    String data_email = studentProfileResponse.getEmail();
                    String data_phone_number = studentProfileResponse.getPhone();
                    String data_student_code = String.valueOf(studentProfileResponse.getStudentCode());
                    String data_student_unit = studentProfileResponse.getUnit();
                    String data_student_system = studentProfileResponse.getSystem();
                    String data_student_class = studentProfileResponse.getStudentClass();
                    String data_student_avatar = studentProfileResponse.getAvatar();
                    name.setText(data_name);
                    email.setText(data_email);
                    phone_number.setText(data_phone_number);
                    student_code.setText(data_student_code);
                    student_unit.setText(data_student_unit);
                    student_system.setText(data_student_system);
                    student_class.setText(data_student_class);
                    Picasso.get().load(data_student_avatar).into(avatar);

                } else {
                    try {
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
            public void onFailure(Call<ProfileStudentResponse> call, Throwable t) {
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