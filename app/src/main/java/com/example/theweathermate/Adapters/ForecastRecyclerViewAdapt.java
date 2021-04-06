package com.example.theweathermate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theweathermate.ModelClasses.DailyWeather;
import com.example.theweathermate.R;
import com.example.theweathermate.Utils.HelperMethods;

import java.util.List;

public class ForecastRecyclerViewAdapt extends RecyclerView.Adapter<ForecastRecyclerViewAdapt.mViewHolder> {

    Context context;
    List<DailyWeather> dailyWeather;

    public ForecastRecyclerViewAdapt(Context context, List<DailyWeather> dailyWeather) {
        this.context = context;
        this.dailyWeather = dailyWeather;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.daily_forecast_recycler_view, parent, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.date.setText(HelperMethods.epochToDate(dailyWeather.get(position).getDt(), HelperMethods.DATE_FORMAT));
        int minTemp = HelperMethods.KelvinToCelsius(dailyWeather.get(position).getTemp().getMin());
        int maxTemp = HelperMethods.KelvinToCelsius(dailyWeather.get(position).getTemp().getMax());
        holder.minMaxTemp.setText(context.getString(R.string.MIN_MAX_TEMP, maxTemp, minTemp));

        HelperMethods.setIcon(context,holder.imgRes, dailyWeather.get(position).getWeather().get(0).getIcon());
    }

    @Override
    public int getItemCount() {
        return dailyWeather.size();
    }

    public static class mViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        ImageView imgRes;
        TextView minMaxTemp;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.textView4);
            imgRes = itemView.findViewById(R.id.imageView2);
            minMaxTemp = itemView.findViewById(R.id.textView5);
        }
    }
}
