package com.river.simpleweather.base;

import android.app.Application;
import android.content.Context;

import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * Created by ZhangYanPeng on 2018/10/27.
 */
public class SWApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    public Context getContext() {
        return context;
    }
}
