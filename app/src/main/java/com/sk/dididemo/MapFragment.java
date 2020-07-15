package com.sk.dididemo;

import android.os.Handler;

import com.sk.commons.base.BaseFragment;
import com.sk.dididemo.bean.Coupon;
import com.sk.dididemo.bean.Order;
import com.sk.xscrollview.XScrollView;

import butterknife.BindView;

/**
 * @author sk on 2019-07-08.
 */
public class MapFragment extends BaseFragment {

    @BindView(R.id.choose_location_widget)
    XScrollView chooseLocationWidget;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Order order = new Order();
                chooseLocationWidget.setOrderText(order);
                Coupon coupon = new Coupon();
                chooseLocationWidget.setCouponText(coupon);
            }
        }, 1000);




    }


}
