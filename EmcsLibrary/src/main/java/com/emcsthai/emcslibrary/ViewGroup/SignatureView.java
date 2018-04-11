package com.emcsthai.emcslibrary.ViewGroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;

import java.io.File;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SignatureView extends BaseCustomViewGroup {

    private TextView txtSignatureViewTitle;

    private ImageView imgSignatureViewRequire;

    private FrameLayout frameSignatureViewLayout2;

    private ImageView imgSignatureViewSignature;

    private String strTitle = "";

    private boolean isDisable = false;
    private boolean isRequireField = false;

    private ClickState clickState = ClickState.NORMAL;

    private OnSignatureClickListener onSignatureClickListener;

    /**
     * Do not use
     */
    protected SignatureView mView;

    public SignatureView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_signature, this);
    }

    private void initInstances() {
        // findViewById here
        txtSignatureViewTitle = (TextView) findViewById(R.id.txt_signature_view_title);

        imgSignatureViewRequire = (ImageView) findViewById(R.id.img_signature_view_require);

        frameSignatureViewLayout2 = (FrameLayout) findViewById(R.id.frame_signature_view_layout_2);

        imgSignatureViewSignature = (ImageView) findViewById(R.id.img_signature_view_signature);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SignatureView,
                defStyleAttr, defStyleRes);

        if (a.hasValue(R.styleable.SignatureView_em_sign_title)) {
            strTitle = a.getString(R.styleable.SignatureView_em_sign_title);
        }

        if (a.hasValue(R.styleable.SignatureView_em_sign_disable)) {
            isDisable = a.getBoolean(R.styleable.SignatureView_em_sign_disable, false);
        }

        if (a.hasValue(R.styleable.SignatureView_em_sign_require_field)) {
            isRequireField = a.getBoolean(R.styleable.SignatureView_em_sign_require_field, false);
        }

        try {

        } finally {
            a.recycle();
        }

        // Method from this class
        invalidates();
    }

    private void invalidates() {

        if (!strTitle.equals("")) {
            txtSignatureViewTitle.setText(strTitle);
        }

        if (isRequireField) {
            imgSignatureViewRequire.setVisibility(VISIBLE);
        } else {
            imgSignatureViewRequire.setVisibility(GONE);
        }

        if (isDisable) {
            imgSignatureViewSignature.setEnabled(false);
            // Method from this class
            setBackgroundDisable();
            frameSignatureViewLayout2.setOnTouchListener(null);
        } else {
            imgSignatureViewSignature.setEnabled(true);
            // Method from this class
            setBackgroundNormal();
            frameSignatureViewLayout2.setOnTouchListener(onTouchListener);
        }
    }

    public ImageView getImageView() {
        return imgSignatureViewSignature;
    }

    public void load(byte[] bytes) {
        Glide.with(getContext()).load(bytes).into(imgSignatureViewSignature);
    }

    public void load(Bitmap bitmap) {
        Glide.with(getContext()).load(bitmap).into(imgSignatureViewSignature);
    }

    public void load(File file) {
        Glide.with(getContext()).load(file).into(imgSignatureViewSignature);
    }

    public void setError() {
        setBackgroundError();
    }

    private void setBackgroundNormal() {
        frameSignatureViewLayout2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_signature_normal));
        clickState = ClickState.NORMAL;
    }

    private void setBackgroundDisable() {
        frameSignatureViewLayout2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_signature_disable));
        clickState = ClickState.DISABLE;
    }

    private void setBackgroundFocused() {
        frameSignatureViewLayout2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_signature_focused));
    }

    private void setBackgroundError() {
        frameSignatureViewLayout2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_signature_error));
        clickState = ClickState.ERROR;
    }

    private void setBackgroundErrorFocused() {
        frameSignatureViewLayout2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_signature_error_focused));
    }

    public void setOnSignatureClickListener(SignatureView view, OnSignatureClickListener onSignatureClickListener) {
        this.mView = view;
        this.onSignatureClickListener = onSignatureClickListener;
    }


    /*******************************************************************
     * *************************** Listener ****************************
     *******************************************************************/

    private final OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (onSignatureClickListener != null) {
                        onSignatureClickListener.onClick(mView);
                        if (clickState == ClickState.NORMAL) {
                            setBackgroundNormal();
                        } else {
                            setBackgroundError();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_DOWN:
                    if (clickState == ClickState.NORMAL) {
                        setBackgroundFocused();
                    } else {
                        setBackgroundErrorFocused();
                    }
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    if (clickState == ClickState.NORMAL) {
                        setBackgroundNormal();
                    } else {
                        setBackgroundError();
                    }
                    return true;
            }

            return false;
        }
    };

    /*******************************************************************
     * ************************* Enum Class ****************************
     *******************************************************************/

    public enum ClickState {
        NORMAL, ERROR, DISABLE
    }

    /*******************************************************************
     * ********************** Interface Class **************************
     *******************************************************************/

    public interface OnSignatureClickListener {
        void onClick(SignatureView view);
    }
}
