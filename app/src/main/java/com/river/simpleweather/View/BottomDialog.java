package com.river.simpleweather.View;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.river.simpleweather.R;


/**
 * @ClassName BottomDialog
 * @Description 底部弹出框，用于展示生活指数详情
 * @Author zhang.yanpeng
 * @Date 2019/8/27 13:16
 */
public class BottomDialog extends DialogFragment implements View.OnClickListener {
    View view;
    TextView title;
    TextView content;
    TextView leftBtn;
    TextView rightBtn;

    OnDialogBtnClickListener clickListener;

    public void setClickListener(OnDialogBtnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.BOTTOM);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_layout_bottom, container, false);
        title = view.findViewById(R.id.dialog_title);
        content = view.findViewById(R.id.dialog_content);
        leftBtn = view.findViewById(R.id.dialog_left_btn);
        rightBtn = view.findViewById(R.id.dialog_right_btn);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String titleText = arguments.getString("title");
            String contentText = arguments.getString("content");

            title.setText(titleText);
            content.setText(contentText);
        }

        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_left_btn:
                if (clickListener!=null)
                clickListener.leftClick();
                break;
            case R.id.dialog_right_btn:
                if (clickListener!=null)
                clickListener.rightClick();
                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//      关闭时需要销毁所有数据

    }

    public interface OnDialogBtnClickListener {
        void leftClick();

        void rightClick();
    }

}
