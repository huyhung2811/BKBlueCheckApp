package com.example.project3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project3.Activity.Student.StudentMainActivity;
import com.example.project3.Activity.Teacher.TeacherMainActivity;
import com.example.project3.Body.LoginBody;
import com.example.project3.Response.LoginResponse;
import com.example.project3.Interface.ApiInterface;
import com.example.project3.Network.RetrofitClientInstance;
import com.example.project3.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button btnLogin;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login);

        apiInterface = RetrofitClientInstance.getInstance().create(ApiInterface.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (userEmail.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                performLogin(userEmail, userPassword);
            }
        });
    }

    private void performLogin(String email, String password) {
        Call<LoginResponse> call = apiInterface.login(new LoginBody(email, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        String token = loginResponse.getToken();
                        String role = loginResponse.getRole();
                        SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                        editor.putString("token", token);
                        editor.putString("role", role);
                        editor.apply();

                        if(role.equals("student")){
                            Intent intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                            startActivity(intent);
                        }
                        if(role.equals("teacher")){
                            Intent intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Phản hồi không hợp lệ từ máy chủ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}