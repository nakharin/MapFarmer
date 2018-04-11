package com.emcsthai.emcslibrary.ViewGroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by nakarin on 9/27/2016 AD.
 */

public class CameraView extends BaseCustomViewGroup {

    private TextView txtCameraViewTitle;

    private ImageView imgCameraViewRequire;

    private String strTitle = "";

    private boolean isDisable = false;
    private boolean isRequireField = false;

    private LoadingView loadingView;

    private TextView txtCameraViewEdit;

    private ImageView imgCameraViewCamera;

    public CameraView(Context context) {
        super(context);
        initInflate();
        initInstance();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstance();
        initWithAttrs(attrs, 0, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstance();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstance();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_camera, this);
    }

    private void initInstance() {

        txtCameraViewTitle = (TextView) findViewById(R.id.txt_camera_view_title);

        imgCameraViewRequire = (ImageView) findViewById(R.id.img_camera_view_require);

        txtCameraViewEdit = (TextView) findViewById(R.id.camera_view_txt_edit);
        imgCameraViewCamera = (ImageView) findViewById(R.id.camera_view_img_camera);
        loadingView = (LoadingView) findViewById(R.id.camera_view_loading_view);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CameraView,
                defStyleAttr, defStyleRes);

        if (a.hasValue(R.styleable.CameraView_em_camera_title)) {
            strTitle = a.getString(R.styleable.CameraView_em_camera_title);
        }

        if (a.hasValue(R.styleable.CameraView_em_camera_disable)) {
            isDisable = a.getBoolean(R.styleable.CameraView_em_camera_disable, false);
        }

        if (a.hasValue(R.styleable.CameraView_em_camera_require_field)) {
            isRequireField = a.getBoolean(R.styleable.CameraView_em_camera_require_field, false);
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
            txtCameraViewTitle.setText(strTitle);
        }

        if (isRequireField) {
            imgCameraViewRequire.setVisibility(VISIBLE);
        } else {
            imgCameraViewRequire.setVisibility(GONE);
        }

        if (isDisable) {
            imgCameraViewCamera.setEnabled(false);
        } else {
            imgCameraViewCamera.setEnabled(true);
        }
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = MeasureSpec.getSize(widthMeasureSpec); // width in px
//        int height = width * 2 / 3;
//
//        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
//                height,
//                MeasureSpec.EXACTLY
//        );
//        // Child View
//        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
//        // Self
//        setMeasuredDimension(width, height);
//    }

    public ImageView getImageView() {
        return imgCameraViewCamera;
    }

    public TextView getTextView() {
        return txtCameraViewEdit;
    }

    public void showLoading() {
        loadingView.setVisibility(VISIBLE);
    }

    public void hideLoading() {
        loadingView.setVisibility(GONE);
    }

    public void loadBitmap(Bitmap bitmap) {
        txtCameraViewEdit.setVisibility(VISIBLE);
        imgCameraViewCamera.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgCameraViewCamera.setImageBitmap(bitmap);
    }

    public void loadImage(File imgFile) {
        showLoading();
        loadFile(imgFile, null);
    }

    public void loadImage(File imgFile, LoadCallback loadCallback) {
        showLoading();
        loadFile(imgFile, loadCallback);
    }

    private void loadFile(File imgFile, final LoadCallback loadCallback) {

        if (imgFile.isFile()) {

            imgCameraViewCamera.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            Picasso.with(getContext())
                    .load(imgFile)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .noFade()
                    .into(imgCameraViewCamera, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            txtCameraViewEdit.setVisibility(VISIBLE);
                            imgCameraViewCamera.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            hideLoading();
                            if (loadCallback != null) {
                                loadCallback.onSuccess();
                            }
                        }

                        @Override
                        public void onError() {
                            hideLoading();
                            if (loadCallback != null) {
                                loadCallback.onError();
                            }
                        }
                    });

        } else {
            txtCameraViewEdit.setVisibility(GONE);
            imgCameraViewCamera.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.vector_add_a_photo));
            imgCameraViewCamera.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    /*******************************************************************
     * *************************** Interface ***************************
     *******************************************************************/

    public interface LoadCallback {
        void onSuccess();

        void onError();
    }

    /*******************************************************************
     * **************** Save & Restore InstanceState ********************
     *******************************************************************/

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//
//        BundleSavedState savedState = new BundleSavedState(superState);
//        // Save Instance State(s) here to the 'savedState.getBundle()'
//        // for example,
//        // savedState.getBundle().putString("key", value);
//
//        return savedState;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        BundleSavedState ss = (BundleSavedState) state;
//        super.onRestoreInstanceState(ss.getSuperState());
//
//        Bundle bundle = ss.getBundle();
//        // Restore State from bundle here
//    }

}
