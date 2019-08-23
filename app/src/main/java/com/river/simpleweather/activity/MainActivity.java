package com.river.simpleweather.activity;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.river.simpleweather.R;
import com.river.simpleweather.base.BaseActivity;
import com.river.simpleweather.utils.LogUtil;
import com.river.simpleweather.utils.PermissionUtils;

public class MainActivity extends BaseActivity {

    //  高德地图SDK相关
    AMapLocationClient locationClient;
    AMapLocationClientOption clientOption;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean b = PermissionUtils.checkPermission(mContext, PermissionUtils.PERMISSION_LOCAL[0]);
        if (b) {//启动定位
            initLocation();
        } else {
            PermissionUtils.requestPermission(this, PermissionUtils.PERMISSION_LOCAL[0], 11);
        }

        initWeather();
        initView();
    }

    /**
     * 初始化天气数据
     */
    private void initWeather() {
        locationClient = new AMapLocationClient(mContext);
        clientOption = new AMapLocationClientOption();
//      设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
//              定位回调
                if (aMapLocation == null) {
                    return;
                }

                if (aMapLocation.getErrorCode() == 0) {
                    String city = aMapLocation.getCity();
                    LogUtil.i("定位结果："+city);
                } else {
                    int errorCode = aMapLocation.getErrorCode();
                    String errorInfo = aMapLocation.getErrorInfo();
//                  定位失败，需要给出提示
                    LogUtil.i("定位失败："+errorCode+","+errorInfo);
                }
            }
        });
//      高精度定位
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        locationClient.setLocationOption(clientOption);
//      开启定位
        locationClient.startLocation();


    }

    /**
     * 初始化定位
     */
    private void initLocation() {


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults[0] == 0) {
            initWeather();
        } else {//给出弹窗 提示需要获取定位权限

        }
    }

    private void initView() {

    }
}
