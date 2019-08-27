package com.river.simpleweather.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
//        ImmersionBar.with(this)
//                .statusBarDarkFont(true)
//                .init();
        Window window = getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }



        setContent();



    }

    protected void setContent() {

    }

    @Override
    public void onClick(View v) {

    }
}
