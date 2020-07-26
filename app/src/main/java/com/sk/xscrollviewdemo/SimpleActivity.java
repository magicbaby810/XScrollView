package com.sk.xscrollviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.sk.xscrollview.XScrollView;
import com.sk.xscrollview.utils.XScrollViewHolder;
import com.sk.xscrollviewdemo.bean.Activity;
import com.sk.xscrollviewdemo.bean.Coupon;
import com.sk.xscrollviewdemo.bean.Order;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * |                   quu..__
 * |                    $$  `---.__
 * |                     "$        `--.                          ___.---uuudP
 * |                      `$           `.__.------.__     __.---'      $$$$"              .
 * |                        "          -'            `-.-'            $$$"              .'|
 * |                          ".                                       d$"             _.'  |
 * |                            `.   /                              ..."             .'     |
 * |                              `./                           ..::-'            _.'       |
 * |                               /                         .:::-'            .-'         .'
 * |                              :                          ::''\          _.'            |
 * |                             .' .-.             .-.           `.      .'               |
 * |                             : /'$$|           .@"$\           `.   .'              _.-'
 * |                            .'|$$|          |$$,$$|           |  <            _.-'
 * |                            | `:$$:'          :$$$$$:           `.  `.       .-'
 * |                            :                  `"--'             |    `-.     \
 * |                           :$$.       ==             .$$$.       `.      `.    `\
 * |                           |$$:                      :$$$:        |        >     >
 * |                           |$'     `..'`..'          `$$$'        x:      /     /
 * |                            \                                   xXX|     /    ./
 * |                             \                                xXXX'|    /   ./
 * |                             /`-.                                  `.  /   /
 * |                            :    `-  ...........,                   | /  .'
 * |                            |         ``:::::::'       .            |<    `.
 * |                            |             ```          |           x| \ `.:``.
 * |                            |                         .'    /'   xXX|  `:`M`M':.
 * |                            |    |                    ;    /:' xXXX'|  -'MMMMM:'
 * |                            `.  .'                   :    /:'       |-'MMMM.-'
 * |                             |  |                   .'   /'        .'MMM.-'
 * |                             `'`'                   :  ,'          |MMM<
 * |                               |                     `'            |tbap\
 * |                                \                                  :MM.-'
 * |                                 \                 |              .''
 * |                                  \.               `.            /
 * |                                   /     .:::::::.. :           /
 * |                                  |     .:::::::::::`.         /
 * |                                  |   .:::------------\       /
 * |                                 /   .''               >::'  /
 * |                                 `',:                 :    .'
 * |
 * |                                                      `:.:'
 * |
 * |
 * |
 *
 * @author SK on 2020/7/26
 * contact magicbaby810@gmail.com
 */


public class SimpleActivity extends AppCompatActivity implements XScrollView.InitItemViewListener, View.OnLayoutChangeListener {

    @BindView(R.id.scroll_view)
    XScrollView xScrollView;
    @BindView(R.id.title_bar)
    MaterialToolbar titleBar;


    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Activity> activities = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        ButterKnife.bind(this);

        titleBar.addOnLayoutChangeListener(this);
        xScrollView.setInitItemViewListener(this);

        // 设置recyclerview 可以展示部分
        xScrollView.setItemOffsetValue(0.2f);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Order order = new Order();
                orders.add(order);
                xScrollView.setOrderText(orders);

//                Coupon coupon = new Coupon();
//                coupons.add(coupon);
//                xScrollView.setCouponText(coupons);
//
                Activity activity = new Activity();
                activities.add(activity);
                xScrollView.setActivityText(activities);
            }
        }, 1000);
    }


    @Override
    public void initRouteView(XScrollViewHolder holder, int position) {

    }

    @Override
    public void initCouponView(XScrollViewHolder holder, int position) {

    }

    @Override
    public void initActivityView(XScrollViewHolder holder, int position) {

    }

    @Override
    public void initTopLayoutView(View view) {

    }

    @Override
    public void initBottomLayoutView(View view) {

    }

    @Override
    public void animTopLayoutVisible(View view) {

    }

    @Override
    public void animTopLayoutGone(View view) {

    }

    @Override
    public void animTitleBar(boolean touchMoon) {

    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        xScrollView.setTitleBarHeight(view.getMeasuredHeight());
    }
}
