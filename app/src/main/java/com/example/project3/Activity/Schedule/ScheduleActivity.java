package com.example.project3.Activity.Schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.project3.Activity.Fragment.ScheduleFragment.Student.ClassInSemesterFragment;
import com.example.project3.Activity.Fragment.ScheduleFragment.Student.StudentMonthScheduleFragment;
import com.example.project3.Activity.Student.StudentMainActivity;
import com.example.project3.Adapter.ViewPagerFragmentAdapter;
import com.example.project3.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private Toolbar scheduleToolbar;
    private TabLayout tabScheduleLayout;
    private ViewPager2 viewSchedulePager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        scheduleToolbar = findViewById(R.id.schedule_toolbar);
        tabScheduleLayout = findViewById(R.id.tabScheduleLayout);
        viewSchedulePager = findViewById(R.id.viewSchedulePage);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new StudentMonthScheduleFragment());
        fragmentList.add(new ClassInSemesterFragment());

        scheduleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleActivity.this, StudentMainActivity.class);
                startActivity(intent);
                ScheduleActivity.this.finish();
            }
        });

        viewSchedulePager.setAdapter(new ViewPagerFragmentAdapter(this, fragmentList));
        new TabLayoutMediator(tabScheduleLayout, viewSchedulePager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Thời khóa biểu");
                            break;
                        case 1:
                            tab.setText("Danh sách lớp");
                            break;
                    }
                }
        ).attach();
    }
}