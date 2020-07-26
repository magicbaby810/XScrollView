package com.sk.xscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.dynamicanimation.animation.SpringAnimation;


/**
 * 增加回弹效果
 * 以及超出nestedscrollview内布局部分，仍继续让下层布局响应touch事件
 *
 * @author sk on 2019-07-04.
 */
public abstract class XNestedScrollView extends NestedScrollView implements NestedScrollView.OnScrollChangeListener {

    private float startDragY;
    /** 布局顶部距离屏幕顶部的距离*/
    private int topMargin;


    private SpringAnimation springAnim;

    private ScrollChangeListener scrollChangeListener;


    public XNestedScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public XNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setFillViewport(true);
        setNestedScrollingEnabled(false);
        setOnScrollChangeListener(this);

        springAnim = new SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0);
        //刚度 默认1200 值越大回弹的速度越快
        springAnim.getSpring().setStiffness(1000.0f);
        //阻尼 默认0.5 值越小，回弹之后来回的次数越多
        springAnim.getSpring().setDampingRatio(2f);

    }

    /**
     * 设置nestedscrollview内最顶部布局
     * 超过顶部布局的位置不拦截touch事件，nestedscrollview下面的布局仍继续响应touch事件
     * topMargin供onTouchEvent里面做判断使用
     *
     * @param height
     */
    protected void setTopHeight(int height) {
        topMargin = height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getY() < topMargin) {
            springBack();
            return false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() <= 0) {
                    //顶部下拉
                    if (startDragY == 0) {
                        startDragY = ev.getRawY();
                    }
                    if (ev.getRawY() - startDragY >= 0) {
                        setTranslationY((ev.getRawY() - startDragY) / 3);
                        return true;
                    } else {
                        startDragY = 0;
                        springAnim.cancel();
                        setTranslationY(0);
                    }

                } else if ((getScrollY() + getHeight()) >= getChildAt(0).getMeasuredHeight()) {
                    //底部上拉
                    if (startDragY == 0) {
                        startDragY = ev.getRawY();
                    }

                    if (ev.getRawY() - startDragY <= 0) {
                        setTranslationY((ev.getRawY() - startDragY) / 3);
                        return true;
                    } else {
                        startDragY = 0;
                        springAnim.cancel();
                        setTranslationY(0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                springBack();
                break;
                default:break;
        }

        return super.onTouchEvent(ev);
    }

    private void springBack() {
        if (getTranslationY() != 0) {
            springAnim.start();
        }
        startDragY = 0;
    }

    /**
     * 把布局滚动到底部
     *
     */
    public void scrollToBottom() {
        fullScroll(View.FOCUS_UP);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (null != scrollChangeListener) {
//            Log.e(XScrollView.TAG, "onScrollChange " + scrollY + " " + oldScrollY);
            scrollChangeListener.scrollChange(scrollY);
        }
    }

    public interface ScrollChangeListener {
         void scrollChange(int scrollY);
    }

    public void setScrollChangeListener(ScrollChangeListener scrollChangeListener) {
        this.scrollChangeListener = scrollChangeListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        sizeChanged();
    }

    protected abstract void sizeChanged();

}
