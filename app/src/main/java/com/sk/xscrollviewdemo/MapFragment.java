package com.sk.xscrollviewdemo;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;
import com.sk.xscrollview.XScrollView;
import com.sk.xscrollviewdemo.bean.Activity;
import com.sk.xscrollviewdemo.bean.Coupon;
import com.sk.xscrollviewdemo.bean.Order;
import com.sk.xscrollview.utils.XScrollViewHolder;
import com.sk.xscrollviewdemo.view.LocationItemView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * @author sk on 2019-07-08.
 */
public class MapFragment extends Fragment implements XScrollView.InitItemViewListener {

    @BindView(R.id.scroll_view)
    XScrollView xScrollView;
    @BindView(R.id.map_view)
    TextureMapView mapView;

    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Coupon> coupons = new ArrayList<>();
    private ArrayList<Activity> activities = new ArrayList<>();

    private LocationItemView mSourceItem;
    private LocationItemView mDestItem;
    private TextView useCarNow, useCarPlan;

    private View titleBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("titleBar", "3");
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Log.e("titleBar", "0");
        ButterKnife.bind(this, view);
        Log.e("titleBar", "2");
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView.onCreate(savedInstanceState);
    }

    private void initView() {
        xScrollView.setTitleBar(titleBar);
        xScrollView.setInitItemViewListener(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                orders.clear();
                Order order = new Order();
                orders.add(order);
                xScrollView.setOrderText(orders);

                coupons.clear();
                Coupon coupon = new Coupon();
                coupons.add(coupon);
                xScrollView.setCouponText(coupons);

                activities.clear();
                Activity activity = new Activity();
                activities.add(activity);
                xScrollView.setActivityText(activities);
            }
        }, 1000);

        init();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void initRouteView(XScrollViewHolder holder, int position) {
        final Order order = orders.get(0);

        AppCompatTextView title = holder.getView(R.id.title);
        AppCompatTextView goRouteText = holder.getView(R.id.go_route_text);

        title.setText(holder.itemView.getContext().getString(R.string.hint_servicing_order));
        goRouteText.setText(holder.itemView.getContext().getString(R.string.title_go_route));


        goRouteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void initCouponView(XScrollViewHolder holder, int position) {

    }

    @Override
    public void initActivityView(XScrollViewHolder holder, int position) {

    }

    @Override
    public void initTopLayoutView(View view) {

        AppCompatTextView locationImg = view.findViewById(R.id.location_img);
        locationImg.setOnClickListener(new View.OnClickListener() {
            boolean index = false;
            @Override
            public void onClick(View view) {
                if (index) {
                    activities.clear();
                } else {
                    Activity activity = new Activity();
                    activities.add(activity);
                }
                index = !index;
                xScrollView.setActivityText(activities);
            }
        });
    }

    @Override
    public void initBottomLayoutView(View view) {
        mSourceItem = view.findViewById(R.id.choose_source_item_view);
        mDestItem = view.findViewById(R.id.choose_dest_item_view);
        useCarNow = view.findViewById(R.id.use_car_now);
        useCarPlan = view.findViewById(R.id.use_car_plan);
        useCarNow.setSelected(true);

        mSourceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    public void animTopLayoutVisible(View view) {
        if (!isVisible) {
            isVisible = true;
            AnimatorUtils.alphaAnim(view, new LinearInterpolator(), 100, true);
        }
    }

    boolean isVisible = true;
    @Override
    public void animTopLayoutGone(View view) {
        if (isVisible) {
            isVisible = false;
            AnimatorUtils.alphaAnim(view, new LinearInterpolator(), 100, false);
        }
    }

    @Override
    public void animTitleBar(boolean touchMoon) {
        ((MainActivity) getActivity()).animTitleBar(touchMoon);
    }

    public void setTitleBar(View titleBar) {
        this.titleBar = titleBar;
    }


    /**
     * 高德地图初始化
     */
    private void init() {
        mapView.getMap().setMyLocationEnabled(true);
        mapView.getMap().getUiSettings().setZoomControlsEnabled(false);
        mapView.getMap().getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        mapView.getMap().getUiSettings().setGestureScaleByMapCenter(true);
        mapView.getMap().getUiSettings().setRotateGesturesEnabled(false);
        mapView.getMap().getUiSettings().setTiltGesturesEnabled(false);
    }

}
