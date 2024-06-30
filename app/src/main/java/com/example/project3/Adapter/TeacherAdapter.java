package com.example.project3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project3.Model.Teacher;
import com.example.project3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TeacherAdapter extends ArrayAdapter<Teacher> {
    private Context context;
    private List<Teacher> teacherList;

    public TeacherAdapter(Context context, List<Teacher> teacherList) {
        super(context, R.layout.list_teacher_item, teacherList);
        this.context = context;
        this.teacherList = teacherList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_teacher_item, null);
        }

        Teacher teacher = teacherList.get(position);

        if (teacher != null) {
            ImageView avatar = view.findViewById(R.id.listImage);
            TextView name = view.findViewById(R.id.listName);
            TextView teacher_code = view.findViewById(R.id.listTeacherCode);

            name.setText(teacher.getName());
            teacher_code.setText(teacher.getTeacher_code());
            Picasso.get().load(teacher.getAvatar()).into(avatar);
        }

        return view;
    }

    public String getTeacherCodeAtPosition(int position) {
        return teacherList.get(position).getTeacher_code();
    }
}
