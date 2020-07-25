package com.sk.xscrollviewdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Interpolator;

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
 */


public class AnimatorUtils {

    private static ObjectAnimator animator;
    private static AnimatorSet set;

    public static final int TOP_TO_VISIBLE = 0;
    public static final int TOP_TO_GONE = 1;
    public static final int BOTTOM_TO_VISIBLE = 2;
    public static final int BOTTOM_TO_GONE = 3;

    public static void animEnd(OnAnimListener animListener) {
        animator.addListener(animListener);
    }

    public static void alphaAnim(View view, Interpolator interpolator, int duration, boolean isVisible) {
        if (isVisible) {
            animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        } else {
            animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        }
        if (null != interpolator) { animator.setInterpolator(interpolator);}
        animator.setDuration(duration);
        animator.start();
    }

    public static void translationYAnim(View view, Interpolator interpolator, int duration, boolean isVisible, int toWhere) {

        set = new AnimatorSet();
        switch (toWhere) {
            case TOP_TO_VISIBLE:
                animator = ObjectAnimator.ofFloat(view, "translationY", -360f, 0f);
                break;
            case TOP_TO_GONE:
                animator = ObjectAnimator.ofFloat(view, "translationY", 0f, -360f);
                break;
            case BOTTOM_TO_VISIBLE:
                animator = ObjectAnimator.ofFloat(view, "translationY", 360f, 0f);
                break;
            case BOTTOM_TO_GONE:
                animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 360f);
                break;
            default:
                break;
        }

        if (null != interpolator) {animator.setInterpolator(interpolator);}
        animator.setDuration(duration);

        ObjectAnimator alphaAnim;
        if (isVisible) {
            alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        } else {
            alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        }
        if (null != interpolator) { alphaAnim.setInterpolator(interpolator);}
        alphaAnim.setDuration(duration);

        set.play(animator).with(alphaAnim);
        set.start();
    }

    public static void stopAnim() {
        if (null != set) { set.end();}
    }


    public static class OnAnimListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationStart(Animator animation) {

        }


    }

}
