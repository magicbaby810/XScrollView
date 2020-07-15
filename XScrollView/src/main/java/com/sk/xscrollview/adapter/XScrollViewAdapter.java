package com.sk.xscrollview.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;


import com.sk.xscrollview.R;
import com.sk.xscrollview.bean.XScrollViewBean;
import com.sk.xscrollview.interf.LayoutConfigImpl;
import com.sk.xscrollview.utils.SuperViewHolder;

import java.util.ArrayList;

/**
 * @author sk on 2019-07-02.
 */
public class XScrollViewAdapter extends ListBaseAdapter<SuperViewHolder> implements LayoutConfigImpl {


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

    private LayoutChangeListener layoutChangeListener;

    public XScrollViewAdapter(Context context) {
        super(context);
    }


    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ROUTE_INDEX:
                routeItemView = LayoutInflater.from(parent.getContext()).inflate(routeResourceId, parent, false);
                return new SuperViewHolder(routeItemView);
            case COUPON_INDEX:
                couponItemView = LayoutInflater.from(parent.getContext()).inflate(couponResourceId, parent, false);
                return new SuperViewHolder(couponItemView);
            case ACTIVITIES_INDEX:
                activityItemView = LayoutInflater.from(parent.getContext()).inflate(activityResourceId, parent, false);
                return new SuperViewHolder(activityItemView);
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }


    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
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
                if (null != layoutChangeListener && position == 0) {
                    layoutChangeListener.changeLayout(holder.itemView.getHeight());
                }
            }
        });
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

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

    private void bindTypeRoute(final SuperViewHolder holder, final int position) {
        final ArrayList<Object> orders = entity.orders;
        if (orders != null && orders.size() > 0) {




        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.itemView.setLayoutParams(ll);
        }
    }


    private void bindTypeCoupon(final SuperViewHolder holder, final int position) {
        final ArrayList<Object> coupons = entity.coupons;
        if (coupons != null && coupons.size() > 0) {


            AppCompatTextView money = holder.itemView.findViewById(R.id.tv_coupon_rebate);

            Coupon coupon = coupons.get(0);
            if (null != coupon) {
                SpannableString builderMoney;
                String text = AppUtils.getMoney(coupon.faceValue).replace(".00", "");
                if (text.contains(".") && text.endsWith("0")) {
                    text = text.substring(0, text.length() - 1);
                }
                text = text + "元";
                builderMoney = new SpannableString(text);
                builderMoney.setSpan(new RelativeSizeSpan(2), 0, text.length() - 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                money.setText(builderMoney);
            }

            holder.seeMoreText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.itemView.setLayoutParams(ll);
        }
    }


    private void bindTypeActivities(final SuperViewHolder holder, final int position) {
        final ArrayList<Object> messages = entity.activities;
        if (messages != null && messages.size() > 0) {



        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.itemView.setLayoutParams(ll);
        }
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

    public void setActivityData(ArrayList<Object> activities) {
        entity.activities = activities;
        setData(entity);
    }


    public void setCouponData(ArrayList<Object> coupons) {
        this.entity.coupons = coupons;
        setData(entity);
    }

    public void setOrderData(ArrayList<Object> orders) {
        this.entity.orders = orders;
        setData(entity);
    }

    public interface LayoutChangeListener {
        void changeLayout(int value);
    }

    public void setLayoutChangeListener(LayoutChangeListener layoutChangeListener) {
        this.layoutChangeListener = layoutChangeListener;
    }

}
