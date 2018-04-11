package com.emcsthai.emcslibrary.ViewGroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SelectView extends BaseCustomViewGroup {

    private LinearLayout llModeText;
    private LinearLayout llModeImage;

    private FrameLayout frameImageCenter;

    private Button btnSelectLeft;
    private Button btnSelectCenter;
    private Button btnSelectRight;

    private String strButtonLeft = "";
    private String strButtonCenter = "";
    private String strButtonRight = "";

    private ImageView imgSelectLeft;
    private ImageView imgSelectCenter;
    private ImageView imgSelectRight;

    private Drawable srcImageLeft = null;
    private Drawable srcImageCenter = null;
    private Drawable srcImageRight = null;

    private TextView txtBelowLeft;
    private TextView txtBelowCenter;
    private TextView txtBelowRight;

    private String strBelowLeft = "";
    private String strBelowCenter = "";
    private String strBelowRight = "";

    private int txtColorLeft = ContextCompat.getColor(getContext(), R.color.colorWhite);
    private int txtColorCenter = ContextCompat.getColor(getContext(), R.color.colorPrimary);
    private int txtColorRight = ContextCompat.getColor(getContext(), R.color.colorPrimary);

    private OnItemClickListener onItemClickListener;

    private boolean isDisable = false;

    private Side SIDE = Side.LEFT;

    private Mode MODE = Mode.TEXT;

    private Column COLUMN = Column.TWO;

    /**
     * Do not use
     */
    protected SelectView mView;

    public SelectView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public SelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public SelectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_select, this);
    }

    private void initInstances() {
        // findViewById here

        llModeText = (LinearLayout) findViewById(R.id.select_view_linear_mode_text);
        llModeImage = (LinearLayout) findViewById(R.id.select_view_linear_mode_image);

        frameImageCenter = (FrameLayout) findViewById(R.id.select_view_frame_center);

        btnSelectLeft = (Button) findViewById(R.id.select_view_btn_left);
        btnSelectCenter = (Button) findViewById(R.id.select_view_btn_center);
        btnSelectRight = (Button) findViewById(R.id.select_view_btn_right);

        imgSelectLeft = (ImageView) findViewById(R.id.select_view_img_left);
        imgSelectCenter = (ImageView) findViewById(R.id.select_view_img_center);
        imgSelectRight = (ImageView) findViewById(R.id.select_view_img_right);

        txtBelowLeft = (TextView) findViewById(R.id.select_view_txt_left);
        txtBelowCenter = (TextView) findViewById(R.id.select_view_txt_center);
        txtBelowRight = (TextView) findViewById(R.id.select_view_txt_right);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SelectView,
                defStyleAttr, defStyleRes);

        if (a.hasValue(R.styleable.SelectView_em_sv_disable)) {
            isDisable = a.getBoolean(R.styleable.SelectView_em_sv_disable, false);
        }

        if (a.hasValue(R.styleable.SelectView_em_sv_mode)) {
            MODE = SelectView.Mode.fromId(a.getInt(R.styleable.SelectView_em_sv_mode, 0));
        }

        if (a.hasValue(R.styleable.SelectView_em_sv_column)) {
            COLUMN = SelectView.Column.fromId(a.getInt(R.styleable.SelectView_em_sv_column, 0));
        }

        strButtonLeft = a.getString(R.styleable.SelectView_em_txt_left);
        strButtonCenter = a.getString(R.styleable.SelectView_em_txt_center);
        strButtonRight = a.getString(R.styleable.SelectView_em_txt_right);

        txtColorLeft = a.getColor(R.styleable.SelectView_em_txt_color_left, txtColorLeft);
        txtColorCenter = a.getColor(R.styleable.SelectView_em_txt_color_center, txtColorCenter);
        txtColorRight = a.getColor(R.styleable.SelectView_em_txt_color_right, txtColorRight);

        if (a.hasValue(R.styleable.SelectView_em_sv_src_left)) {
            srcImageLeft = a.getDrawable(R.styleable.SelectView_em_sv_src_left);
        }

        if (a.hasValue(R.styleable.SelectView_em_sv_src_center)) {
            srcImageCenter = a.getDrawable(R.styleable.SelectView_em_sv_src_center);
        }

        if (a.hasValue(R.styleable.SelectView_em_sv_src_right)) {
            srcImageRight = a.getDrawable(R.styleable.SelectView_em_sv_src_right);
        }

        strBelowLeft = a.getString(R.styleable.SelectView_em_sv_txt_below_left);
        strBelowCenter = a.getString(R.styleable.SelectView_em_sv_txt_below_center);
        strBelowRight = a.getString(R.styleable.SelectView_em_sv_txt_below_right);

        try {

        } finally {
            a.recycle();
        }

        // Method from this class
        invalidates();
    }

    private void invalidates() {

        switch (MODE) {

            case TEXT:
                llModeImage.setVisibility(GONE);
                llModeText.setVisibility(VISIBLE);
                btnSelectLeft.setOnTouchListener(onButtonTouchListener);
                btnSelectCenter.setOnTouchListener(onButtonTouchListener);
                btnSelectRight.setOnTouchListener(onButtonTouchListener);
                break;
            case IMAGE:
                llModeImage.setVisibility(VISIBLE);
                llModeText.setVisibility(GONE);
                imgSelectLeft.setOnTouchListener(onImageTouchListener);
                imgSelectCenter.setOnTouchListener(onImageTouchListener);
                imgSelectRight.setOnTouchListener(onImageTouchListener);
                break;
        }

        switch (COLUMN) {

            case TWO:
                btnSelectCenter.setVisibility(GONE);
                frameImageCenter.setVisibility(GONE);
                break;
            case THREE:
                btnSelectCenter.setVisibility(VISIBLE);
                frameImageCenter.setVisibility(VISIBLE);
                break;
        }

        btnSelectLeft.setText(strButtonLeft);
        btnSelectCenter.setText(strButtonCenter);
        btnSelectRight.setText(strButtonRight);

        btnSelectLeft.setTextColor(txtColorLeft);
        btnSelectCenter.setTextColor(txtColorCenter);
        btnSelectRight.setTextColor(txtColorRight);

        if (srcImageLeft != null) {
            imgSelectLeft.setImageDrawable(srcImageLeft);
        }

        if (srcImageCenter != null) {
            imgSelectCenter.setImageDrawable(srcImageCenter);
        }

        if (srcImageRight != null) {
            imgSelectRight.setImageDrawable(srcImageRight);
        }

        txtBelowLeft.setText(strBelowLeft);
        txtBelowCenter.setText(strBelowCenter);
        txtBelowRight.setText(strBelowRight);

        if (isDisable) {

            btnSelectLeft.setOnTouchListener(null);
            btnSelectCenter.setOnTouchListener(null);
            btnSelectRight.setOnTouchListener(null);
            imgSelectLeft.setOnTouchListener(null);
            imgSelectCenter.setOnTouchListener(null);
            imgSelectRight.setOnTouchListener(null);

            // Method from this class
            reloadSide();
        }
    }

    /*******************************************************************
     * **************************** Method *****************************
     *******************************************************************/

    public void setDisable(boolean isDisable) {
        this.isDisable = isDisable;

        // Method from this class
        invalidates();
    }

    public Side getSide() {
        return SIDE;
    }

    public void setSide(Side side) {

        switch (MODE) {
            case TEXT:
                switch (side) {
                    case LEFT:
                        // Method from this class
                        setTextLeftClickPressed();
                        break;
                    case CENTER:
                        // Method from this class
                        setTextCenterClickPressed();
                        break;
                    case RIGHT:
                        // Method from this class
                        setTextRightClickPressed();
                        break;
                }
                break;
            case IMAGE:
                switch (side) {
                    case LEFT:
                        // Method from this class
                        setImageLeftClickPressed();
                        break;
                    case CENTER:
                        // Method from this class
                        setImageCenterClickPressed();
                        break;
                    case RIGHT:
                        // Method from this class
                        setImageRightClickPressed();
                        break;
                }
                break;
        }
    }

    public void setOnItemClickListener(SelectView view, OnItemClickListener onItemClickListener) {
        this.mView = view;
        this.onItemClickListener = onItemClickListener;
    }

    private void reloadSide() {
        switch (SIDE) {
            case LEFT:
                if (isDisable) {
                    // Method from this class
                    setTextLeftClickPressedDisable();
                } else {
                    // Method from this class
                    setTextLeftClickPressed();
                }
                break;
            case CENTER:
                if (isDisable) {
                    // Method from this class
                    setTextCenterClickPressedDisable();
                } else {
                    // Method from this class
                    setTextCenterClickPressed();
                }
                break;
            case RIGHT:
                if (isDisable) {
                    // Method from this class
                    setTextRightClickPressedDisable();
                } else {
                    // Method from this class
                    setTextRightClickPressed();
                }
                break;
        }
    }

    public void setTextButton(String left, String center, String right) {

        Log.i("setTextButton", "SIDE : " + SIDE.name());

        strButtonLeft = left;
        strButtonCenter = center;
        strButtonRight = right;

        invalidates();
    }

    public void setDisable() {
        llModeText.setEnabled(true);
        llModeImage.setEnabled(true);
    }

    public void setDisableBtnRight() {
        btnSelectRight.setEnabled(true);
    }

    /********************************
     * Button
     ****************************/

    // Left
    private void setTextLeftClickNormal() {
        txtColorLeft = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        btnSelectLeft.setTextColor(txtColorLeft);
        btnSelectLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_left_normal));
    }

    private void setTextLeftClickNormalDisable() {
        txtColorLeft = ContextCompat.getColor(getContext(), R.color.colorGrayLV2);
        btnSelectLeft.setTextColor(txtColorLeft);
        btnSelectLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_left_normal_disable));
    }

    private void setTextLeftClickFocused() {
        txtColorLeft = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        btnSelectLeft.setTextColor(txtColorLeft);
        btnSelectLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_left_focused));
    }

    private void setTextLeftClickPressed() {
        txtColorLeft = ContextCompat.getColor(getContext(), R.color.colorWhite);
        btnSelectLeft.setTextColor(txtColorLeft);
        btnSelectLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_left_pressed));
        // Method from this class
        setTextCenterClickNormal();
        // Method from this class
        setTextRightClickNormal();
    }

    private void setTextLeftClickPressedDisable() {
        txtColorLeft = ContextCompat.getColor(getContext(), R.color.colorWhite);
        btnSelectLeft.setTextColor(txtColorLeft);
        btnSelectLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_left_pressed_disable));
        // Method from this class
        setTextCenterClickNormalDisable();
        // Method from this class
        setTextRightClickNormalDisable();
    }

    // Center
    private void setTextCenterClickNormal() {
        txtColorCenter = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        btnSelectCenter.setTextColor(txtColorCenter);
        btnSelectCenter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_center_normal));
    }

    private void setTextCenterClickNormalDisable() {
        txtColorCenter = ContextCompat.getColor(getContext(), R.color.colorGrayLV2);
        btnSelectCenter.setTextColor(txtColorCenter);
        btnSelectCenter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_center_normal_disable));
    }

    private void setTextCenterClickFocused() {
        txtColorCenter = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        btnSelectCenter.setTextColor(txtColorCenter);
        btnSelectCenter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_center_focused));
    }

    private void setTextCenterClickPressed() {
        txtColorCenter = ContextCompat.getColor(getContext(), R.color.colorWhite);
        btnSelectCenter.setTextColor(txtColorCenter);
        btnSelectCenter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_center_pressed));
        // Method from this class
        setTextLeftClickNormal();
        // Method from this class
        setTextRightClickNormal();
    }

    private void setTextCenterClickPressedDisable() {
        txtColorCenter = ContextCompat.getColor(getContext(), R.color.colorWhite);
        btnSelectCenter.setTextColor(txtColorCenter);
        btnSelectCenter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_center_pressed_disable));
        // Method from this class
        setTextLeftClickNormalDisable();
        // Method from this class
        setTextRightClickNormalDisable();
    }

    // Right
    private void setTextRightClickNormal() {
        txtColorRight = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        btnSelectRight.setTextColor(txtColorRight);
        btnSelectRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_right_normal));
    }

    private void setTextRightClickNormalDisable() {
        txtColorRight = ContextCompat.getColor(getContext(), R.color.colorGrayLV2);
        btnSelectRight.setTextColor(txtColorRight);
        btnSelectRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_right_normal_disable));
    }

    private void setTextRightClickFocused() {
        txtColorRight = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        btnSelectRight.setTextColor(txtColorRight);
        btnSelectRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_right_focused));
    }

    private void setTextRightClickPressed() {
        txtColorRight = ContextCompat.getColor(getContext(), R.color.colorWhite);
        btnSelectRight.setTextColor(txtColorRight);
        btnSelectRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_right_pressed));
        // Method from this class
        setTextLeftClickNormal();
        // Method from this class
        setTextCenterClickNormal();
    }

    private void setTextRightClickPressedDisable() {
        txtColorRight = ContextCompat.getColor(getContext(), R.color.colorWhite);
        btnSelectRight.setTextColor(txtColorRight);
        btnSelectRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_right_pressed_disable));
        // Method from this class
        setTextLeftClickNormalDisable();
        // Method from this class
        setTextCenterClickNormalDisable();
    }

    /****************************
     * ImageView
     *****************************/

    // Left
    private void setImageLeftClickNormal() {
        imgSelectLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_left_normal));
    }

    private void setImageLeftClickFocused() {
        imgSelectLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_left_focused));
    }

    private void setImageLeftClickPressed() {
        imgSelectLeft.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_left_pressed));
        // Method from this class
        setImageCenterClickNormal();
        // Method from this class
        setImageRightClickNormal();
    }

    // Center

    private void setImageCenterClickNormal() {
        imgSelectCenter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_center_normal));
    }

    private void setImageCenterClickFocused() {
        imgSelectCenter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_center_focused));
    }

    private void setImageCenterClickPressed() {
        imgSelectCenter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_center_pressed));
        // Method from this class
        setImageLeftClickNormal();
        // Method from this class
        setImageRightClickNormal();
    }

    // Right

    private void setImageRightClickNormal() {
        imgSelectRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_right_normal));
    }

    private void setImageRightClickFocused() {
        imgSelectRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_right_focused));
    }

    private void setImageRightClickPressed() {
        imgSelectRight.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_select_image_right_pressed));
        // Method from this class
        setImageLeftClickNormal();
        // Method from this class
        setImageCenterClickNormal();
    }

    /*******************************************************************
     * ************************** Listener *****************************
     *******************************************************************/

    private final View.OnTouchListener onButtonTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP: // Pressed
                    if (v == btnSelectLeft) {
                        if (onItemClickListener != null) {
                            SIDE = Side.LEFT;
                            onItemClickListener.onItemClick(mView, MODE, SIDE);
                            // Method from this class
                            setTextLeftClickPressed();
                        }
                    }

                    if (v == btnSelectCenter) {
                        if (onItemClickListener != null) {
                            SIDE = Side.CENTER;
                            onItemClickListener.onItemClick(mView, MODE, SIDE);
                            // Method from this class
                            setTextCenterClickPressed();
                        }
                    }

                    if (v == btnSelectRight) {
                        if (onItemClickListener != null) {
                            SIDE = Side.RIGHT;
                            onItemClickListener.onItemClick(mView, MODE, SIDE);
                            // Method from this class
                            setTextRightClickPressed();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_DOWN: // focused
                    if (v == btnSelectLeft) {
                        if (SIDE != Side.LEFT) {
                            // Method from this class
                            setTextLeftClickFocused();
                        }
                    }

                    if (v == btnSelectCenter) {
                        if (SIDE != Side.CENTER) {
                            // Method from this class
                            setTextCenterClickFocused();
                        }
                    }

                    if (v == btnSelectRight) {
                        if (SIDE != Side.RIGHT) {
                            // Method from this class
                            setTextRightClickFocused();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    if (v == btnSelectLeft) {
                        if (SIDE != Side.LEFT) {
                            // Method from this class
                            setTextLeftClickNormal();
                        }
                    }

                    if (v == btnSelectCenter) {
                        if (SIDE != Side.CENTER) {
                            // Method from this class
                            setTextCenterClickNormal();
                        }
                    }

                    if (v == btnSelectRight) {
                        if (SIDE != Side.RIGHT) {
                            // Method from this class
                            setTextRightClickNormal();
                        }
                    }
            }

            return false;
        }
    };

    private final View.OnTouchListener onImageTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP: // Pressed
                    if (v == imgSelectLeft) {
                        if (onItemClickListener != null) {
                            SIDE = Side.LEFT;
                            onItemClickListener.onItemClick(mView, MODE, SIDE);
                            // Method from this class
                            setImageLeftClickPressed();
                        }
                    }

                    if (v == imgSelectCenter) {
                        if (onItemClickListener != null) {
                            SIDE = Side.CENTER;
                            onItemClickListener.onItemClick(mView, MODE, SIDE);
                            // Method from this class
                            setImageCenterClickPressed();
                        }
                    }

                    if (v == imgSelectRight) {
                        if (onItemClickListener != null) {
                            SIDE = Side.RIGHT;
                            onItemClickListener.onItemClick(mView, MODE, SIDE);
                            // Method from this class
                            setImageRightClickPressed();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_DOWN: // focused
                    if (v == imgSelectLeft) {
                        if (SIDE != Side.LEFT) {
                            // Method from this class
                            setImageLeftClickFocused();
                        }
                    }

                    if (v == imgSelectCenter) {
                        if (SIDE != Side.CENTER) {
                            // Method from this class
                            setImageCenterClickFocused();
                        }
                    }

                    if (v == imgSelectRight) {
                        if (SIDE != Side.RIGHT) {
                            // Method from this class
                            setImageRightClickFocused();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    if (v == imgSelectLeft) {
                        if (SIDE != Side.LEFT) {
                            // Method from this class
                            setImageLeftClickNormal();
                        }
                    }

                    if (v == imgSelectCenter) {
                        if (SIDE != Side.CENTER) {
                            // Method from this class
                            setImageCenterClickNormal();
                        }
                    }

                    if (v == imgSelectRight) {
                        if (SIDE != Side.RIGHT) {
                            // Method from this class
                            setImageRightClickNormal();
                        }
                    }
            }

            return false;
        }
    };

    /*******************************************************************
     * *********************** Inner Class *****************************
     *******************************************************************/

    public enum Side {
        LEFT, CENTER, RIGHT
    }

    public enum Mode {
        TEXT(1),
        IMAGE(2);

        int id;

        Mode(int id) {
            this.id = id;
        }

        static Mode fromId(int id) {
            for (Mode mode : values()) {
                if (mode.id == id) {
                    return mode;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Column {
        TWO(1),
        THREE(2);

        int id;

        Column(int id) {
            this.id = id;
        }

        static Column fromId(int id) {
            for (Column column : values()) {
                if (column.id == id) {
                    return column;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    /*******************************************************************
     * ********************** Interface Class **************************
     *******************************************************************/

    public interface OnItemClickListener {
        void onItemClick(SelectView view, Mode mode, Side side);
    }
}
