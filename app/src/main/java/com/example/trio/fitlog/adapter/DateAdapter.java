package com.example.trio.fitlog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trio.fitlog.HomeFragment;
import com.example.trio.fitlog.R;

import java.util.Calendar;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    int selected_date;
    Context context;
    List<Integer> dates;
    HomeFragment hm;

    public DateAdapter(Context context, List<Integer> dates, int selected_date, HomeFragment hm) {
        this.context = context;
        this.dates = dates;
        this.selected_date = selected_date;
        this.hm = hm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.date_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.date.setText(String.valueOf(dates.get(i)));

        viewHolder.date.setTextColor(context.getResources().getColor(R.color.black6));
        if(dates.get(i)==this.selected_date) {
            viewHolder.date.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void setDates(List<Integer> dates){
        this.dates = dates;
    }

    public void setItemPosition(int date){
        this.selected_date = date;
    }

    public int getItemPosition(int date){
        return dates.indexOf(date);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selected_date = dates.get(getAdapterPosition());
                    hm.dateChanged(selected_date);
                    setItemPosition(selected_date);
                }
            });
        }
    }
}

