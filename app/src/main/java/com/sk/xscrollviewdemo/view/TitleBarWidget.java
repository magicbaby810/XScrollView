package com.sk.xscrollviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sk.xscrollviewdemo.R;


/**
 * @author sk
 */
public class TitleBarWidget extends RelativeLayout {


    public TitleBarWidget(Context context) {
        super(context);
        init();
    }

    public TitleBarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBarWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ImageView mUserIconIV;
    private ImageView mMsgIV;

    private TextView mCityNameTV;
    private View mCityNameView;

    private TextView mTitleTV;

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_titlebar, this);

        mUserIconIV = findViewById(R.id.user_icon);
        mMsgIV = findViewById(R.id.msg_icon);
        mCityNameTV = findViewById(R.id.cityname_tv);
        mCityNameView = findViewById(R.id.cityname_ll);

        mTitleTV = findViewById(R.id.title_tv);

    }




}
