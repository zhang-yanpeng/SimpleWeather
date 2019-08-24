package com.river.simpleweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.river.simpleweather.R;
import com.river.simpleweather.bean.ForecastWeather.HeWeather6Bean.DailyForecastBean;

import java.util.ArrayList;

/**
 * Created by ZhangYanPeng on 2019/8/24.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastVH> {

    private Context mCon;
    private ArrayList<DailyForecastBean> forecastBeans;
    private LayoutInflater inflater;

    public ForecastAdapter(Context mCon, ArrayList<DailyForecastBean> forecastBeans) {
        this.mCon = mCon;
        this.forecastBeans = forecastBeans;
        this.inflater = LayoutInflater.from(mCon);
    }

    @NonNull
    @Override
    public ForecastVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_forecast, parent, false);
        return new ForecastVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastVH holder, int position) {
        DailyForecastBean forecast = forecastBeans.get(position);
        if (forecast != null) {
            holder.forcast_data.setText(forecast.getDate());
            holder.forcast_weather.setText(forecast.getCond_txt_d());

            holder.forcast_temp_max.setText(forecast.getTmp_max());
            holder.forcast_temp_min.setText(forecast.getTmp_min());

        }
    }

    @Override
    public int getItemCount() {
        return forecastBeans == null ? 0 : forecastBeans.size();
    }

    class ForecastVH extends RecyclerView.ViewHolder {
        TextView forcast_data;
        TextView forcast_weather;
        TextView forcast_temp_max;
        TextView forcast_temp_min;

        public ForecastVH(@NonNull View itemView) {
            super(itemView);
            forcast_data = itemView.findViewById(R.id.forcast_data);
            forcast_weather = itemView.findViewById(R.id.forcast_weather);
            forcast_temp_max = itemView.findViewById(R.id.forcast_temp_max);
            forcast_temp_min = itemView.findViewById(R.id.forcast_temp_min);
        }
    }
}
