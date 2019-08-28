package com.river.simpleweather.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.river.simpleweather.R;

/**
 * @ClassName DoubleTextView
 * @Description 自定义View
 * @Author zhang.yanpeng
 * @Date 2019/8/28 10:06
 */
public class DoubleTextView extends LinearLayout {

    Context mCon;
    View view;
    TextView topText;
    TextView bottomText;

    public DoubleTextView(Context context) {
        this(context, null);
    }

    public DoubleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCon = context;
        view = View.inflate(context, R.layout.view_double_text, this);
        topText = view.findViewById(R.id.tv_top);
        bottomText = view.findViewById(R.id.tv_bottom);

        initStyle(attrs);
    }

    private void initStyle(AttributeSet attrs) {
        TypedArray typedArray = mCon.obtainStyledAttributes(attrs, R.styleable.DoubleTextView);

        topText.setText(typedArray.getString(R.styleable.DoubleTextView_topText));
        bottomText.setText(typedArray.getString(R.styleable.DoubleTextView_bottomText));

        typedArray.recycle();
    }


    public void setTopText(String topText) {
        this.topText.setText(topText);
    }

    public void setBottomText(String bottomText) {
        this.bottomText.setText(bottomText);
    }
}
