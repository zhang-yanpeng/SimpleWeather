package com.river.simpleweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.river.simpleweather.R;

import com.river.simpleweather.bean.LifeStyle;
import com.river.simpleweather.bean.LifeStyle.HeWeather6Bean.LifestyleBean;

import java.util.ArrayList;

/**
 * Created by ZhangYanPeng on 2019/8/24.
 */
public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.ForecastVH> {

    private Context mCon;
    private ArrayList<LifestyleBean> forecastBeans;
    private LayoutInflater inflater;

    public LifeAdapter(Context mCon, ArrayList<LifestyleBean> forecastBeans) {
        this.mCon = mCon;
        this.forecastBeans = forecastBeans;
        this.inflater = LayoutInflater.from(mCon);
    }

    @NonNull
    @Override
    public ForecastVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_life, parent, false);
        return new ForecastVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastVH holder, int position) {
        LifestyleBean forecast = forecastBeans.get(position);
        if (forecast != null) {
            holder.life_item.setText(forecast.getType());
            holder.life_status.setText(forecast.getBrf());
            holder.life_suggestion.setText(forecast.getTxt());
        }
    }

    @Override
    public int getItemCount() {
        return forecastBeans == null ? 0 : forecastBeans.size();
    }

    class ForecastVH extends RecyclerView.ViewHolder {
        TextView life_item;
        TextView life_status;
        TextView life_suggestion;

        public ForecastVH(@NonNull View itemView) {
            super(itemView);
            life_item = itemView.findViewById(R.id.life_item);
            life_status = itemView.findViewById(R.id.life_status);
            life_suggestion = itemView.findViewById(R.id.life_suggestion);
        }
    }
}
