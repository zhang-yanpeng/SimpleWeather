package com.river.simpleweather.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gyf.immersionbar.ImmersionBar;
import com.river.simpleweather.BuildConfig;
import com.river.simpleweather.R;
import com.river.simpleweather.base.BaseActivity;
import com.river.simpleweather.utils.PermissionUtils;
import com.river.simpleweather.utils.SharedPrefenceUtils;

public class SplashActivity extends BaseActivity {

    private Button btn_jump;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (btn_jump != null) {
                        toMain();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        creatNotificationChanel();

        boolean allow = (boolean) SharedPrefenceUtils.getParam(mContext, "notification", false);
        if (allow) {
//          生成通知

        }

        btn_jump = findViewById(R.id.btn_jump);
        btn_jump.setOnClickListener(this);

//      判断权限
        boolean b = PermissionUtils.checkPermissionsGroup(mContext, PermissionUtils.PERMISSION_LOCAL);
        if (!b) {
//          请求权限
            PermissionUtils.requestPermissions(this, PermissionUtils.PERMISSION_LOCAL, 10);
        } else {
            btn_jump.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(1, 3 * 1000);
        }
    }

    @Override
    protected void setContent() {
        super.setContent();

        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_jump:
                toMain();
                break;
        }
    }

    private void toMain() {
        mHandler.removeCallbacksAndMessages(null);
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 生成通知渠道
     */
    private void creatNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0之后  创建通知渠道
            String channelId = BuildConfig.APPLICATION_ID;
            String channelName = BuildConfig.VERSION_NAME;
            int importanceHigh = NotificationManager.IMPORTANCE_HIGH;
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importanceHigh);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (requestCode == 10) {
//          授权成功 可以开始执行跳转
                btn_jump.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessageDelayed(1, 3 * 1000);
            }
        }
    }

}
