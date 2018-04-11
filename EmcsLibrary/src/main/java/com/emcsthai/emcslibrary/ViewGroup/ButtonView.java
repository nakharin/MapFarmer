package com.emcsthai.emcslibrary.ViewGroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class ButtonView extends BaseCustomViewGroup {

    private FrameLayout frameLayout;

    private LinearLayout linearLayout;

    private RelativeLayout relativeLayout;

    private ImageView imageView;

    private View view;

    private TextView txtTextMessage;
    private TextView txtErrorMessage;

    private ProgressBar progressBar;

    private View.OnClickListener mOnClickListener;

    private String strTextMessage;

    private Drawable drawableLeft;

    private int colorTextMessage = Color.WHITE;

    private Boolean isShowDrawable = true;

    private int textMessageSize;

    private Drawable drawableBackground;

    private int padding;

    public ButtonView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public ButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public ButtonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_button, this);
    }

    private void initInstances() {
        // findViewById here
        frameLayout = (FrameLayout) findViewById(R.id.button_view_frame_layout);
        linearLayout = (LinearLayout) findViewById(R.id.button_view_linear_layout);
        relativeLayout = (RelativeLayout) findViewById(R.id.button_view_relative_layout);
        imageView = (ImageView) findViewById(R.id.button_view_image_view);
        view = findViewById(R.id.button_view_view);
        txtTextMessage = (TextView) findViewById(R.id.button_view_txt_text_message);
        progressBar = (ProgressBar) findViewById(R.id.button_view_progress_bar);
        txtErrorMessage = (TextView) findViewById(R.id.button_view_txt_error_message);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ButtonView,
                defStyleAttr, defStyleRes);

        strTextMessage = a.getString(
                R.styleable.ButtonView_em_btn_text);

        if (a.hasValue(R.styleable.ButtonView_em_btn_background)) {
            drawableBackground = a.getDrawable(R.styleable.ButtonView_em_btn_background);
        }

        if (a.hasValue(R.styleable.ButtonView_em_drawable_left)) {
            drawableLeft = a.getDrawable(
                    R.styleable.ButtonView_em_drawable_left);
            drawableLeft.setCallback(this);
        }

        colorTextMessage = a.getColor(R.styleable.ButtonView_em_btn_color, colorTextMessage);

        isShowDrawable = a.getBoolean(R.styleable.ButtonView_em_is_show_drawable, true);

        textMessageSize = a.getDimensionPixelSize(R.styleable.ButtonView_em_txt_size, 12);

        padding = a.getDimensionPixelSize(R.styleable.ButtonView_em_btn_padding, 0);

        try {

        } finally {
            a.recycle();
        }

        // Method from this class
        invalidates();
    }

    private void invalidates() {

        int paddingPixel = padding;
        float density = getContext().getResources().getDisplayMetrics().density;
        int paddingDp = (int) (paddingPixel * density);
        relativeLayout.setPadding(0, paddingDp, 0, 0);

        txtTextMessage.setText(strTextMessage);
        txtTextMessage.setTextColor(colorTextMessage);
        txtTextMessage.setTextSize(textMessageSize);

        if (drawableLeft != null) {
            imageView.setImageDrawable(drawableLeft);
        } else {
            imageView.setImageResource(R.drawable.ic_reg);
        }

        if (isShowDrawable) {
            imageView.setVisibility(VISIBLE);
            view.setVisibility(VISIBLE);
        } else {
            imageView.setVisibility(GONE);
            view.setVisibility(GONE);
        }

        if (drawableBackground != null) {
            relativeLayout.setBackground(drawableBackground);
        }
    }

    public void setText(String text) {
        txtTextMessage.setText(text);
    }

//    public void setBackground(Drawable drawable) {
//        if (drawable != null) {
//            relativeLayout.setBackground(drawable);
//        }
//    }

    public void showLoading() {

        imageView.setVisibility(GONE);
        view.setVisibility(GONE);
        txtTextMessage.setVisibility(GONE);

        progressBar.setVisibility(VISIBLE);

        relativeLayout.setBackground(drawableBackground);
        relativeLayout.setEnabled(false);

        // Method from this class
        hideError();
    }

    public void cancelLoading() {
        progressBar.setVisibility(GONE);
        txtTextMessage.setVisibility(VISIBLE);
        if (isShowDrawable) {
            imageView.setVisibility(VISIBLE);
            view.setVisibility(VISIBLE);
        }
    }

    public void hideLoading(@NonNull final Response response,
                            @Nullable String textError,
                            @Nullable String btnTextResponse,
                            @Nullable final DelayCallback delayCallback) {

        switch (response) {

            case SUCCESS:
                YoYo.with(Techniques.ZoomIn).duration(200).playOn(txtTextMessage);
                Drawable drawableSuccess = ContextCompat.getDrawable(getContext(), R.drawable.btn_selector_green);
                relativeLayout.setBackground(drawableSuccess);
                txtTextMessage.setText(btnTextResponse);

                setDelay(1.5, new DelayCallback() {
                    @Override
                    public void afterDelay() {
                        Drawable drawableReClick = ContextCompat.getDrawable(getContext(), R.drawable.btn_selector_blue);
                        relativeLayout.setBackground(drawableReClick);

                        if (delayCallback != null) {
                            delayCallback.afterDelay();
                        }
                    }
                });

                break;
            case FAILED:
                YoYo.with(Techniques.ZoomIn).duration(200).playOn(txtTextMessage);
                Drawable drawableFailed = ContextCompat.getDrawable(getContext(), R.drawable.btn_selector_red);
                relativeLayout.setBackground(drawableFailed);
                txtTextMessage.setText(btnTextResponse);

                setDelay(2, new DelayCallback() {
                    @Override
                    public void afterDelay() {
                        if (response == Response.FAILED) {
                            YoYo.with(Techniques.ZoomIn).duration(200).playOn(txtTextMessage);
                            txtTextMessage.setText(R.string.str_try_again);
                        }
                    }
                });

                setDelay(2, new DelayCallback() {
                    @Override
                    public void afterDelay() {
                        if (response == Response.FAILED) {
                            Drawable drawableReClick = ContextCompat.getDrawable(getContext(), R.drawable.btn_selector_blue);
                            relativeLayout.setBackground(drawableReClick);
                        }

                        if (delayCallback != null) {
                            delayCallback.afterDelay();
                        }
                    }
                });

                break;
        }

        if (isShowDrawable) {
            imageView.setVisibility(VISIBLE);
            view.setVisibility(VISIBLE);
            txtTextMessage.setVisibility(VISIBLE);

            progressBar.setVisibility(GONE);
        } else {
            txtTextMessage.setVisibility(VISIBLE);

            progressBar.setVisibility(GONE);
        }

        if (textError != null && !textError.equals("")) {
            // Method from this class
            showError(textError);
        } else {
            // Method from this class
            hideError();
        }

        relativeLayout.setEnabled(true);
    }

    public void showError(String text) {

        txtErrorMessage.setText(text);
        txtErrorMessage.setVisibility(VISIBLE);

        YoYo.with(Techniques.ZoomIn).duration(200).playOn(txtErrorMessage);
    }

    public void hideError() {
        txtErrorMessage.setVisibility(GONE);
    }

    public void setDelay(double secs, final DelayCallback delayCallback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (delayCallback != null) {
                    delayCallback.afterDelay();
                }
            }
        }, (long) (secs * 1000)); // afterDelay will be executed after (secs*1000) milliseconds.
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        mOnClickListener = onClickListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mOnClickListener != null;
    }

    /*******************************************************************
     * *************************** Enum ***************************
     *******************************************************************/

    public enum Response {
        SUCCESS, FAILED
    }

    /*******************************************************************
     * *************************** Interface ***************************
     *******************************************************************/

    public interface DelayCallback {
        void afterDelay();
    }
}
