package com.river.simpleweather.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.river.simpleweather.BuildConfig;
import com.river.simpleweather.R;
import com.river.simpleweather.base.BaseActivity;
import com.river.simpleweather.utils.LogUtil;
import com.river.simpleweather.utils.SharedPrefenceUtils;

public class SettingActivity extends BaseActivity {

    ImageView backImg;
    TextView toolbar_text;
    Switch switch_notification;

    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar_text = findViewById(R.id.tooltar_text);
        backImg = findViewById(R.id.toolbar_back);
        switch_notification = findViewById(R.id.switch_notification);

        backImg.setOnClickListener(this);
        toolbar_text.setText("设置");

        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtil.i("是否创建通知：" + isChecked);
                if (isChecked) {//打开通知栏 同时存储在本地sp
                    creatNotification();
                } else {//关闭通知栏
                    if (notificationManager!=null){
                        notificationManager.cancelAll();
                    }
                }
                saveStatus(isChecked);
            }
        });
    }

    private void saveStatus(boolean b){
        SharedPrefenceUtils.setParam(mContext,"notification",b);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    private void creatNotification() {
        String channelId = BuildConfig.APPLICATION_ID;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.simple)
                .setContentTitle("title")
                .setContentText("content")
                .build();
        notificationManager.notify(1, notification);
    }

}
