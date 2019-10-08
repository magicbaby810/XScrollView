package com.sk.dididemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
import com.sk.commons.base.BaseActivity;
import com.sk.commons.base.BaseFragmentsAdapter;
import com.sk.commons.utils.RxBus;
import com.sk.commons.utils.RxManager;
import com.sk.commons.view.NoScrollViewPager;
import com.sk.dididemo.event.RefreshTitleBarEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements View.OnLayoutChangeListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.title_bar_layout)
    View titleBarLayout;
    @BindView(R.id.to_bottom_img)
    AppCompatTextView toBottomImg;


    private MapFragment mapFragment;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private BaseFragmentsAdapter mAdapter;

    private RxManager mRxManager;
    private Disposable refreshTitleBarDisposable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int setStatusBarColor() {
        return R.color.colorPrimary;
    }

    @Override
    protected void initView() {


        mFragments.add(mapFragment = new MapFragment());
        mFragments.add(new MapFragment());
        mFragments.add(new MapFragment());
        mAdapter = new BaseFragmentsAdapter(getSupportFragmentManager(), mFragments);

        viewPager.setAdapter(mAdapter);
        //禁止viewpager滑动
        viewPager.setNoScroll(false);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setText("专车");
        tabLayout.getTabAt(1).setText("顺风车");
        tabLayout.getTabAt(2).setText("出租车");

        titleBarLayout.addOnLayoutChangeListener(this);

        mRxManager = new RxManager();
        bindRxbusEvent();
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.to_bottom_img)
    public void toBottom() {
        mapFragment.chooseLocationWidget.scrollToBottom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxManager.clear();
    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        mapFragment.chooseLocationWidget.setTitleBarHeight(titleBarLayout.getHeight());
    }

    // 注册RXBUS
    public void bindRxbusEvent() {

        refreshTitleBarDisposable = RxBus.getDefault().register(RefreshTitleBarEvent.class, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                RefreshTitleBarEvent event = (RefreshTitleBarEvent) o;
                if (event.isHideTitleBar()) {
                    titleBarLayout.setVisibility(View.GONE);
                    toBottomImg.setVisibility(View.VISIBLE);
                } else {
                    titleBarLayout.setVisibility(View.VISIBLE);
                    toBottomImg.setVisibility(View.GONE);
                }
            }
        });

        mRxManager.add(refreshTitleBarDisposable);
    }
}
