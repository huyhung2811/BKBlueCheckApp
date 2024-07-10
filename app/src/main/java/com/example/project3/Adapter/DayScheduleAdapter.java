package com.example.project3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project3.Model.Schedule;
import com.example.project3.Model.Student;
import com.example.project3.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DayScheduleAdapter extends ArrayAdapter<Schedule> {
    private Context context;
    private List<Schedule> scheduleClassList;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat outputFormat = new SimpleDateFormat("H:mm");

    public DayScheduleAdapter(@NonNull Context context, List<Schedule> scheduleClassList) {
        super(context, R.layout.list_schedule_item, scheduleClassList);
        this.context = context;
        this.scheduleClassList = scheduleClassList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_schedule_item, null);
        }

        Schedule scheduleClass = scheduleClassList.get(position);

        if (scheduleClass!= null) {
            TextView startTime = view.findViewById(R.id.start_time);
            TextView endTime = view.findViewById(R.id.end_time);
            TextView title = view.findViewById(R.id.title);
            TextView txtTime = view.findViewById(R.id.txtTime);
            TextView txtWeek = view.findViewById(R.id.txtWeek);

//            startTime.setText(scheduleClass.getStartTime());
            try {
                Date startDate = inputFormat.parse(scheduleClass.getStartTime());
                startTime.setText(outputFormat.format(startDate));
            } catch (ParseException e) {
                startTime.setText(scheduleClass.getStartTime());
            }

            try {
                Date endDate = inputFormat.parse(scheduleClass.getEndTime());
                endTime.setText(outputFormat.format(endDate));
            } catch (ParseException e) {
                endTime.setText(scheduleClass.getEndTime());
            }
            title.setText(scheduleClass.getCourseCode() + " - " + scheduleClass.getName() + " - " + scheduleClass.getClassCode());
            txtTime.setText(scheduleClass.getSchoolDay() + ", " +scheduleClass.getRoom());
//            txtWeek.setText("Tuần 1");
        }

        return view;
    }

    public int getClassCodeAtPosition(int position) {
        return scheduleClassList.get(position).getClassCode();
    }
}

