package com.example.project3.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.project3.Model.DayOffRequest;
import com.example.project3.R;
import java.util.List;

public class RequestAdapter extends ArrayAdapter<DayOffRequest> {
    private Context context;
    private List<DayOffRequest> dayOffRequests;

    public RequestAdapter(@NonNull Context context, List<DayOffRequest> dayOffRequests) {
        super(context, R.layout.list_request_item, dayOffRequests);
        this.context = context;
        this.dayOffRequests = dayOffRequests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_request_item, null);
        }

        DayOffRequest dayOffRequest = dayOffRequests.get(position);

        if (dayOffRequest!= null) {
            TextView txtClassName = view.findViewById(R.id.txtClassName);
            TextView txtReason = view.findViewById(R.id.txtReason);
            TextView txtStatus = view.findViewById(R.id.txtRequestStatus);

            txtClassName.setText(dayOffRequest.getClass_code() + " - " + dayOffRequest.getClass_name());
            txtReason.setText(dayOffRequest.getReason());
            txtStatus.setText(dayOffRequest.getStatus());
            switch (dayOffRequest.getStatus()) {
                case "Duyệt":
                    txtStatus.setBackgroundColor(Color.parseColor("#66FF99"));
                    break;
                case "Từ chối":
                    txtStatus.setBackgroundColor(Color.parseColor("#FF9999"));
                    break;
                case "Chờ duyệt":
                    txtStatus.setBackgroundColor(Color.parseColor("#FFFF99"));
                    break;
                default:
                    txtStatus.setTextColor(Color.parseColor("#000000"));
                    break;
            }
        }
        return view;
    }

    public int getRequestIdAtPosition(int position) {
        return dayOffRequests.get(position).getId();
    }
}
