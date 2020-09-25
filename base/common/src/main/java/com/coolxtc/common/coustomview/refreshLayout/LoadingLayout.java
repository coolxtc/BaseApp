package com.coolxtc.common.coustomview.refreshLayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.coolxtc.common.network.NetWorkCode;
import com.coolxtc.common.R;

import java.util.ArrayList;

/**
 * Desc:
 * 通用的加载提示组件，包括开始加载、停止加载以及加载失败三种状态。
 * 请配合{@link R.layout#loading_layout}使用（在layout xml中include或者代码中Inflate）
 *
 * @author xtc
 * @date 2020/9/3
 * @email qsawer888@126.com
 */
public class LoadingLayout extends RelativeLayout {

    private LinearLayout mLoadingContainer;
    private LinearLayout mLoadingFailedContainer;
    private ProgressBar mProgressView;
    private TextView mFailedView;
    private TextView mReloadButton;
    private TextView mCustomButton;

    private volatile Status mStatus = Status.NOT_LOADED;

    private ArrayList<View> mBindViewList = new ArrayList<>();
    private Drawable loadingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.common_loading);

    public LoadingLayout(Context context) {
        super(context);
    }

    public LoadingLayout(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoadingContainer = findViewById(R.id.loading_container);
        mLoadingFailedContainer = findViewById(R.id.loading_failed_container);
        setOnClickListener(v -> {
            //	防止透传
        });
        mProgressView = findViewById(R.id.loading_progress);
        mFailedView = findViewById(R.id.loading_failed);
        mReloadButton = findViewById(R.id.loading_reload);
        mCustomButton = findViewById(R.id.loading_custom);
//        mProgressView.setIndeterminateDrawable(null);
//        mProgressView.setIndeterminate(false);
    }

    /**
     * 绑定到该view，以便通过loading状态控制该view的显示。<b>注意：执行该方法后，会先将该view隐藏</b>。
     *
     * @param view
     */
    public void addBindView(@NonNull View view) {
        mBindViewList.add(view);
        view.setVisibility(GONE);
    }

    public boolean isLoading() {
        if (getVisibility() == View.VISIBLE && mLoadingContainer.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取Loading时显示的ProgressBar，默认为菊花转的样式，可自行更改
     *
     * @return
     */
    public ProgressBar getProgressView() {
        return mProgressView;
    }

    /**
     * 获取Load失败的提示View，默认由图片（居上）和文字（居下）组成，可自行更改
     *
     * @return
     */
    public TextView getFailedView() {
        return mFailedView;
    }

    /**
     * 获取Load失败后的重新加载按钮，可自定义事件和显示样式
     *
     * @return
     */
    public TextView getReloadButton() {
        return mReloadButton;
    }

    /**
     * 获取自定义事件和显示样式Btn
     *
     * @return
     */
    public TextView getCustomButton() {
        return mCustomButton;
    }

    /**
     * 显示loading界面（可以在非UI线程中执行）
     */
    public void startLoading() {
        mStatus = Status.LOADING;
        runCallBack(() -> {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            setVisibility(VISIBLE);
            mLoadingContainer.setVisibility(VISIBLE);
            mProgressView.setIndeterminateDrawable(loadingDrawable);
            mLoadingFailedContainer.setVisibility(GONE);
            for (View bindView : mBindViewList) {
                bindView.setVisibility(GONE);
            }
        });
    }

    /**
     * 结束（隐藏）loading界面（可以在非UI线程中执行）
     */
    public void stopLoading() {
        mStatus = Status.NOT_LOADED;
        runCallBack(() -> {
            setVisibility(GONE);
            mLoadingContainer.setVisibility(GONE);
//            mProgressView.setIndeterminateDrawable(null);
            mLoadingFailedContainer.setVisibility(GONE);
            for (View bindView : mBindViewList) {
                bindView.setVisibility(VISIBLE);
            }
        });
    }

    /**
     * 失败的页面，用于自定义
     *
     * @param resId
     * @param errormsg
     */
    public void failedLoading(final int resId, final String errormsg) {
        failLoading(resId, errormsg, null);
    }

    public void failLoading(final int resId, final String errormsg, final OnClickListener onClickListener) {
        if (resId <= 0) {
            return;
        }
        mStatus = Status.FAILED;
        runCallBack(() -> {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            setVisibility(VISIBLE);
            mLoadingContainer.setVisibility(GONE);
//            mProgressView.setIndeterminateDrawable(null);
            mLoadingFailedContainer.setVisibility(VISIBLE);
            Drawable drawable = getResources().getDrawable(resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mFailedView.setCompoundDrawables(null, drawable, null, null);
            mFailedView.setText(errormsg);
//                mFailedView.setOnClickListener(onClickListener);
        });
    }

    /**
     * 显示loading失败的界面（可以在非UI线程中执行）
     */
    public void failedLoading(final String errormsg, int statusCode) {
        mStatus = Status.FAILED;
        if (statusCode == 404 || statusCode == 408) {
            failedLoading(R.mipmap.ic_no_network, NetWorkCode.NETWORK_ERR_TOAST);
        } else {
            failedLoading(R.mipmap.ic_fail_to_load, errormsg);
        }
    }

    public Status getStatus() {
        return mStatus;
    }

    private void runCallBack(@NonNull Runnable callBack) {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(callBack);
        } else {
            post(callBack);
        }
    }

    public enum Status {
        /**
         * 未加载，或者加载结束
         */
        NOT_LOADED,

        /**
         * 正在加载
         */
        LOADING,

        /**
         * 加载失败
         */
        FAILED;
    }
}
