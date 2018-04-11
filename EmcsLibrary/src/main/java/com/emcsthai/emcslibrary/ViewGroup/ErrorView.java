package com.emcsthai.emcslibrary.ViewGroup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;

/**
 * Created by nakarin on 9/29/2016 AD.
 */

public class ErrorView extends BaseCustomViewGroup {

    private FrameLayout errorView;

    private ImageView imgError;

    private TextView txtErrorMessage;

    private Button btnTryAgain;

    public ErrorView(Context context) {
        super(context);
        // Method from this class
        initInflate();
        // Method from this class
        initInstance();
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Method from this class
        initInflate();
        // Method from this class
        initInstance();
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Method from this class
        initInflate();
        // Method from this class
        initInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // Method from this class
        initInflate();
        // Method from this class
        initInstance();
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_error, this);
    }

    private void initInstance() {

        errorView = (FrameLayout) findViewById(R.id.error_view_frame_layout);

        imgError = (ImageView) findViewById(R.id.error_view_img_error);

        txtErrorMessage = (TextView) findViewById(R.id.error_view_txt_error_message);

        btnTryAgain = (Button) findViewById(R.id.error_view_btn_try_again);
    }

    public void showTextOnly(ErrorView errorView) {
        errorView.setVisibility(VISIBLE);
        txtErrorMessage.setVisibility(VISIBLE);
        imgError.setVisibility(GONE);
        btnTryAgain.setVisibility(GONE);
    }

    public void showImageOnly(ErrorView errorView) {
        errorView.setVisibility(VISIBLE);
        imgError.setVisibility(VISIBLE);
        txtErrorMessage.setVisibility(GONE);
        btnTryAgain.setVisibility(GONE);
    }

    public void showAll(ErrorView errorView) {
        errorView.setVisibility(VISIBLE);
    }

    public void hide(ErrorView errorView) {
        errorView.setVisibility(GONE);
    }

    public ImageView getImageView() {
        return imgError;
    }

    public TextView getTextView() {
        return txtErrorMessage;
    }

    public Button getButton() {
        return btnTryAgain;
    }

    public ErrorView setErrorMessage(@StringRes int strResId) {
        txtErrorMessage.setText(strResId);
        return this;
    }

    public ErrorView setErrorMessage(String string) {
        txtErrorMessage.setText(string);
        return this;
    }

    public ErrorView setImageError(@DrawableRes int resId) {
        imgError.setImageResource(resId);
        return this;
    }

    public ErrorView setImageError(@Nullable Drawable drawable) {
        imgError.setImageDrawable(drawable);
        return this;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        btnTryAgain.setOnClickListener(onClickListener);
    }
}
