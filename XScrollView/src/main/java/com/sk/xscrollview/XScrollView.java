package com.sk.xscrollview;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.xscrollview.adapter.XScrollViewAdapter;
import com.sk.xscrollview.utils.XScrollViewHolder;
import com.sk.xscrollview.utils.XScrollViewUtils;

import java.util.Collection;


/**
 * @author sk
 * @date 2019-7-08
 */
public class XScrollView extends XNestedScrollView implements XNestedScrollView.ScrollChangeListener,
        XScrollViewAdapter.AdapterItemViewListener {

    public final static String TAG = "XScrollView";

    /** 滚动到顶部临界点 */
    private int titleBarHeight;
    /** 固定toplayout到底部距离 */
    private int topLayoutMargin;
    /** 动态toplayout距离顶部的距离 */
    private int dynamicTopLayoutMargin;
    /** recyclerview show part 默认偏移系数 */
    private float ITEM_OFFSET_VALUE = 0.18f;
    /** recyclerview show part最大固定高度 */
    private int maxHeight = 900;
    /** 滚动的高度*/
    private int scrollY;
    /** 未完成行程高度 */
    private int unfinishedRouteHeight = 0;
    /** 家和公司高度 */
    private int homeAndCompanyHeight = 0;
    /** recyclerview item高度 */
    private int itemHeight = 0;
    /** recyclerview divider高度 */
    private final static int DIVIDER_HEIGHT = 6;

    private View topView, bottomView;
    private LinearLayout backgroundLayout;
    private RelativeLayout topLayout, bottomLayout;

    private int routeLayoutResourceId, couponLayoutResourceId, activityLayoutResourceId, topLayoutResourceId, bottomLayoutResourceId;
    private int backgroundColorResourceId;

    private RecyclerView newLandRecyclerView;

    private XScrollViewAdapter xScrollViewAdapter;

    private DisplayMetrics displayMetrics;

    private InitItemViewListener initItemViewListener;


    public XScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void sizeChanged() {
        changeLayout(0);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XScrollView);

            routeLayoutResourceId = array.getResourceId(R.styleable.XScrollView_routeLayout, -1);
            couponLayoutResourceId = array.getResourceId(R.styleable.XScrollView_couponLayout, -1);
            activityLayoutResourceId = array.getResourceId(R.styleable.XScrollView_activityLayout, -1);

            topLayoutResourceId = array.getResourceId(R.styleable.XScrollView_topLayout, -1);
            bottomLayoutResourceId = array.getResourceId(R.styleable.XScrollView_bottomLayout, -1);

            backgroundColorResourceId = array.getColor(R.styleable.XScrollView_backgroundColor, ContextCompat.getColor(getContext(), android.R.color.transparent));

            array.recycle();
        }

        setScrollChangeListener(this);

        LayoutInflater.from(getContext()).inflate(R.layout.layout_scroll_view, this);
        newLandRecyclerView = findViewById(R.id.new_land_view);
        topLayout = findViewById(R.id.top_layout);
        backgroundLayout = findViewById(R.id.background_layout);
        bottomLayout = findViewById(R.id.bottom_layout);

        backgroundLayout.setBackgroundColor(backgroundColorResourceId);
        topView = LayoutInflater.from(getContext()).inflate(topLayoutResourceId, topLayout, false);
        topLayout.addView(topView);
        bottomView = LayoutInflater.from(getContext()).inflate(bottomLayoutResourceId, bottomLayout, false);
        bottomLayout.addView(bottomView);

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


                changeLayout(v.getMeasuredHeight() >= 0 && v.getMeasuredHeight() < 1500 ? v.getMeasuredHeight() : maxHeight);

//                if (oldTop == 0) {
//                    ObjectAnimatorHelper.translationYBottomAnim(XScrollView.this, new AnticipateOvershootInterpolator(), 1000, ObjectAnimatorHelper.BOTTOM_TO_VISIBLE, 360f);
//                }
            }
        });

        xScrollViewAdapter = new XScrollViewAdapter();
        xScrollViewAdapter.setAdapterItemViewListener(this);
        xScrollViewAdapter.setCouponLayout(couponLayoutResourceId);
        xScrollViewAdapter.setRouteLayout(routeLayoutResourceId);
        xScrollViewAdapter.setActivityLayout(activityLayoutResourceId);

        newLandRecyclerView.setAdapter(xScrollViewAdapter);

        displayMetrics = XScrollViewUtils.getDpi(getContext());
        // 适配低分辨率手机,以2340分辨率做参照
        if (displayMetrics.heightPixels < 2340) {
            maxHeight = displayMetrics.heightPixels * maxHeight / 2340;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ViewGroup) findViewById(R.id.root_layout)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }
    }

    public void setCouponText(Collection<?> coupons) {
        xScrollViewAdapter.setCouponData(coupons);
    }

    public void setActivityText(Collection<?> activities) {
        xScrollViewAdapter.setActivityData(activities);
    }

    public void setOrderText(Collection<?> orders) {
        xScrollViewAdapter.setOrderData(orders);
    }


    @Override
    public void changeLayout(int value) {
        itemHeight = value;

        setTopLayoutTopMargin(itemHeight);

        // 初始化超过底布局，响应地图
        setTopHeight(dynamicTopLayoutMargin + topLayout.getMeasuredHeight());
    }

    private void setTopLayoutTopMargin(int itemHeight) {

//        Log.e(TAG, "topview：" + topLayout.getMeasuredHeight() + " bottomview：" + bottomLayout.getMeasuredHeight() + " 列表首次展示高度：" + calculateOffsetHeight(itemHeight) + " 状态栏：" + XScrollViewUtils.getStatusBarHeight((Activity) getContext()) + " 导航栏：" + XScrollViewUtils.getNavigationHeight((Activity) getContext()) + " unfinishedRouteHeight " + unfinishedRouteHeight + " itemHeight " + itemHeight);

        int tempTopLayoutMargin = 0;
        if (topLayoutMargin != 0) {
            tempTopLayoutMargin = topLayoutMargin;
        }
        topLayoutMargin = displayMetrics.heightPixels - (topLayout.getMeasuredHeight() + bottomLayout.getMeasuredHeight() + XScrollViewUtils.getStatusBarHeight((Activity) getContext()) + XScrollViewUtils.getNavigationHeight((Activity) getContext()));

        dynamicTopLayoutMargin = topLayoutMargin - calculateOffsetHeight(itemHeight);

        // 底布局不越过屏幕的一半
        if (dynamicTopLayoutMargin < (displayMetrics.heightPixels - (XScrollViewUtils.getStatusBarHeight((Activity) getContext()) + XScrollViewUtils.getNavigationHeight((Activity) getContext()))) / 2) {
            dynamicTopLayoutMargin = (displayMetrics.heightPixels - (XScrollViewUtils.getStatusBarHeight((Activity) getContext()) + XScrollViewUtils.getNavigationHeight((Activity) getContext()))) / 2 - 100;
        }

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin = dynamicTopLayoutMargin;
        topLayout.setLayoutParams(ll);

        int happyTogether = scrollY + (tempTopLayoutMargin - topLayoutMargin);
        if (happyTogether > 0) {
            scrollChange(scrollY);
        }
//        Log.e(TAG, dynamicTopLayoutMargin + " " + topLayoutMargin);
    }

    private int calculateOffsetHeight(int itemHeight) {

        int tempHeight = 0;
        if (0 != unfinishedRouteHeight && 0 != homeAndCompanyHeight) {
            return unfinishedRouteHeight + homeAndCompanyHeight - 10;
        } else if (0 == unfinishedRouteHeight && 0 != homeAndCompanyHeight) {
            tempHeight = homeAndCompanyHeight;
        } else if (0 == homeAndCompanyHeight && 0 != unfinishedRouteHeight) {
            tempHeight = unfinishedRouteHeight;
        }

        return (int) (itemHeight * ITEM_OFFSET_VALUE + tempHeight);
    }

    @Override
    public void scrollChange(int scrollY) {
        this.scrollY = scrollY;

        // 动态设置topLayout的距离顶部的高度
        setTopHeight(topLayout.getTop() - scrollY + topLayout.getMeasuredHeight());

        int topMargin = displayMetrics.heightPixels - titleBarHeight - XScrollViewUtils.getStatusBarHeight((Activity) getContext());

//        int scrollHeight = scrollY + (displayMetrics.heightPixels - topLayoutMargin - XScrollViewUtils.getNavigationHeight((Activity) getContext()) - XScrollViewUtils.getStatusBarHeight((Activity) getContext()));

        //int scrollHeightOffset = scrollHeight + calculateOffsetHeight(itemHeight);
        int scrollHeightOffset = scrollY + (displayMetrics.heightPixels - topLayout.getTop() - XScrollViewUtils.getStatusBarHeight((Activity) getContext()));
        //Log.e(TAG, scrollY + " " + topMargin + " " + topLayoutMargin + " " + titleBarHeight + " " + scrollHeightOffset);

        if (scrollHeightOffset >= topMargin) {

            if (null != initItemViewListener) {
                initItemViewListener.animTopLayoutGone(topLayout);
            }

        } else {
            if (null != initItemViewListener) {
                initItemViewListener.animTopLayoutVisible(topLayout);
            }
        }

        boolean touchMoon = scrollHeightOffset - topLayout.getMeasuredHeight() >= topMargin;

        if (null != initItemViewListener) {
            initItemViewListener.animTitleBar(touchMoon);
        }
    }

    @Override
    public void initRouteView(XScrollViewHolder holder, int position) {
        if (null != initItemViewListener) {
            initItemViewListener.initRouteView(holder, position);
        }
    }

    @Override
    public void initCouponView(XScrollViewHolder holder, int position) {
        if (null != initItemViewListener) {
            initItemViewListener.initCouponView(holder, position);
        }
    }

    @Override
    public void initActivityView(XScrollViewHolder holder, int position) {
        if (null != initItemViewListener) {
            initItemViewListener.initActivityView(holder, position);
        }
    }

    public void setTitleBarHeight(int height) {
        titleBarHeight = height;
    }

    public void setItemOffsetValue(float itemOffsetValue) {
        ITEM_OFFSET_VALUE = itemOffsetValue;
    }

    @Override
    public void unfinishedRouteHeight(int height) {
        unfinishedRouteHeight = height + XScrollViewUtils.dip2px(getContext(), DIVIDER_HEIGHT);
    }

    @Override
    public void homeAndCompanyHeight(int height) {

    }

    public void setInitItemViewListener(InitItemViewListener initItemViewListener) {
        this.initItemViewListener = initItemViewListener;

        if (null != initItemViewListener) {
            initItemViewListener.initTopLayoutView(topView);
            initItemViewListener.initBottomLayoutView(bottomView);
        }
    }

    public interface InitItemViewListener {

        void initRouteView(XScrollViewHolder holder, int position);
        void initCouponView(XScrollViewHolder holder, int position);
        void initActivityView(XScrollViewHolder holder, int position);

        void initTopLayoutView(View view);
        void initBottomLayoutView(View view);

        void animTopLayoutVisible(View view);
        void animTopLayoutGone(View view);

        void animTitleBar(boolean touchMoon);
    }


}
