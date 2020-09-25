package com.coolxtc.common.coustomview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Desc:
 * 禁止滑动的ViewPager
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
public class ForbidSlidingViewPager extends ViewPager {
    // the sliding page switch
    private boolean isSlidingEnable = true;

    public ForbidSlidingViewPager(Context context) {
        super(context);
    }

    public ForbidSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写此函数
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isSlidingEnable && super.onTouchEvent(ev);
    }

    //重写此函数
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isSlidingEnable && super.onInterceptTouchEvent(ev);
    }

    public void setSlidingEnable(boolean slidingEnable) {
        isSlidingEnable = slidingEnable;
    }
}
