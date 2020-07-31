package com.sk.xscrollviewdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.google.android.material.tabs.TabLayout;
import com.sk.xscrollview.XScrollView;
import com.sk.xscrollviewdemo.adapter.BaseFragmentPagerAdapter;
import com.sk.xscrollviewdemo.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sk.xscrollviewdemo.AnimatorUtils.BOTTOM_TO_VISIBLE;
import static com.sk.xscrollviewdemo.AnimatorUtils.TOP_TO_GONE;
import static com.sk.xscrollviewdemo.AnimatorUtils.TOP_TO_VISIBLE;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.title_bar_layout)
    View titleBarLayout;
    @BindView(R.id.to_bottom_img)
    AppCompatImageView toBottomImg;


    private MapFragment mapFragment1, mapFragment2, mapFragment3;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private BaseFragmentPagerAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        mFragments.add(mapFragment1 = new MapFragment());
        mFragments.add(mapFragment2 = new MapFragment());
        mFragments.add(mapFragment3 = new MapFragment());
        mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), mFragments);

        viewPager.setAdapter(mAdapter);
        viewPager.setNoScroll(false);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setText("TAB1");
        tabLayout.getTabAt(1).setText("TAB2");
        tabLayout.getTabAt(2).setText("TAB3");

        mapFragment1.setTitleBar(titleBarLayout);
        mapFragment2.setTitleBar(titleBarLayout);
        mapFragment3.setTitleBar(titleBarLayout);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
    }


    @OnClick(R.id.to_bottom_img)
    public void toBottom() {
        mapFragment1.xScrollView.scrollToBottom();
    }

    boolean isVisible = true;
    public void animTitleBar(boolean touchMoon) {
        if (touchMoon) {

            if (isVisible) {
                isVisible = false;

                AnimatorUtils.translationYAnim(titleBarLayout, new AnticipateOvershootInterpolator(), 1000, false, TOP_TO_GONE);
                toBottomImg.setVisibility(View.VISIBLE);
            }

        } else {

            if (!isVisible) {
                isVisible = true;

                AnimatorUtils.translationYAnim(titleBarLayout, new AnticipateOvershootInterpolator(), 1000, true, TOP_TO_VISIBLE);
                toBottomImg.setVisibility(View.GONE);
            }

        }
    }
}
