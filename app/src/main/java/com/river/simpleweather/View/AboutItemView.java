package com.river.simpleweather.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.river.simpleweather.R;

/**
 * @ClassName AboutItemView
 * @Description 关于 item
 * @Author zhang.yanpeng
 * @Date 2019/8/27 15:13
 */
public class AboutItemView extends ConstraintLayout {
    Context mCon;
    View view;
    TextView item_name;
    TextView item_value;

    public AboutItemView(Context context) {
        this(context,null);
    }

    public AboutItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AboutItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCon = context;
//        view = LayoutInflater.from(context).inflate(R.layout.item_about, this, false);
        view = View.inflate(context,R.layout.item_about,this);
        item_name = view.findViewById(R.id.item_name);
        item_value = view.findViewById(R.id.item_value);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mCon.obtainStyledAttributes(attrs, R.styleable.AboutItemView);
        item_name.setText(typedArray.getString(R.styleable.AboutItemView_nameText));
        item_value.setText(typedArray.getString(R.styleable.AboutItemView_valueText));
        typedArray.recycle();
    }

}
