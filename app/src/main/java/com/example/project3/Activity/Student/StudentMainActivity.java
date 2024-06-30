package com.example.project3.Activity.Student;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.project3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        BottomNavigationView navView = findViewById(R.id.nav_student_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_student_main);
        NavigationUI.setupWithNavController(navView, navController);
    }

}