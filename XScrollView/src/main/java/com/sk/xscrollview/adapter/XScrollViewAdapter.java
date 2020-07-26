package com.sk.xscrollview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import androidx.recyclerview.widget.RecyclerView;

import com.sk.xscrollview.bean.XScrollViewBean;
import com.sk.xscrollview.interf.LayoutConfigImpl;
import com.sk.xscrollview.utils.XScrollViewHolder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author sk on 2019-07-02.
 */
public class XScrollViewAdapter extends RecyclerView.Adapter<XScrollViewHolder> implements LayoutConfigImpl {


    /**行程位置*/
    private final static int ROUTE_INDEX = 0;
    /**优惠券位置*/
    private final static int COUPON_INDEX = 1;
    /** 活动位置*/
    private final static int ACTIVITIES_INDEX = 2;


    private int routeSize = 0;
    private int couponSize = 0;
    private int activitySize = 0;


    private int routeResourceId = 0;
    private int couponResourceId = 0;
    private int activityResourceId = 0;

    private View routeItemView, couponItemView, activityItemView;


    private int itemCount = 0;
    private XScrollViewBean entity = new XScrollViewBean();

    private AdapterItemViewListener adapterItemViewListener;


    @Override
    public XScrollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ROUTE_INDEX:
                routeItemView = LayoutInflater.from(parent.getContext()).inflate(routeResourceId, parent, false);
                return new XScrollViewHolder(routeItemView);
            case COUPON_INDEX:
                couponItemView = LayoutInflater.from(parent.getContext()).inflate(couponResourceId, parent, false);
                return new XScrollViewHolder(couponItemView);
            case ACTIVITIES_INDEX:
                activityItemView = LayoutInflater.from(parent.getContext()).inflate(activityResourceId, parent, false);
                return new XScrollViewHolder(activityItemView);
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }


    @Override
    public void onBindViewHolder(final XScrollViewHolder holder, final int position) {
        if (holder.itemView.equals(routeItemView)) {
            bindTypeRoute(holder, position);
        } else if (holder.itemView.equals(couponItemView)) {
            bindTypeCoupon(holder, position);
        } else {
            bindTypeActivities(holder, position);
        }


        holder.itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (null != adapterItemViewListener && position == 0) {
                    adapterItemViewListener.changeLayout(holder.itemView.getHeight());
                }
            }
        });
    }


    @Override
    public void setRouteLayout(int resourceId) {
        this.routeResourceId = resourceId;
    }

    @Override
    public void setCouponLayout(int resourceId) {
        this.couponResourceId = resourceId;
    }

    @Override
    public void setActivityLayout(int resourceId) {
        this.activityResourceId = resourceId;
    }

    private void bindTypeRoute(final XScrollViewHolder holder, final int position) {
        final ArrayList<Object> orders = entity.orders;
        if (orders != null && orders.size() > 0) {

            if (null != adapterItemViewListener) {
                adapterItemViewListener.initRouteView(holder, position);
            }

            holder.itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    if (null != adapterItemViewListener) {
                        adapterItemViewListener.unfinishedRouteHeight(holder.itemView.getMeasuredHeight());
                    }
                }
            });

        }

        holder.itemView.setVisibility(orders != null && orders.size() > 0 ? View.VISIBLE : View.GONE);
    }


    private void bindTypeCoupon(final XScrollViewHolder holder, final int position) {
        final ArrayList<Object> coupons = entity.coupons;
        if (coupons != null && coupons.size() > 0) {

            if (null != adapterItemViewListener) {
                adapterItemViewListener.initCouponView(holder, position);
            }

        }

        holder.itemView.setVisibility(coupons != null && coupons.size() > 0 ? View.VISIBLE : View.GONE);
    }


    private void bindTypeActivities(final XScrollViewHolder holder, final int position) {
        final ArrayList<Object> messages = entity.activities;
        if (messages != null && messages.size() > 0) {

            if (null != adapterItemViewListener) {
                adapterItemViewListener.initActivityView(holder, position);
            }
        }

        holder.itemView.setVisibility(messages != null && messages.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < routeSize) {
            return ROUTE_INDEX;
        } else if (position < (routeSize + couponSize)) {
            return COUPON_INDEX;
        } else {
            return ACTIVITIES_INDEX;
        }
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public void setData(XScrollViewBean entity) {
        routeSize = (null == entity.orders || 0 == entity.orders.size()) ? 0 : entity.orders.size();
        couponSize = (null == entity.coupons || 0 == entity.coupons.size()) ? 0 : entity.coupons.size();
        activitySize = (null == entity.activities || 0 == entity.activities.size()) ? 0 : entity.activities.size();

        itemCount = routeSize + couponSize + activitySize;
        notifyDataSetChanged();
    }

    public void setActivityData(Collection<?> activities) {
        if (null == this.entity.activities) {
            this.entity.activities = new ArrayList<>();
        } else {
            this.entity.activities.clear();
        }
        this.entity.activities.addAll(activities);

        setData(entity);
    }


    public void setCouponData(Collection<?> coupons) {
        if (null == this.entity.coupons) {
            this.entity.coupons = new ArrayList<>();
        } else {
            this.entity.coupons.clear();
        }
        this.entity.coupons.addAll(coupons);

        setData(entity);
    }

    public void setOrderData(Collection<?> orders) {
        if (null == this.entity.orders) {
            this.entity.orders = new ArrayList<>();
        } else {
            this.entity.orders.clear();
        }
        this.entity.orders.addAll(orders);

        setData(entity);
    }

    public void setAdapterItemViewListener(AdapterItemViewListener adapterItemViewListener) {
        this.adapterItemViewListener = adapterItemViewListener;
    }

    public interface AdapterItemViewListener {

        void changeLayout(int value);

        void initRouteView(XScrollViewHolder holder, int position);
        void initCouponView(XScrollViewHolder holder, int position);
        void initActivityView(XScrollViewHolder holder, int position);

        void unfinishedRouteHeight(int height);
        void homeAndCompanyHeight(int height);
    }

}
