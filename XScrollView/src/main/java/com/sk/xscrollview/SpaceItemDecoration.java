package com.sk.xscrollview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author sk
 * @desc item间隔
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int left;
    private int right;
    private int top;
    private int bottom;

    public SpaceItemDecoration(int leftRight, int topBottom) {
        this.left = leftRight;
        this.right = leftRight;
        this.top = topBottom;
        this.bottom = topBottom;
    }

    public SpaceItemDecoration(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        try {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();

            //最后一项不需要right
            if (parent.getChildAdapterPosition(view) != gridLayoutManager.getItemCount() - 1) {
                outRect.right = right;
            }

            outRect.top = top;
            outRect.left = left;
            outRect.bottom = bottom;

        } catch (ClassCastException e) {

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();

            //竖直方向的
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                //最后一项需要 bottom
                if (parent.getChildAdapterPosition(view) == linearLayoutManager.getItemCount() - 1) {
                    outRect.bottom = bottom;
                }
                //第一项不需要 top
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = top;
                }
                outRect.left = left;
                outRect.right = right;
            } else {

                outRect.right = right;
                outRect.top = top;
                outRect.left = left;
                outRect.bottom = bottom;
            }
        }

    }
}