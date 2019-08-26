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
     * 必应首页地址，
     * 为了获取首页每日一图
     */
    public static final String URL_BING_XML = "https://cn.bing.com/?toHttps=1&redig=0332DC95D8084E269D62CEDFFDD7CA66";
    public static final String URL_BING = "https://cn.bing.com";
    public static final String URL_BING_IMG_DAY = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN";

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
     * hourly 逐个小时天气(免费版的不支持)
     */
    public static final String URL_WEATHER_HOURLY = "https://free-api.heweather.net/s6/weather/hourly";

    /**
     * 生活指数
     */
    public static final String URL_WEATHER_LIFE = "https://free-api.heweather.net/s6/weather/lifestyle";
}
