package com.sk.xscrollviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.sk.xscrollview.XScrollView;
import com.sk.xscrollview.utils.XScrollViewHolder;
import com.sk.xscrollviewdemo.bean.Activity;
import com.sk.xscrollviewdemo.bean.Coupon;
import com.sk.xscrollviewdemo.bean.Order;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sk.xscrollviewdemo.AnimatorUtils.TOP_TO_GONE;
import static com.sk.xscrollviewdemo.AnimatorUtils.TOP_TO_VISIBLE;

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


public class SimpleActivity extends AppCompatActivity implements XScrollView.InitItemViewListener {

    @BindView(R.id.scroll_view)
    XScrollView xScrollView;
    @BindView(R.id.title_bar)
    MaterialToolbar titleBar;
    @BindView(R.id.to_bottom_img)
    AppCompatImageView toBottomImg;


    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Coupon> coupons = new ArrayList<>();
    private ArrayList<Activity> activities = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        ButterKnife.bind(this);

        xScrollView.setInitItemViewListener(this);
        xScrollView.setTitleBar(titleBar);
        // 设置recyclerview 可以展示部分
        xScrollView.setItemOffsetValue(0.2f);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Order order = new Order();
                orders.add(order);
                xScrollView.setOrderText(orders);

                Coupon coupon = new Coupon();
                coupons.add(coupon);
                xScrollView.setCouponText(coupons);

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
                    activities.add(activity);
                }
                index = !index;
                xScrollView.setActivityText(activities);
            }
        });
    }

    @Override
    public void initBottomLayoutView(View view) {

    }

    @Override
    public void animTopLayoutVisible(View view) {
        if (!isTopLayoutVisible) {
            isTopLayoutVisible = true;
            AnimatorUtils.alphaAnim(view, new LinearInterpolator(), 100, true);
        }
    }

    boolean isTopLayoutVisible = true;
    @Override
    public void animTopLayoutGone(View view) {
        if (isTopLayoutVisible) {
            isTopLayoutVisible = false;
            AnimatorUtils.alphaAnim(view, new LinearInterpolator(), 100, false);
        }
    }

    boolean isVisible = true;
    @Override
    public void animTitleBar(boolean touchMoon) {
        if (touchMoon) {

            if (isVisible) {
                isVisible = false;

                AnimatorUtils.translationYAnim(titleBar, new AnticipateOvershootInterpolator(), 1000, false, TOP_TO_GONE);
                toBottomImg.setVisibility(View.VISIBLE);
            }

        } else {

            if (!isVisible) {
                isVisible = true;

                AnimatorUtils.translationYAnim(titleBar, new AnticipateOvershootInterpolator(), 1000, true, TOP_TO_VISIBLE);
                toBottomImg.setVisibility(View.GONE);
            }

        }
    }

    @OnClick(R.id.to_bottom_img)
    public void toBottom() {
        xScrollView.scrollToBottom();
    }
}
