package com.sk.xscrollviewdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.google.android.material.tabs.TabLayout;
import com.sk.xscrollviewdemo.adapter.BaseFragmentPagerAdapter;
import com.sk.xscrollviewdemo.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sk.xscrollviewdemo.AnimatorUtils.BOTTOM_TO_VISIBLE;
import static com.sk.xscrollviewdemo.AnimatorUtils.TOP_TO_GONE;
import static com.sk.xscrollviewdemo.AnimatorUtils.TOP_TO_VISIBLE;


public class MainActivity extends AppCompatActivity implements View.OnLayoutChangeListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.title_bar_layout)
    View titleBarLayout;
    @BindView(R.id.to_bottom_img)
    AppCompatImageView toBottomImg;


    private MapFragment mapFragment;

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
        mFragments.add(mapFragment = new MapFragment());
        mFragments.add(new MapFragment());
        mFragments.add(new MapFragment());
        mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), mFragments);

        viewPager.setAdapter(mAdapter);
        viewPager.setNoScroll(false);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setText("专车");
        tabLayout.getTabAt(1).setText("顺风车");
        tabLayout.getTabAt(2).setText("出租车");

        titleBarLayout.addOnLayoutChangeListener(this);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
    }


    @OnClick(R.id.to_bottom_img)
    public void toBottom() {
        mapFragment.xScrollView.scrollToBottom();
    }


    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        mapFragment.xScrollView.setTitleBarHeight(titleBarLayout.getHeight());
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
