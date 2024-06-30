package com.example.project3.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project3.Model.CourseClass;
import com.example.project3.Model.Student;
import com.example.project3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CourseClassAdapter extends ArrayAdapter<CourseClass> {

    private Context context;
    private List<CourseClass> courseClassList;
    private String name;

    public CourseClassAdapter(@NonNull Context context, List<CourseClass> courseClassList, String name) {
        super(context, R.layout.list_course_class_item, courseClassList);
        this.context = context;
        this.courseClassList = courseClassList;
        this.name = name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_course_class_item, parent, false);
        }
        CourseClass courseClass = courseClassList.get(position);

        if (courseClass != null) {
            TextView txtCourseClass = view.findViewById(R.id.course_class_info);
            txtCourseClass.setText( courseClass.getClass_code()+ " - " + name + " - " + courseClass.getCourse_code());
        }

        return view;
    }

    public int getClassCodeAtPosition(int position) {
        return courseClassList.get(position).getClass_code();
    }
}
