package com.emcsthai.emcslibrary.ViewGroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class LoadingView extends BaseCustomViewGroup {

    private FrameLayout loadingViewFrame;

    private LinearLayout loadingViewLinear;

    private ProgressBar progressBar;

    private TextView txtLoading;

    private boolean isBackGroundMap = false;

    private int backGroundColor = 0;

    public LoadingView(Context context) {
        super(context);
        // Method from this class
        initInflate();
        // Method from this class
        initInstance();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Method from this class
        initInflate();
        // Method from this class
        initInstance();
        // Method from this class
        initWithAttrs(attrs, 0, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Method from this class
        initInflate();
        // Method from this class
        initInstance();
        // Method from this class
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // Method from this class
        initInflate();
        // Method from this class
        initInstance();
        // Method from this class
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_loading, this);
    }

    private void initInstance() {
        loadingViewFrame = (FrameLayout) findViewById(R.id.loading_view_frame_layout);
        loadingViewLinear = (LinearLayout) findViewById(R.id.loading_view_linear_layout);
        progressBar = (ProgressBar) findViewById(R.id.loading_view_progress_bar);
        txtLoading = (TextView) findViewById(R.id.loading_view_txt_loading);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LoadingView,
                defStyleAttr, defStyleRes);

        if (a.hasValue(R.styleable.LoadingView_em_bg_color)) {
            backGroundColor = a.getColor(R.styleable.LoadingView_em_bg_color, ContextCompat.getColor(getContext(), R.color.colorGrayLV6));
        }

        isBackGroundMap = a.getBoolean(R.styleable.LoadingView_em_is_bg_map, false);

        try {

        } finally {
            a.recycle();
        }

        // Method from this class
        invalidates();
    }

    private void invalidates() {
        if (isBackGroundMap) {
            loadingViewFrame.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackgroundMap));
        } else {
            loadingViewFrame.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGrayLV6));
        }

        if (backGroundColor != 0) {
            loadingViewFrame.setBackgroundColor(backGroundColor);
        }
    }

    public void setProgressMessage(String str) {
        txtLoading.setText(str);
    }

    public void setProgressMessage(@StringRes int strResId) {
        txtLoading.setText(strResId);
    }
}
