package com.sk.dididemo.view;

import android.animation.Animator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.gyf.barlibrary.ImmersionBar;
import com.sk.commons.utils.AppUtils;
import com.sk.commons.utils.ObjectAnimatorHelper;
import com.sk.commons.utils.OnAnimListener;
import com.sk.commons.utils.RxBus;
import com.sk.commons.utils.SpaceItemDecoration;
import com.sk.dididemo.R;
import com.sk.dididemo.adapter.NewLandAdapter;
import com.sk.dididemo.bean.Activity;
import com.sk.dididemo.bean.Coupon;
import com.sk.dididemo.bean.Order;
import com.sk.dididemo.event.RefreshTitleBarEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sk
 * @date 2019-7-08
 */
public class ChooseLocationWidget extends CustomNestedScrollView implements NewLandAdapter.LayoutChangeListener,
       CustomNestedScrollView.ScrollChangeListener {


    /** 滚动到顶部临界点 */
    private int titleBarHeight;
    /** 固定toplayout到底部距离 */
    private int topLayoutMargin;




    private ChooseLocationItemWidget mSourceItem;
    private ChooseLocationItemWidget mDestItem;
    private TextView useCarNow, useCarPlan;
    private View bottomView, topLayout;


    private RecyclerView newLandRecyclerView;

    private NewLandAdapter newLandAdapter;




    public ChooseLocationWidget(Context context) {
        super(context);
        init();
    }

    public ChooseLocationWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScrollChangeListener(this);

        LayoutInflater.from(getContext()).inflate(R.layout.widget_choose_location, this);
        newLandRecyclerView = findViewById(R.id.new_land_view);
        topLayout = findViewById(R.id.top_layout);
        bottomView = findViewById(R.id.layout);
        mSourceItem = findViewById(R.id.choose_source_item_widget);
        mDestItem = findViewById(R.id.choose_dest_item_widget);
        useCarNow = findViewById(R.id.use_car_now);
        useCarPlan = findViewById(R.id.use_car_plan);
        useCarNow.setSelected(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        newLandRecyclerView.setLayoutManager(linearLayoutManager);
        newLandRecyclerView.setHasFixedSize(true);
        newLandRecyclerView.setNestedScrollingEnabled(false);
        newLandRecyclerView.setItemAnimator(new DefaultItemAnimator());
        newLandRecyclerView.addItemDecoration(new SpaceItemDecoration(20, 20));

        newLandAdapter = new NewLandAdapter();
        newLandAdapter.setLayoutChangeListener(this);
        newLandRecyclerView.setAdapter(newLandAdapter);


        setTopHeight(topLayout.getTop());
    }


    public void setCouponText(Coupon coupon) {

        ArrayList<Coupon> coupons = new ArrayList<>();
        coupons.add(coupon);
        coupons.add(coupon);
        coupons.add(coupon);
        coupons.add(coupon);
        coupons.add(coupon);
        coupons.add(coupon);
        newLandAdapter.setCouponData(coupons);
    }

    public void setActivityText(Activity activity) {

        ArrayList<Activity> activities = new ArrayList<>();
        activities.add(activity);

        newLandAdapter.setActivityData(activities);
    }

    public void setOrderText(Order order) {

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);
        newLandAdapter.setOrderData(orders);
    }


    public void setTitleBarHeight(int height) {
        titleBarHeight = height;
    }

    @Override
    public void changeLayout(int value) {

        DisplayMetrics displayMetrics = AppUtils.getDpi(getContext());

        Log.e("changeLayout", topLayout.getHeight() + " " + bottomView.getHeight());
        topLayoutMargin = displayMetrics.heightPixels - (topLayout.getHeight() + bottomView.getHeight() + (int) (value * 0.8) + ImmersionBar.getNavigationBarHeight((android.app.Activity) getContext()));

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin = topLayoutMargin;
        topLayout.setLayoutParams(ll);
    }

    boolean isVisible = true;
    @Override
    public void scrollChange(int scrollY) {

        // 动态设置topLayout的距离顶部的高度
        setTopHeight(topLayout.getTop() - scrollY);



        // 计算滚动到接近titlebar时，隐藏toplayout布局
        // 顶部高度topMargin：屏幕高度减去titlebar高度和状态栏高度
        // 滚动高度scrollHeight：scrollY是从toplayout的高度开始计算的，所以scrollY加上屏幕高度减去页面初始化后toplayout的高度
        DisplayMetrics displayMetrics = AppUtils.getDpi(getContext());

        int topMargin = displayMetrics.heightPixels - titleBarHeight - ImmersionBar.getStatusBarHeight((android.app.Activity) getContext());
        int scrollHeight = scrollY + (displayMetrics.heightPixels - topLayoutMargin);

        if (scrollHeight >= topMargin) {

            if (topLayout.getVisibility() == VISIBLE && isVisible) {
                isVisible = false;
                ObjectAnimatorHelper.alphaAnim(topLayout, new LinearInterpolator(), 100, false);
                ObjectAnimatorHelper.animEnd(new OnAnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        topLayout.setVisibility(INVISIBLE);
                        isVisible = true;
                    }
                });
            }

        } else {
            if (topLayout.getVisibility() == INVISIBLE) {
                topLayout.setVisibility(VISIBLE);
                ObjectAnimatorHelper.alphaAnim(topLayout, new LinearInterpolator(), 100, true);
            }
        }

        RxBus.getDefault().post(new RefreshTitleBarEvent(scrollHeight >= topMargin));
    }

    public static class ChooseLocationItemWidget extends RelativeLayout {
        public ChooseLocationItemWidget(Context context) {
            super(context);
            init();
        }

        public ChooseLocationItemWidget(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public ChooseLocationItemWidget(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        /**
         * 默认开始地点
         */
        public static final int SOURCE_TYPE = 0;
        /**
         * 下车地点
         */
        public static final int DEST_TYPE = 1;
        /**
         * 移动中
         */
        public static final int START_MOVE = 2;

        private ImageView mTypeIV;
        private TextView mInputET;

        private String location;

        private void init() {
            LayoutInflater.from(getContext()).inflate(R.layout.widget_choose_item_location, this);

            mTypeIV = (ImageView) findViewById(R.id.type_icon);
            mInputET = (TextView) findViewById(R.id.input_location_et);

        }

    }


}
