package com.sk.xscrollview;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.xscrollview.adapter.XScrollViewAdapter;
import com.sk.xscrollview.bean.XScrollViewBean;
import com.sk.xscrollview.utils.XScrollViewUtils;

import java.util.ArrayList;


/**
 * @author sk
 * @date 2019-7-08
 */
public class XScrollView extends XNestedScrollView implements XScrollViewAdapter.LayoutChangeListener,
       XNestedScrollView.ScrollChangeListener {


    /** 滚动到顶部临界点 */
    private int titleBarHeight;
    /** 固定toplayout到底部距离 */
    private int topLayoutMargin;
    /** 动态toplayout距离顶部的距离 */
    private int dynamicTopLayoutMargin;
    /** recyclerview show part 偏移系数 */
    private final static float ITEM_OFFSET_VALUE = 0.18f;
    /** recyclerview show part最大固定高度 */
    private int maxHeight = 900;
    /** 滚动的高度*/
    private int scrollY;
    /** 未完成行程高度 */
    private int unfinishedRouteHeight = 0;
    /** recyclerview item高度 */
    private int itemHeight = 0;

    private ChooseLocationItemWidget mSourceItem;
    private ChooseLocationItemWidget mDestItem;
    private TextView useCarNow, useCarPlan;
    private View bottomView, topLayout;

    private int routeLayoutResourceId, couponLayoutResourceId, activityLayoutResourceId;


    private RecyclerView newLandRecyclerView;

    private XScrollViewAdapter xScrollViewAdapter;

    private DisplayMetrics displayMetrics;

    private RecyclerView.ItemDecoration itemDecoration;


    public XScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XScrollView);
            hintTextSize = array.getDimension(R.styleable.DefaultPageView_hintTextSize, defaultValue);
            hintTextColor = array.getColor(R.styleable.DefaultPageView_hintTextColor, defaultValue);
            hintTextMarginTop = array.getDimension(R.styleable.DefaultPageView_hintTextMarginTop, 120);

            refreshTextSize = array.getDimension(R.styleable.DefaultPageView_refreshTextSize, defaultValue);
            refreshTextColor = array.getColor(R.styleable.DefaultPageView_refreshTextColor, defaultValue);

            routeLayoutResourceId = array.getLayoutDimension(R.styleable.XScrollView_routeLayout, -1);
            couponLayoutResourceId = array.getLayoutDimension(R.styleable.XScrollView_couponLayout, -1);
            activityLayoutResourceId = array.getLayoutDimension(R.styleable.XScrollView_activityLayout, -1);

            array.recycle();
        }





        setScrollChangeListener(this);

        LayoutInflater.from(getContext()).inflate(R.layout.adapter_scroll_view_item, this);
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
        newLandRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                changeLayout(v.getMeasuredHeight() >= 0 && v.getMeasuredHeight() < 1500 ? maxHeight : v.getMeasuredHeight());

                if (oldTop == 0) {
                    ObjectAnimatorHelper.translationYBottomAnim(XScrollView.this, new AnticipateOvershootInterpolator(), 1000, ObjectAnimatorHelper.BOTTOM_TO_VISIBLE, 360f);
                }
            }
        });

        xScrollViewAdapter = new XScrollViewAdapter();
        xScrollViewAdapter.setLayoutChangeListener(this);

        xScrollViewAdapter.setCouponLayout(couponLayoutResourceId);
        xScrollViewAdapter.setRouteLayout(routeLayoutResourceId);
        xScrollViewAdapter.setActivityLayout(activityLayoutResourceId);

        newLandRecyclerView.setAdapter(xScrollViewAdapter);

        displayMetrics = XScrollViewUtils.getDpi(getContext());
        // 适配低分辨率手机,以2340分辨率做参照
        if (displayMetrics.heightPixels < 2340) {
            maxHeight = displayMetrics.heightPixels * maxHeight / 2340;
        }
    }

    public XScrollView setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        newLandRecyclerView.addItemDecoration(itemDecoration);
        return this;
    }


    public void setCouponText(Coupon coupon) {

        ArrayList<Coupon> coupons = new ArrayList<>();
        coupons.add(coupon);
        coupons.add(coupon);
        coupons.add(coupon);
        coupons.add(coupon);
        coupons.add(coupon);
        coupons.add(coupon);
        xScrollViewAdapter.setCouponData(coupons);
    }

    public void setActivityText(Activity activity) {

        ArrayList<Activity> activities = new ArrayList<>();
        activities.add(activity);

        xScrollViewAdapter.setActivityData(activities);
    }

    public void setOrderText(Order order) {

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);
        xScrollViewAdapter.setOrderData(orders);
    }


    public void setTitleBarHeight(int height) {
        titleBarHeight = height;
    }

    @Override
    public void changeLayout(int value) {
        itemHeight = value;

        setTopLayoutTopMargin(itemHeight);

        // 初始化超过底布局，响应地图
        setTopHeight(dynamicTopLayoutMargin + topLayout.getMeasuredHeight());
    }

    private void setTopLayoutTopMargin(int itemHeight) {

        Log.e("setTopLayoutTopMargin", "topview：" + topLayout.getMeasuredHeight() + " bottomview：" + bottomView.getMeasuredHeight() + " 列表首次展示高度：" + (int) (itemHeight * ITEM_OFFSET_VALUE + unfinishedRouteHeight)
                + " 状态栏：" + XScrollViewUtils.getStatusBarHeight((android.app.Activity) getContext()) + " 导航栏：" + XScrollViewUtils.getNavigationHeight((android.app.Activity) getContext()) + " unfinishedRouteHeight " + unfinishedRouteHeight + " itemHeight " + itemHeight);

        int tempTopLayoutMargin = 0;
        if (topLayoutMargin != 0) {
            tempTopLayoutMargin = topLayoutMargin;
        }
        topLayoutMargin = displayMetrics.heightPixels - (topLayout.getMeasuredHeight() + bottomView.getMeasuredHeight() + XScrollViewUtils.getStatusBarHeight((android.app.Activity) getContext()) + XScrollViewUtils.getNavigationHeight((android.app.Activity) getContext()));

        dynamicTopLayoutMargin = topLayoutMargin - (int) (itemHeight * ITEM_OFFSET_VALUE + unfinishedRouteHeight);

        // 此处是防止底布局高度过高遮挡住地图中心的图钉，如果你有这样的需求请把注释清掉
//        if (dynamicTopLayoutMargin < displayMetrics.heightPixels / 2) {
//            dynamicTopLayoutMargin = displayMetrics.heightPixels / 2 - 100;
//        }

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin = dynamicTopLayoutMargin;
        topLayout.setLayoutParams(ll);

        int happyTogether = scrollY + (tempTopLayoutMargin - topLayoutMargin);
        // negative number is nothing
        if (happyTogether > 0) {
            scrollChange(scrollY);
        }
        Log.e("setTopLayoutTopMargin", dynamicTopLayoutMargin + " " + topLayoutMargin);
    }

    boolean isVisible = true;
    @Override
    public void scrollChange(int scrollY) {
        this.scrollY = scrollY;

        // 动态设置topLayout的距离顶部的高度
        setTopHeight(topLayout.getTop() - scrollY + topLayout.getMeasuredHeight());


        // 计算滚动到接近titlebar时，隐藏toplayout布局
        // 顶部高度topMargin：屏幕高度减去titlebar高度和状态栏高度
        // 滚动高度scrollHeight：scrollY是从toplayout的高度开始计算的，所以scrollY加上屏幕高度减去页面初始化后toplayout的高度
        int topMargin = displayMetrics.heightPixels - titleBarHeight - XScrollViewUtils.getStatusBarHeight((Activity) getContext());

        int scrollHeight = scrollY + (displayMetrics.heightPixels - topLayoutMargin - XScrollViewUtils.getNavigationHeight((Activity) getContext()) - XScrollViewUtils.getStatusBarHeight((android.app.Activity) getContext()));

        Log.e("setTopLayoutTopMargin", scrollY + " " + scrollHeight + " " + topMargin + " " + topLayoutMargin);

        if (scrollHeight + (int) (itemHeight * ITEM_OFFSET_VALUE + unfinishedRouteHeight) >= topMargin) {

            if (topLayout.getVisibility() == View.VISIBLE && isVisible) {
                isVisible = false;
                ObjectAnimatorHelper.alphaAnim(topLayout, new LinearInterpolator(), 100, false);
                ObjectAnimatorHelper.animEnd(new OnAnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        topLayout.setVisibility(View.INVISIBLE);
                        isVisible = true;
                    }
                });
            }

        } else {
            if (topLayout.getVisibility() == View.INVISIBLE) {
                topLayout.setVisibility(View.VISIBLE);
                ObjectAnimatorHelper.alphaAnim(topLayout, new LinearInterpolator(), 100, true);
            }
        }

        boolean touchMoon = scrollHeight + (int) (itemHeight * ITEM_OFFSET_VALUE + unfinishedRouteHeight) >= (topMargin + topLayout.getMeasuredHeight() - 30);

        // 更新titlebar

        RxBus.getDefault().post(new RefreshTitleBarEvent(touchMoon));
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
            LayoutInflater.from(getContext()).inflate(R.layout.layout_choose_location_item, this);

            mTypeIV = (ImageView) findViewById(R.id.type_icon);
            mInputET = (TextView) findViewById(R.id.input_location_et);

        }

    }


}
