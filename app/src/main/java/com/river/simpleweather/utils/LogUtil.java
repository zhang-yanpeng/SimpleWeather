package com.river.simpleweather.utils;

import android.util.Log;

/**
 * Created by ZhangYanPeng on 2018/10/27.
 */
public class LogUtil {
    private static boolean isDebug = true;


    public static void i(String TAG,String msg){
        if (isDebug){
            Log.i(TAG,msg);
        }
    }

    public static void e(String TAG,String msg){
        if (isDebug){
            Log.e(TAG,msg);
        }
    }

    public static void w(String TAG,String msg){
        if (isDebug){
            Log.w(TAG,msg);
        }
    }

    public static void a(String TAG,String msg){
        if (isDebug){
            Log.d(TAG,msg);
        }
    }

    public static void v(String TAG,String msg){
        if (isDebug){
            Log.v(TAG,msg);
        }
    }


}
