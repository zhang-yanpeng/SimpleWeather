package com.river.simpleweather.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.gyf.immersionbar.ImmersionBar;

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
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContent();

    }

    protected void setContent() {

    }

    @Override
    public void onClick(View v) {

    }
}
