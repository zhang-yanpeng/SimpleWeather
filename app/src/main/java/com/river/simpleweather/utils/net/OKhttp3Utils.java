package com.river.simpleweather.utils.net;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ZhangYanPeng on 2019/8/24.
 */
public class OKhttp3Utils {

    private OkHttpClient httpClient;
    private static OKhttp3Utils oKhttp3Utils;
    private Context context;

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    private OKhttp3Utils(Context c) {
        context = c;
        httpClient = new OkHttpClient.Builder()
//                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public static OKhttp3Utils getInstance(Context c) {

        if (oKhttp3Utils == null) {
            oKhttp3Utils = new OKhttp3Utils(c);
        }

        return oKhttp3Utils;
    }

    /**
     * okhttp  get 请求
     *
     * @param url      url地址
     * @param params   参数（map）
     * @param callback 结果回调
     */
    public void get(String url, HashMap<String, String> params, final OKCallback callback) {

        if (!params.isEmpty()) {
//          拼接字符串
            StringBuffer buffer = new StringBuffer(url);
            buffer.append('?');
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buffer.append(entry.getKey());
                buffer.append('=');
                buffer.append(entry.getValue());
                buffer.append('&');
            }
            buffer.deleteCharAt(buffer.length() - 1);
            url = buffer.toString();
        }

        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET", null);
        Request request = builder.build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });

    }

    /**
     * okhttp  get 请求
     *
     * @param url      url地址
     * @param callback 结果回调
     */
    public void get(String url,final OKCallback callback) {

        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET", null);
        Request request = builder.build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });

    }

    /**
     * post 请求
     *
     * @param url
     * @param params
     * @param callback
     */
    public void post(String url, HashMap<String, String> params, final OKCallback callback) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (!params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBody.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody form = formBody.build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.post(form)
                .url(url)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });
    }

    /**
     * 上传图片
     *
     * @param url
     * @param
     * @param file
     * @param callback
     */
    public void postImage(String url, HashMap<String, String> params, File file, final OKCallback callback) {

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();


        if (!params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        multipartBodyBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MEDIA_TYPE_JPG, file));

        MultipartBody body = multipartBodyBuilder.build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.post(body)
                .url(url)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.success(response);
            }
        });

    }


    /**
     * 回调接口，用于请求成功以及请求失败
     */
    public interface OKCallback {
        void success(Response resp) throws IOException;

        void failed(IOException E);
    }


    public interface OnProgressListener {
        void progress(long totalSize, long currentSize, boolean done, int id);
    }
}
