package com.sk.dididemo.adapter;

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


import com.sk.commons.utils.AppUtils;
import com.sk.dididemo.R;
import com.sk.dididemo.bean.Activity;
import com.sk.dididemo.bean.Coupon;
import com.sk.dididemo.bean.NewLandBean;
import com.sk.dididemo.bean.Order;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author sk on 2019-07-02.
 */
public class NewLandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    /**行程位置*/
    private final static int ROUTE_INDEX = 0;
    /**优惠券位置*/
    private final static int COUPON_INDEX = 1;
    /** 活动位置*/
    private final static int ACTIVITIES_INDEX = 2;


    private int routeSize = 0;
    private int couponSize = 0;
    private int activitySize = 0;


    private int itemCount = 0;
    private NewLandBean entity = new NewLandBean();

    private LayoutChangeListener layoutChangeListener;



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ROUTE_INDEX:
                return new HolderRoute(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_land_route, parent, false));
            case COUPON_INDEX:
                return new HolderCoupon(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_land_coupon, parent, false));
            case ACTIVITIES_INDEX:
                return new HolderActivities(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_land_activities, parent, false));
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HolderRoute) {
            bindTypeRoute((HolderRoute) holder, position);
        } else if (holder instanceof HolderCoupon) {
            bindTypeCoupon((HolderCoupon) holder, position);
        } else {
            bindTypeActivities((HolderActivities) holder, position);
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


    public class HolderRoute extends RecyclerView.ViewHolder {
        public View routeLayout;
        public AppCompatTextView goRouteText;

        public HolderRoute(View itemView) {
            super(itemView);
            routeLayout = itemView.findViewById(R.id.route_layout);
            goRouteText = itemView.findViewById(R.id.go_route_text);
        }
    }

    public class HolderCoupon extends RecyclerView.ViewHolder {
        public View couponLayout;
        public AppCompatTextView seeMoreText;

        public HolderCoupon(View itemView) {
            super(itemView);
            couponLayout = itemView.findViewById(R.id.coupon_layout);
            seeMoreText = itemView.findViewById(R.id.see_more_text);
        }
    }

    public class HolderActivities extends RecyclerView.ViewHolder {
        public View activityLayout;
        public AppCompatImageView activityImg;

        public HolderActivities(View itemView) {
            super(itemView);
            activityLayout = itemView.findViewById(R.id.activity_layout);
            activityImg = itemView.findViewById(R.id.activity_img);
        }
    }

    private void bindTypeRoute(final HolderRoute holder, final int position) {
        final ArrayList<Order> orders = entity.orders;
        if (orders != null && orders.size() > 0) {

            holder.goRouteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppUtils.isFastClick()) {
                        return;
                    }


                }
            });


        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.routeLayout.setLayoutParams(ll);
        }
    }


    private void bindTypeCoupon(final HolderCoupon holder, final int position) {
        final ArrayList<Coupon> coupons = entity.coupons;
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
                    if (AppUtils.isFastClick()) {
                        return;
                    }


                }
            });

        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.couponLayout.setLayoutParams(ll);
        }
    }


    private void bindTypeActivities(final HolderActivities holder, final int position) {
        final ArrayList<Activity> messages = entity.activities;
        if (messages != null && messages.size() > 0) {



        } else {
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.activityLayout.setLayoutParams(ll);
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

    public void setData(NewLandBean entity) {
        routeSize = (null == entity.orders || 0 == entity.orders.size()) ? 0 : entity.orders.size();
        couponSize = (null == entity.coupons || 0 == entity.coupons.size()) ? 0 : entity.coupons.size();
        activitySize = (null == entity.activities || 0 == entity.activities.size()) ? 0 : entity.activities.size();

        itemCount = routeSize + couponSize + activitySize;
        notifyDataSetChanged();
    }

    public void setActivityData(ArrayList<Activity> activities) {
        entity.activities = activities;
        setData(entity);
    }


    public void setCouponData(ArrayList<Coupon> coupons) {
        this.entity.coupons = coupons;
        setData(entity);
    }

    public void setOrderData(ArrayList<Order> orders) {
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
