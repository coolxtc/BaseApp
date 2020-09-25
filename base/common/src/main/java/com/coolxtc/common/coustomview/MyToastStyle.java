package com.coolxtc.common.coustomview;

import android.view.Gravity;

import com.coolxtc.common.App;
import com.coolxtc.common.R;
import com.hjq.toast.IToastStyle;

/**
 * Desc:
 * Toast样式设置
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
public class MyToastStyle implements IToastStyle {
    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return (int) App.instance.getResources().getDimension(R.dimen.dp_80);
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public int getCornerRadius() {
        return (int) App.instance.getResources().getDimension(R.dimen.dp_8);
    }

    @Override
    public int getBackgroundColor() {
        return 0XB3000000;
    }

    @Override
    public int getTextColor() {
        return 0XFFFFFFFF;
    }

    @Override
    public float getTextSize() {
        return (int) App.instance.getResources().getDimension(R.dimen.sp_16);
    }

    @Override
    public int getMaxLines() {
        return 3;
    }

    @Override
    public int getPaddingStart() {
        return (int) App.instance.getResources().getDimension(R.dimen.dp_10);
    }

    @Override
    public int getPaddingTop() {
        return (int) App.instance.getResources().getDimension(R.dimen.dp_9);
    }

    @Override
    public int getPaddingEnd() {
        return getPaddingStart();
    }

    @Override
    public int getPaddingBottom() {
        return getPaddingTop();
    }
}
