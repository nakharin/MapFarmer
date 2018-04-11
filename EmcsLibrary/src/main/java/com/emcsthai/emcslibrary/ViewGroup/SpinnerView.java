package com.emcsthai.emcslibrary.ViewGroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.emcsthai.emcslibrary.Model.Utils.EMCSUtils;
import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;

import java.util.LinkedHashMap;

import timber.log.Timber;

/**
 * Created by nakharin on 11/16/2014.
 */
public class SpinnerView extends BaseCustomViewGroup {

    /*******************************************************************
     * ************************* Variable ******************************
     *******************************************************************/

    private FrameLayout frameSpinnerViewLayout;

    private LinearLayout linearSpinnerViewLayout;

    private RelativeLayout relativeSpinnerViewLayout;

    private TextView txtSpinnerViewTitle;

    private ImageView imgSpinnerViewRequire;

    private RelativeLayout relativeSpinnerViewLayout2;

    private Spinner spnSpinnerView;

    private ImageView imgSpinnerViewArrow;

    private TextView txtSpinnerViewError;

    private String strTitle = "";
    private String strError = "";

    private Boolean isDisable = false;
    private Boolean isRequireField = false;

    private ClickState clickState = ClickState.NORMAL;

    private LinkedHashMap<String, String> mLinkedHashMap = new LinkedHashMap<>();

    private OnSpinnerItemSelectedListener onSpinnerItemSelectedListener;

    /**
     * Do not use
     */
    protected SpinnerView mView;

    public SpinnerView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    /*******************************************************************
     * *********************** Init Method *****************************
     *******************************************************************/

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_spinner, this);
    }

    private void initInstances() {
        // findViewById here
        frameSpinnerViewLayout = (FrameLayout) findViewById(R.id.spinner_view_frame_layout);

        linearSpinnerViewLayout = (LinearLayout) findViewById(R.id.spinner_view_linear_layout);

        relativeSpinnerViewLayout = (RelativeLayout) findViewById(R.id.spinner_view_relative_layout);

        txtSpinnerViewTitle = (TextView) findViewById(R.id.spinner_view_txt_title);

        imgSpinnerViewRequire = (ImageView) findViewById(R.id.spinner_view_img_require);

        relativeSpinnerViewLayout2 = (RelativeLayout) findViewById(R.id.spinner_view_relative_layout_2);

        spnSpinnerView = (Spinner) findViewById(R.id.spinner_view_spn);

        imgSpinnerViewArrow = (ImageView) findViewById(R.id.spinner_view_img_arrow);

        txtSpinnerViewError = (TextView) findViewById(R.id.spinner_view_txt_error);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SpinnerView,
                defStyleAttr, defStyleRes);

        try {

            if (a.hasValue(R.styleable.SpinnerView_em_spn_title)) {
                strTitle = a.getString(R.styleable.SpinnerView_em_spn_title);
            }

            if (a.hasValue(R.styleable.SpinnerView_em_spn_require_field)) {
                isRequireField = a.getBoolean(R.styleable.SpinnerView_em_spn_require_field, false);
            }

            if (a.hasValue(R.styleable.SpinnerView_em_spn_disable)) {
                isDisable = a.getBoolean(R.styleable.SpinnerView_em_spn_disable, false);
            }

        } finally {
            a.recycle();
        }

        // Method from this class
        invalidates();
    }

    private void invalidates() {

        if (!strTitle.equals("")) {
            txtSpinnerViewTitle.setText(strTitle);
        }

        if (isRequireField) {
            imgSpinnerViewRequire.setVisibility(VISIBLE);
        } else {
            imgSpinnerViewRequire.setVisibility(GONE);
        }

        if (isDisable) {
            spnSpinnerView.setEnabled(false);
            // Method from this class
            setBackgroundDisable();
        } else {
            spnSpinnerView.setEnabled(true);
            spnSpinnerView.setOnItemSelectedListener(onItemSelectedListener);
            // Method from this class
            setBackgroundNormal();
        }

        /**
         * SetUp Default Spinner
         */
//        LinkedHashMap<String, String> linkedHashMap =
//                EMCSUtils.getInstance().getHashMapFromXML(R.array.spinner_hashmap_test);
//
//        // Method from this class
//        setHashMapSpinner(linkedHashMap);
    }

    public Spinner getSpinner() {
        return spnSpinnerView;
    }

    public void setHashMapSpinner(LinkedHashMap<String, String> linkedHashMap) {
        mLinkedHashMap = linkedHashMap;
        Timber.i("mLinkedHashMap :" + mLinkedHashMap.toString());
        EMCSUtils.getInstance().mappingSpinner(getContext(), mLinkedHashMap, spnSpinnerView);
    }

    public void setOnSpinnerItemSelectedListener(SpinnerView view, OnSpinnerItemSelectedListener onSpinnerItemSelectedListener) {
        this.mView = view;
        this.onSpinnerItemSelectedListener = onSpinnerItemSelectedListener;
    }

    public void setSelection(int position) {
        spnSpinnerView.setSelection(position);
    }

    public int getSelectedItemPosition() {
        return spnSpinnerView.getSelectedItemPosition();
    }

    public String getKey() {
        return EMCSUtils.getInstance().getKeyFromIndex(mLinkedHashMap, getSelectedItemPosition());
    }

    public String getKey(int position) {
        return EMCSUtils.getInstance().getKeyFromIndex(mLinkedHashMap, position);
    }

    public String getKey(String value) {
        return EMCSUtils.getInstance().getKeyFromValue(mLinkedHashMap, value);
    }

    public String getValue(String key) {
        return mLinkedHashMap.get(key);
    }

    public String getValue() {
        return mLinkedHashMap.get(getKey(getSelectedItemPosition()));
    }

    public String getValue(int position) {
        return mLinkedHashMap.get(getKey(position));
    }

    private void setBackgroundNormal() {
        clickState = ClickState.NORMAL;
        relativeSpinnerViewLayout2.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_spinner_normal));
        imgSpinnerViewArrow.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.vector_arrow_normal_drop_down));
        txtSpinnerViewError.setVisibility(GONE);
    }

    private void setBackgroundNormalFocused() {
        clickState = ClickState.NORMAL_FOCUSED;
        relativeSpinnerViewLayout2.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_spinner_focused));
        imgSpinnerViewArrow.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.vector_arrow_normal_drop_up));
    }

    private void setBackgroundError() {
        clickState = ClickState.ERROR;
        relativeSpinnerViewLayout2.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_spinner_error_normal));
        imgSpinnerViewArrow.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.vector_arrow_error_drop_down));
        txtSpinnerViewError.setText(strError);
        txtSpinnerViewError.setVisibility(VISIBLE);
    }

    private void setBackgroundErrorFocused() {
        clickState = ClickState.ERROR_FOCUSED;
        relativeSpinnerViewLayout2.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_spinner_error_focused));
        imgSpinnerViewArrow.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.vector_arrow_error_drop_up));
    }

    private void setBackgroundDisable() {
        clickState = ClickState.DISABLE;
        relativeSpinnerViewLayout2.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_spinner_disable));
        imgSpinnerViewArrow.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.vector_arrow_disable_drop_down));
    }

    /*******************************************************************
     * ************************* Setter & Getter ***********************
     *******************************************************************/

    public Boolean getDisable() {
        return isDisable;
    }

    public void setDisable(Boolean disable) {
        isDisable = disable;
        // Method from this class
        invalidates();
    }

    public Boolean getRequireField() {
        return isRequireField;
    }

    public void setRequireField(Boolean requireField) {
        isRequireField = requireField;
        // Method from this class
        invalidates();
    }

    public String getTitle() {
        return strTitle;
    }

    public void setTitle(String title) {
        this.strTitle = title;
        // Method from this class
        invalidates();
    }

    public void showError(String strError) {
        this.strError = strError;
        // Method from this class
        setBackgroundError();
    }

    public void hideError() {
        // Method from this class
        setBackgroundNormal();
    }

    /*******************************************************************
     * *************************** Listener ****************************
     *******************************************************************/

    private final AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (onSpinnerItemSelectedListener != null) {
                onSpinnerItemSelectedListener.onItemSelected(mView, position, id);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            if (onSpinnerItemSelectedListener != null) {
                onSpinnerItemSelectedListener.onNothingSelected(mView);
            }
        }
    };

    /*******************************************************************
     * ************************* Enum Class ****************************
     *******************************************************************/

    public enum ClickState {
        NORMAL, NORMAL_FOCUSED, ERROR, ERROR_FOCUSED, DISABLE
    }

    /*******************************************************************
     * ********************** Interface Class **************************
     *******************************************************************/

    public interface OnSpinnerItemSelectedListener {
        void onItemSelected(SpinnerView view, int position, long id);

        void onNothingSelected(SpinnerView view);
    }
}
