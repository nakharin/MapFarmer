package com.emcsthai.emcslibrary.ViewGroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.emcsthai.emcslibrary.Model.Utils.EMCSUtils;
import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class StepBarView extends BaseCustomViewGroup {

    private FrameLayout frameLayout1;
    private FrameLayout frameLayout2;
    private FrameLayout frameLayout3;

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    private View view1;
    private View view2;

    private OnStepBarClickListener onStepBarClickListener;

    public StepBarView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public StepBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public StepBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public StepBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_step_bar, this);
    }

    private void initInstances() {
        // findViewById here

        frameLayout1 = (FrameLayout) findViewById(R.id.step_bar_view_frame_layout_1);
        frameLayout2 = (FrameLayout) findViewById(R.id.step_bar_view_frame_layout_2);
        frameLayout3 = (FrameLayout) findViewById(R.id.step_bar_view_frame_layout_3);

        imageView1 = (ImageView) findViewById(R.id.step_bar_view_image_view_1);
        imageView2 = (ImageView) findViewById(R.id.step_bar_view_image_view_2);
        imageView3 = (ImageView) findViewById(R.id.step_bar_view_image_view_3);

        textView1 = (TextView) findViewById(R.id.step_bar_view_text_view_desc_1);
        textView2 = (TextView) findViewById(R.id.step_bar_view_text_view_desc_2);
        textView3 = (TextView) findViewById(R.id.step_bar_view_text_view_desc_3);

        view1 = (View) findViewById(R.id.step_bar_view_view_1);
        view2 = (View) findViewById(R.id.step_bar_view_view_2);

        frameLayout1.setOnClickListener(onClickListener);
        frameLayout2.setOnClickListener(onClickListener);
        frameLayout3.setOnClickListener(onClickListener);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    /*******************************************************************
     * **************************** Method *****************************
     *******************************************************************/

    public void setStep(Step step) {

        switch (step) {
            case ONE:
                EMCSUtils.getInstance().setDelay(0.2, new EMCSUtils.DelayCallback() {
                    @Override
                    public void afterDelay() {
                        setStepOnePressed();
                        setStepTwoNormal();
                        setStepThreeNormal();
                    }
                });
                break;
            case TWO:
                EMCSUtils.getInstance().setDelay(0.2, new EMCSUtils.DelayCallback() {
                    @Override
                    public void afterDelay() {
                        setStepOnePressed();
                        setStepTwoPressed();
                        setStepThreeNormal();
                    }
                });
                break;
            case THREE:
                EMCSUtils.getInstance().setDelay(0.2, new EMCSUtils.DelayCallback() {
                    @Override
                    public void afterDelay() {
                        setStepOnePressed();
                        setStepTwoPressed();
                        setStepThreePressed();
                    }
                });
                break;
        }
    }

    // Normal

    private void setStepTwoNormal() {
        frameLayout2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_step_bar_normal));
        imageView2.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorGrayLV2));
        textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGrayLV2));
        view1.setBackground(ContextCompat.getDrawable(getContext(), R.color.colorGrayLV2));
    }

    private void setStepThreeNormal() {
        frameLayout3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_step_bar_normal));
        imageView3.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorGrayLV2));
        textView3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGrayLV2));
        view2.setBackground(ContextCompat.getDrawable(getContext(), R.color.colorGrayLV2));
    }

    // Pressed

    private void setStepOnePressed() {
        frameLayout1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_step_bar_pressed));
        imageView1.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        textView1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    private void setStepTwoPressed() {
        frameLayout2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_step_bar_pressed));
        imageView2.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        view1.setBackground(ContextCompat.getDrawable(getContext(), R.color.colorPrimary));
    }

    private void setStepThreePressed() {
        frameLayout3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_step_bar_pressed));
        imageView3.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        textView3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        view2.setBackground(ContextCompat.getDrawable(getContext(), R.color.colorPrimary));
    }

    public void setOnStepBarClickListener(OnStepBarClickListener onStepBarClickListener) {
        this.onStepBarClickListener = onStepBarClickListener;
    }

    /*******************************************************************
     * ************************** Listener *****************************
     *******************************************************************/

    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == frameLayout1) {
                if (onStepBarClickListener != null) {
                    onStepBarClickListener.onItemClick(Step.ONE);
                }
            }

            if (v == frameLayout2) {
                if (onStepBarClickListener != null) {
                    onStepBarClickListener.onItemClick(Step.TWO);
                }
            }

            if (v == frameLayout3) {
                if (onStepBarClickListener != null) {
                    onStepBarClickListener.onItemClick(Step.THREE);
                }
            }
        }
    };

    /*******************************************************************
     * *********************** Inner Class *****************************
     *******************************************************************/

    public enum Step {
        ONE, TWO, THREE
    }

    /*******************************************************************
     * ********************** Interface Class **************************
     *******************************************************************/

    public interface OnStepBarClickListener {
        void onItemClick(Step step);
    }
}
