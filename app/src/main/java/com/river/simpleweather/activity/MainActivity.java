package com.river.simpleweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.river.simpleweather.Contants;
import com.river.simpleweather.R;
import com.river.simpleweather.View.BottomDialog;
import com.river.simpleweather.View.DoubleTextView;
import com.river.simpleweather.View.OvalImageView;
import com.river.simpleweather.adapter.ForecastAdapter;
import com.river.simpleweather.adapter.LifeAdapter;
import com.river.simpleweather.adapter.OnItemClickListener;
import com.river.simpleweather.base.BaseActivity;
import com.river.simpleweather.bean.ForecastWeather;
import com.river.simpleweather.bean.ForecastWeather.HeWeather6Bean.DailyForecastBean;
import com.river.simpleweather.bean.LifeStyle;
import com.river.simpleweather.bean.LifeStyle.HeWeather6Bean.LifestyleBean;
import com.river.simpleweather.bean.NowWeather;
import com.river.simpleweather.bean.WeatherBean;
import com.river.simpleweather.utils.DateTimeUtils;
import com.river.simpleweather.utils.GsonUtils;
import com.river.simpleweather.utils.LogUtil;
import com.river.simpleweather.utils.PermissionUtils;
import com.river.simpleweather.utils.net.OKhttp3Utils;
import com.river.simpleweather.utils.net.UrlConstant;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

public class MainActivity extends BaseActivity {

    //  高德地图SDK相关
    AMapLocationClient locationClient;
    AMapLocationClientOption clientOption;

    //下拉刷新
    SmartRefreshLayout smartRefresh;

    DrawerLayout main_rootview;
    LinearLayout left_drawer;
    ImageView app_icon;
    ImageView image_bing;
    String image_url;
    TextView tv_city_name;
    TextView tv_refresh_time;
    TextView tv_temperature;
    TextView tv_weather_type;

    //天气相关信息
    DoubleTextView weather_wind;
    DoubleTextView weather_wet;
    DoubleTextView weather_water;
    DoubleTextView weather_press;

    RecyclerView recycle_forecast;
    ForecastAdapter forecastAdapter;
    ArrayList<DailyForecastBean> forecastBeans;
    RecyclerView.LayoutManager forecastLm;

    RecyclerView recycle_life;
    LifeAdapter lifeAdapter;
    ArrayList<LifestyleBean> lifestyleBeans;
    RecyclerView.LayoutManager lifeLm;

    //左侧菜单栏
    TextView tv_setting;
    TextView tv_city;
    TextView tv_about;

    boolean getNow = false;
    boolean getFuture = false;
    boolean getLife = false;

    String location_city;//当前城市
    //实况天气：
    NowWeather.HeWeather6Bean heNowWeather;
    //未来天气
    ForecastWeather.HeWeather6Bean heFutureWeather;
    //生活指数
    List<LifeStyle.HeWeather6Bean> heLife;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (image_bing != null) {
                        Glide.with(mContext)
                                .load(image_url)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(image_bing);
                    }

                    break;
                case 1://实况天气
                    if (heNowWeather != null) {
                        NowWeather.HeWeather6Bean.NowBean now = heNowWeather.getNow();
                        NowWeather.HeWeather6Bean.BasicBean basic = heNowWeather.getBasic();
                        tv_city_name.setText(basic.getLocation());
                        tv_refresh_time.setText(heNowWeather.getUpdate().getLoc());
                        tv_temperature.setText(now.getTmp() + "°C");
                        tv_weather_type.setText(now.getCond_txt());

                        weather_press.setTopText(now.getPres());
                        weather_water.setTopText(now.getPcpn());
                        weather_wind.setTopText(now.getWind_dir());
                        weather_wet.setTopText(now.getHum());
                    }

                    break;

                case 2:
                    if (forecastAdapter != null) {
                        forecastAdapter.notifyDataSetChanged();
                    }
                    break;

                case 3:
                    if (lifeAdapter != null) {//生活指数
                        lifeAdapter.notifyDataSetChanged();
                    }

                    break;
                case 10://用来关闭页面刷新
                    if (getNow && getFuture && getLife) {
                        smartRefresh.finishRefresh();
                        getNow = false;
                        getLife = false;
                        getFuture = false;

                        isLoading = false;
                    }

                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPermission();

    }

    private void initPermission(){
        boolean b = PermissionUtils.checkPermission(mContext, PermissionUtils.PERMISSION_LOCAL[0]);
        if (b) {//启动定位
            initLocation();
        } else {
            PermissionUtils.requestPermission(this, PermissionUtils.PERMISSION_LOCAL[0], 11);
        }
    }

    /**
     * 未来3-5天天气
     */
    private void initFutureWeather() {
        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(location_city)) {
            location_city = "beijing";
        }
        params.put("location", location_city);
        params.put("key", Contants.HW_API_KEY);

        OKhttp3Utils.getInstance(mContext).get(UrlConstant.URL_WEATHER_FEATURE, params, new OKhttp3Utils.OKCallback() {
            @Override
            public void success(Response resp) throws IOException {
                String string = resp.body().string();
                LogUtil.i("未来天气：" + string);
                ForecastWeather forecastWeather = GsonUtils.GsonToBean(string, ForecastWeather.class);
                heFutureWeather = forecastWeather.getHeWeather6().get(0);
                forecastBeans.clear();
                forecastBeans.addAll(heFutureWeather.getDaily_forecast());
                getFuture = true;
                mHandler.sendEmptyMessage(10);
                mHandler.sendEmptyMessage(2);

            }

            @Override
            public void failed(IOException E) {

            }
        });
    }

    /**
     * 获取生活指数
     */
    private void initWeatherLife() {
        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(location_city)) {
            location_city = "beijing";
        }
        params.put("location", location_city);
        params.put("key", Contants.HW_API_KEY);

        OKhttp3Utils.getInstance(mContext).get(UrlConstant.URL_WEATHER_LIFE, params, new OKhttp3Utils.OKCallback() {
            @Override
            public void success(Response resp) throws IOException {
                String string = resp.body().string();
                LogUtil.i("生活指数：" + string);
                getLife = true;
                mHandler.sendEmptyMessage(10);

                LifeStyle lifeStyle = GsonUtils.GsonToBean(string, LifeStyle.class);

                lifestyleBeans.clear();
                lifestyleBeans.addAll(lifeStyle.getHeWeather6().get(0).getLifestyle());

                mHandler.sendEmptyMessage(3);
            }

            @Override
            public void failed(IOException E) {

            }
        });

    }

    /**
     * 获取实况天气
     */
    private void initNowWeather() {

        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(location_city)) {
            location_city = "beijing";
        }

        params.put("location", location_city);
        params.put("key", Contants.HW_API_KEY);

        OKhttp3Utils.getInstance(mContext).get(UrlConstant.URL_WEATHER_NOW, params, new OKhttp3Utils.OKCallback() {
            @Override
            public void success(Response resp) throws IOException {
                String string = resp.body().string();
                LogUtil.i("now天气：" + string);
                NowWeather nowWeather = GsonUtils.GsonToBean(string, NowWeather.class);
                heNowWeather = nowWeather.getHeWeather6().get(0);
                if (heNowWeather.getStatus().equals("ok")) {
                    mHandler.sendEmptyMessage(1);
                    getNow = true;
                    mHandler.sendEmptyMessage(10);
                }
            }

            @Override
            public void failed(IOException E) {

            }
        });

    }

    @Override
    protected void setContent() {
        super.setContent();
        setContentView(R.layout.activity_main);
        initView();
        getBingImage();
    }

    /**
     * 初始化天气数据
     */
    boolean isLoading = false;

    private void initWeather() {
        isLoading = true;
        initNowWeather();
        initFutureWeather();
        initWeatherLife();
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
//                  获取定位结果成功 开始获取天气
                    location_city = city;
//                  获取天气
                    initWeather();

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
            final BottomDialog dialog = new BottomDialog();
            Bundle bundle = new Bundle();
            bundle.putString("title", "权限说明");
            bundle.putString("content", "简单天气需要申请定位权限来获取详细的天气信息，请您手动赋予权限");
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "setting");

            dialog.setClickListener(new BottomDialog.OnDialogBtnClickListener() {
                @Override
                public void leftClick() {
                    location_city = "beijing";
                    dialog.dismiss();
                    initWeather();
                }

                @Override
                public void rightClick() {
//                  进入系统设置页面
                    dialog.dismiss();
                    toSelfSetting(mContext);
                }
            });
        }
    }


    private void initView() {
        main_rootview = findViewById(R.id.main_rootview);

        app_icon = findViewById(R.id.app_icon);
        smartRefresh = findViewById(R.id.smartRefresh);
        image_bing = findViewById(R.id.image_bing);
        tv_city_name = findViewById(R.id.tv_city_name);
        tv_refresh_time = findViewById(R.id.tv_refresh_time);
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_weather_type = findViewById(R.id.tv_weather_type);
        recycle_forecast = findViewById(R.id.recycle_forecast);
        recycle_life = findViewById(R.id.recycle_life);

        weather_wind = findViewById(R.id.weather_wind);
        weather_water = findViewById(R.id.weather_water);
        weather_press = findViewById(R.id.weather_press);
        weather_wet = findViewById(R.id.weather_wet);

//      天气预报
        forecastBeans = new ArrayList<>();
        forecastAdapter = new ForecastAdapter(mContext, forecastBeans);
        recycle_forecast.setAdapter(forecastAdapter);
        forecastLm = new LinearLayoutManager(mContext);
        ((LinearLayoutManager) forecastLm).setOrientation(RecyclerView.VERTICAL);
        recycle_forecast.setLayoutManager(forecastLm);

//      生活指数
        lifestyleBeans = new ArrayList<>();
        lifeAdapter = new LifeAdapter(mContext, lifestyleBeans);
        recycle_life.setAdapter(lifeAdapter);
        lifeLm = new GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false);
        recycle_life.setLayoutManager(lifeLm);

        lifeAdapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                showLifeDetail(position);
            }
        });

        PhoenixHeader header = new PhoenixHeader(this);
        int blue = getResources().getColor(R.color.colorBlue);
        header.setBackgroundColor(blue);
        smartRefresh.setRefreshHeader(header);

        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//              避免多次刷新 正在刷新过程中时，不允许多次进行请求
                if (!isLoading) {
                    initWeather();
                }

            }
        });

        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
            }
        });

        app_icon.setOnClickListener(this);

        tv_setting = findViewById(R.id.tv_setting);
        tv_about = findViewById(R.id.tv_about);
        tv_city = findViewById(R.id.tv_city);

        tv_setting.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        tv_city.setOnClickListener(this);

    }

    /**
     * 展示 生活指数 详情
     */
    String title;

    private void showLifeDetail(int position) {
        LifestyleBean life = lifestyleBeans.get(position);
        if (life == null) {
            return;
        }

        final BottomDialog bottomDialog = new BottomDialog();
        Bundle bundle = new Bundle();
        String type = life.getType();

        if (type.equals("comf")) {
            title = "舒适度指数";
        }

        if (type.equals("dsrg")) {
            title = "穿衣指数";
        }

        if (type.equals("flu")) {
            title = "感冒指数";
        }

        if (type.equals("sport")) {
            title = "运动指数";
        }

        if (type.equals("trav")) {
            title = "旅游指数";
        }

        if (type.equals("uv")) {
            title = "防晒指数";
        }

        if (type.equals("air")) {
            title = "空气指数";
        }

        bundle.putString("title", title);
        bundle.putString("content", life.getTxt());
        bottomDialog.setArguments(bundle);

        bottomDialog.show(getSupportFragmentManager(), "showLife");

        bottomDialog.setClickListener(new BottomDialog.OnDialogBtnClickListener() {
            @Override
            public void leftClick() {
                bottomDialog.dismiss();

            }

            @Override
            public void rightClick() {
                bottomDialog.dismiss();
            }
        });

    }

    /**
     * 为了处理退出应用的操作
     */
    boolean isSecond;
    long oldTime;
    long newTime;

    @Override
    public void onBackPressed() {
        if (!isSecond) {//第一次
            oldTime = DateTimeUtils.getCurrentTime();
            isSecond = true;
        } else {//第二次进入
            newTime = DateTimeUtils.getCurrentTime();
        }

        final BottomDialog bottomDialog = new BottomDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", "");
        bundle.putString("content", "确定要退出简单天气吗？");
        bottomDialog.setArguments(bundle);

        bottomDialog.show(getSupportFragmentManager(), "onBack");

        bottomDialog.setClickListener(new BottomDialog.OnDialogBtnClickListener() {
            @Override
            public void leftClick() {
                bottomDialog.dismiss();
            }

            @Override
            public void rightClick() {
                bottomDialog.dismiss();
                MainActivity.this.finish();
            }
        });

    }

    /**
     * 获取必应搜索每日图片
     */
    private void getBingImage() {
        OKhttp3Utils.getInstance(mContext).get(UrlConstant.URL_BING_IMG_DAY, new OKhttp3Utils.OKCallback() {

            @Override
            public void success(Response resp) throws IOException {
                String string = resp.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JSONArray images = jsonObject.getJSONArray("images");
                    JSONObject o = (JSONObject) images.get(0);
                    String url = o.getString("url");
                    image_url = UrlConstant.URL_BING + url;
                    LogUtil.i("每日一图：" + image_url);
                    mHandler.sendEmptyMessage(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failed(IOException E) {

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

    public void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
        startActivityForResult(mIntent,100);
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.app_icon:
                main_rootview.openDrawer(Gravity.LEFT);
                break;
            case R.id.tv_city:
                break;
            case R.id.tv_about:
                it = new Intent(this, AboutActivity.class);
                startActivity(it);
                break;
            case R.id.tv_setting:
                it = new Intent(this,SettingActivity.class);
                startActivity(it);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//      重新检查系统权限
        if (resultCode== Activity.RESULT_OK&&requestCode==100){
            initPermission();
        }

    }
}
