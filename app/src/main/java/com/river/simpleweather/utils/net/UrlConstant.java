package com.river.simpleweather.utils.net;

/**
 * Created by ZhangYanPeng on 2019/8/24.
 */
public class UrlConstant {

    /**
     * get
     * 获取bing背景图片
     */
    public static final String URL_IMAGE = "http://guolin.tech/api/bing_pic";

    /**
     * 获取实时天气
     * https://free-api.heweather.net/s6/weather/{weather-type}?{parameters}
     * 后面加相关参数
     */
    public static final String URL_WEATHER_NOW = "https://free-api.heweather.net/s6/weather/now";

    /**
     * 获取未来3-10天天气情况
     */
    public static final String URL_WEATHER_FEATURE = "https://free-api.heweather.net/s6/weather/forecast";

    /**
     * hourly 逐个小时天气
     */
    public static final String URL_WEATHER_HOURLY = "https://free-api.heweather.net/s6/weather/hourly";

    /**
     * 生活指数
     */
    public static final String URL_WEATHER_LIFE = "https://free-api.heweather.net/s6/weather/lifestyle";
}
