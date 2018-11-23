package com.example.trio.triathlog.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trio.triathlog.DetailActivity;
import com.example.trio.triathlog.R;
import com.example.trio.triathlog.database.TriathlogDbHelper;
import com.example.trio.triathlog.model.Activity;
import com.example.trio.triathlog.model.Type;
import com.example.trio.triathlog.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    Context context;
    List<Activity> activities;

    public ActivityAdapter(Context context, List<Activity> activities) {
        this.context = context;
        this.activities = activities;
    }

    public void setItems(List<Activity> activities){
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder viewHolder, int i) {
        Activity activity = activities.get(i);
        Type type = TriathlogDbHelper.getInstance(context).getType(activity.getType_id());

        String[] datetime = activity.getDatetime().split(" ");
        String prevDate = (i==0)?"": activities.get(i - 1).getDatetime().split(" ")[0];

        if(!datetime[0].equals(prevDate)){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String pattern = "E, dd MMM";
            Calendar myCalendar = Calendar.getInstance();
            Date date = new Date();
            try {
                date = df.parse(activity.getDatetime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            myCalendar.setTime(date);
            if(myCalendar.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR))
                pattern+=" yyyy";

            viewHolder.date.setText(Util.calendarToStringFriendly(myCalendar, pattern));
            viewHolder.date.setVisibility(View.VISIBLE);
            viewHolder.line.setVisibility(View.VISIBLE);
            viewHolder.minutes_total.setVisibility(View.VISIBLE);
            viewHolder.distance_total.setVisibility(View.VISIBLE);

            Calendar calendar = Util.stringToCalendar(activity.getDatetime(), "yyyy-MM-dd HH:mm");

            ArrayMap<String, Integer> summary = TriathlogDbHelper.getInstance(context).getActivitySummary(Util.calendarToString(calendar, "yyyy-MM-dd"));
            viewHolder.minutes_total.setText(String.valueOf(summary.get("sum_minute")) + " min");
            viewHolder.distance_total.setText(String.valueOf(summary.get("sum_distance")) + " km");
        } else {
            viewHolder.date.setVisibility(View.GONE);
            viewHolder.line.setVisibility(View.GONE);
            viewHolder.minutes_total.setVisibility(View.GONE);
            viewHolder.distance_total.setVisibility(View.GONE);
        }

        //viewHolder.icon.setImageResource(type.getIcon());
        Glide.with(context).load(type.getIcon()).into(viewHolder.icon);
        viewHolder.time.setText(datetime[1]);
        viewHolder.title.setText(activity.getTitle());
        viewHolder.duration.setText(activity.getHour() +"hr " + activity.getMinute() + "min");
        viewHolder.distance.setText(String.valueOf(activity.getDistance()) + " km");

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View line;
        ImageView icon;
        TextView date;
        TextView time;
        TextView title;
        TextView duration;
        TextView distance;
        TextView minutes_total;
        TextView distance_total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.line);
            icon = itemView.findViewById(R.id.icon);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            distance = itemView.findViewById(R.id.distance);
            minutes_total = itemView.findViewById(R.id.minutes_total);
            distance_total = itemView.findViewById(R.id.distance_total);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position  =   getAdapterPosition();
            Activity activity = activities.get(position);

            Intent detail_activity = new Intent(view.getContext(), DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Activity", activity);
            detail_activity.putExtras(bundle);
            view.getContext().startActivity(detail_activity);
        }
    }
}
