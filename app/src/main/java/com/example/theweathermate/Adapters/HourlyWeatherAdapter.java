package com.example.theweathermate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theweathermate.ModelClasses.CurrentWeather;
import com.example.theweathermate.R;
import com.example.theweathermate.Utils.Constants;
import com.example.theweathermate.Utils.HelperMethods;

import java.util.List;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RECYCLER_VIEW";
    private static final int TEMPERATURE = 2;
    private static final int SUNSET_SUNRISE = 1;

    Context context;
    List<CurrentWeather> hourlyList;

    public HourlyWeatherAdapter(Context context, List<CurrentWeather> hourlyList) {
        this.context = context;
        this.hourlyList = hourlyList;
    }

    @Override
    public int getItemViewType(int position) {

        if (hourlyList.get(position).getWeather().get(0).getMain().equals(Constants.SUNRISE)
                || hourlyList.get(position).getWeather().get(0).getMain().equals(Constants.SUNSET)) {
            return SUNSET_SUNRISE;
        } else
            return TEMPERATURE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType != SUNSET_SUNRISE) {
            View view = inflater.inflate(R.layout.hourly_layout, parent, false);
            return new mViewHolderClass(view);
        } else {
            View view = inflater.inflate(R.layout.hourly_layout_2, parent, false);
            return new mViewHolderClass2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == TEMPERATURE)
            ((mViewHolderClass) (holder)).setWidgets((mViewHolderClass) holder, position);
        else
            ((mViewHolderClass2) (holder)).setData((mViewHolderClass2) holder, position);
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    public class mViewHolderClass extends RecyclerView.ViewHolder {

        TextView time;
        TextView temp;
        ImageView imgRes;

        public mViewHolderClass(@NonNull View itemView) {
            super(itemView);

            imgRes = itemView.findViewById(R.id.imageView3);
            time = itemView.findViewById(R.id.textView7);
            temp = itemView.findViewById(R.id.textView6);
        }

        private void setWidgets(mViewHolderClass holder, int position) {

            String time = HelperMethods.epochToDate(hourlyList.get(position).getDt(), HelperMethods.TIME_FORMAT);
            if (time.equals(context.getString(R.string.NEXT_DAY)) || time.equals(context.getString(R.string.NEXT_DAY_2))) {
                holder.time.setText(HelperMethods.epochToDate(hourlyList.get(position).getDt(), HelperMethods.DAY_FORMAT));
            } else
                holder.time.setText(HelperMethods.epochToDate(hourlyList.get(position).getDt(), HelperMethods.TIME_FORMAT));

            holder.temp.setText(context.getString(R.string.HOURLY_TEMP,
                    HelperMethods.KelvinToCelsius(hourlyList.get(position).getTemp())));

            HelperMethods.setIcon(context, holder.imgRes, hourlyList.get(position).getWeather().get(0).getIcon());
        }
    }

    public class mViewHolderClass2 extends RecyclerView.ViewHolder {

        TextView time;
        TextView temp;
        ImageView imgRes;

        public mViewHolderClass2(@NonNull View itemView) {
            super(itemView);

            imgRes = itemView.findViewById(R.id.imageView7);
            time = itemView.findViewById(R.id.textView11);
            temp = itemView.findViewById(R.id.textView10);
        }

        public void setData(mViewHolderClass2 holder, int position) {

            if(hourlyList.get(position).getWeather().get(0).getMain().equals(Constants.SUNSET)) {
                holder.time.setText(HelperMethods.epochToDate(hourlyList.get(position).getDt(), HelperMethods.MIN_SEC_FORMAT));
                holder.imgRes.setImageResource(R.drawable.ic_sunset);
                holder.temp.setText(Constants.SUNSET);
            }else{
                holder.time.setText(HelperMethods.epochToDate(hourlyList.get(position).getDt(), HelperMethods.MIN_SEC_FORMAT));
                holder.imgRes.setImageResource(R.drawable.ic_sunrise);
                holder.temp.setText(Constants.SUNRISE);
            }
        }
    }
}
