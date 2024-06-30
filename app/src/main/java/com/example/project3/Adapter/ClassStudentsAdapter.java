package com.example.project3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project3.Model.Student;
import com.example.project3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClassStudentsAdapter extends ArrayAdapter<Student> {
    private Context context;
    private List<Student> studentList;

    public ClassStudentsAdapter(Context context, List<Student> studentList) {
        super(context, R.layout.list_student_item, studentList);
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_student_item, null);
        }

        Student student = studentList.get(position);

        if (student != null) {
            ImageView avatar = view.findViewById(R.id.listImage);
            TextView name = view.findViewById(R.id.listName);
            TextView student_code = view.findViewById(R.id.listStudentCode);

            name.setText(student.getName());
            student_code.setText(student.getStudent_code());
            Picasso.get().load(student.getAvatar()).into(avatar);
        }

        return view;
    }

    public String getStudentCodeAtPosition(int position) {
        return studentList.get(position).getStudent_code();
    }
}
