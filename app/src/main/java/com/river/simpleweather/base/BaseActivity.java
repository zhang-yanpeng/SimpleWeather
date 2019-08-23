package com.river.simpleweather.base;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

/**
 * @ClassName BaseActivity
 * @Description
 * @Author zhang.yanpeng
 * @Date 2019/8/23 11:29
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }

    @Override
    public void onClick(View v) {

    }
}
