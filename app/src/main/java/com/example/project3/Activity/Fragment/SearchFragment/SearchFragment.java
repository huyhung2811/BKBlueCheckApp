package com.example.project3.Activity.Fragment.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project3.Activity.Fragment.SearchFragment.FragmentSearch.SearchCourseClassFragment;
import com.example.project3.Activity.Fragment.SearchFragment.FragmentSearch.SearchCourseFragment;
import com.example.project3.Activity.Fragment.SearchFragment.FragmentSearch.SearchStudentFragment;
import com.example.project3.Activity.Fragment.SearchFragment.FragmentSearch.SearchTeacherFragment;
import com.example.project3.Activity.Student.StudentClassActivity;
import com.example.project3.Activity.Student.StudentMainActivity;
import com.example.project3.Adapter.ViewPagerFragmentAdapter;
import com.example.project3.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        tabLayout = view.findViewById(R.id.tabSearchLayout);
        viewPager = view.findViewById(R.id.viewSearchPage);

        // Tạo danh sách các fragment (trang) cho mỗi tab
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SearchStudentFragment());
        fragmentList.add(new SearchTeacherFragment());
        fragmentList.add(new SearchCourseFragment());
        fragmentList.add(new SearchCourseClassFragment());

        viewPager.setAdapter(new ViewPagerFragmentAdapter(requireActivity(), fragmentList));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Sinh viên");
                            break;
                        case 1:
                            tab.setText("Giáo viên");
                            break;
                        case 2:
                            tab.setText("Học phần");
                            break;
                        case 3:
                            tab.setText("Lớp học phần");
                            break;
                    }
                }
        ).attach();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}