package com.river.simpleweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

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
    public void onBindViewHolder(@NonNull ForecastVH holder, final int position) {
        LifestyleBean forecast = forecastBeans.get(position);
        if (forecast != null) {

            String type = forecast.getType();
            if (type.equals("comf")) {
                Glide.with(mCon).load(R.mipmap.comfort).into(holder.life_img);
            }

            if (type.equals("cw")) {
                Glide.with(mCon).load(R.mipmap.car).into(holder.life_img);
            }

            if (type.equals("drsg")) {
                Glide.with(mCon).load(R.mipmap.colthes).into(holder.life_img);
            }

            if (type.equals("flu")) {
                Glide.with(mCon).load(R.mipmap.ill).into(holder.life_img);
            }

            if (type.equals("sport")) {
                Glide.with(mCon).load(R.mipmap.sprit).into(holder.life_img);
            }

            if (type.equals("trav")) {
                Glide.with(mCon).load(R.mipmap.travel).into(holder.life_img);
            }

            if (type.equals("uv")) {
                Glide.with(mCon).load(R.mipmap.radiation).into(holder.life_img);
            }

            if (type.equals("air")) {
                Glide.with(mCon).load(R.mipmap.air).into(holder.life_img);
            }

            if (type.equals("ac")) {
                Glide.with(mCon).load(R.mipmap.kongtiao).into(holder.life_img);
            }

            if (type.equals("ag")) {
                Glide.with(mCon).load(R.mipmap.air).into(holder.life_img);

            }

            if (type.equals("gl")) {
                Glide.with(mCon).load(R.mipmap.sunglass).into(holder.life_img);
            }

            if (type.equals("mu")) {
                Glide.with(mCon).load(R.mipmap.makeup).into(holder.life_img);
            }

            if (type.equals("airc")) {
                Glide.with(mCon).load(R.mipmap.drycloth).into(holder.life_img);
            }

            if (type.equals("ptfc")) {
                Glide.with(mCon).load(R.mipmap.traffic).into(holder.life_img);
            }

            if (type.equals("fsh")) {
                Glide.with(mCon).load(R.mipmap.fish).into(holder.life_img);
            }

            if (type.equals("spi")) {
                Glide.with(mCon).load(R.mipmap.sunscreen).into(holder.life_img);
            }

            holder.life_status.setText(forecast.getBrf());

//          点击监听
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return forecastBeans == null ? 0 : forecastBeans.size();
    }

    class ForecastVH extends RecyclerView.ViewHolder {

        TextView life_status;
        ImageView life_img;

        public ForecastVH(@NonNull View itemView) {
            super(itemView);

            life_status = itemView.findViewById(R.id.life_status);
            life_img = itemView.findViewById(R.id.life_img);

        }
    }
}
