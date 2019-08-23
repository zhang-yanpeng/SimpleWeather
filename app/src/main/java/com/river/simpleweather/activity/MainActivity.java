package com.river.simpleweather.activity;

import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.heweather.plugin.view.HeWeatherConfig;
import com.heweather.plugin.view.HorizonView;
import com.heweather.plugin.view.VerticalView;
import com.river.simpleweather.Contants;
import com.river.simpleweather.R;
import com.river.simpleweather.base.BaseActivity;
import com.river.simpleweather.utils.LogUtil;
import com.river.simpleweather.utils.PermissionUtils;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class MainActivity extends BaseActivity {

    //  高德地图SDK相关
    AMapLocationClient locationClient;
    AMapLocationClientOption clientOption;

    //  和风天气插件
    HorizonView horizonView;
    VerticalView vertical_weather;

    //  下来刷新
    SmartRefreshLayout smartRefresh;

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
        initView();

        boolean b = PermissionUtils.checkPermission(mContext, PermissionUtils.PERMISSION_LOCAL[0]);
        if (b) {//启动定位
            initLocation();
        } else {
            PermissionUtils.requestPermission(this, PermissionUtils.PERMISSION_LOCAL[0], 11);
        }

        initWeather();
    }

    /**
     * 初始化天气数据
     * key e6f6ee33e2164e8ba2049749aae8d7c6
     */
    private void initWeather() {
//      初始化
        HeWeatherConfig.init(Contants.HW_KEY, "");

    }

    /**
     * 初始化定位
     */
    private void initLocation() {
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
                    LogUtil.i("定位结果：" + city);
                    locationClient.stopLocation();
                } else {
                    int errorCode = aMapLocation.getErrorCode();
                    String errorInfo = aMapLocation.getErrorInfo();
//                  定位失败，需要给出提示
                    LogUtil.i("定位失败：" + errorCode + "," + errorInfo);
                }
            }
        });
//      高精度定位
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//      单次定为
        clientOption.setOnceLocation(true);

        locationClient.setLocationOption(clientOption);
//      开启定位
        locationClient.startLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults[0] == 0) {
            initWeather();
        } else {//给出弹窗 提示需要获取定位权限并进入到权限设置页面

        }
    }

    private void initView() {
        smartRefresh = findViewById(R.id.smartRefresh);

        PhoenixHeader header = new PhoenixHeader(this);
        int blue = getResources().getColor(R.color.colorBlue);
        header.setBackgroundColor(blue);
        smartRefresh.setRefreshHeader(header);

        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
            }
        });

        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//      释放定位资源
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
    }
}
