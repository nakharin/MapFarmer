package com.emcsthai.emcslibrary.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.emcsthai.emcslibrary.R;

/**
 * Created by nakarin on 7/3/2017 AD.
 */

public class DialogConfirm extends DialogFragment {

    private static final String KEY_TYPE = "KEY_TYPE";
    private static final String KEY_TITLE = "KEY_TITLE";
    private static final String KEY_MESSAGE = "KEY_MESSAGE";
    private static final String KEY_POSITIVE = "KEY_POSITIVE";
    private static final String KEY_NEGATIVE = "KEY_NEGATIVE";
    private static final String KEY_CANCELABLE = "KEY_CANCELABLE";

    private Type type;

    private int title;
    private int message;
    private int positive;
    private int negative;

    private boolean cancelable;

    public enum Type {
        ONE, TWO
    }

    private TextView tvTitle;
    private TextView tvMessage;
    private Button btnPositive;
    private Button btnNegative;

    public static DialogConfirm newInstance(Type type, @StringRes int title, @StringRes int message,
                                            @StringRes int positive, @StringRes int negative,
                                            boolean cancelable) {
        DialogConfirm fragment = new DialogConfirm();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type.ordinal());
        bundle.putInt(KEY_TITLE, title);
        bundle.putInt(KEY_MESSAGE, message);
        bundle.putInt(KEY_POSITIVE, positive);
        bundle.putInt(KEY_NEGATIVE, negative);
        bundle.putBoolean(KEY_CANCELABLE, cancelable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            restoreArguments(getArguments());
        } else {
            restoreInstanceState(savedInstanceState);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_dialog_confirm, container);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        // Method from this class
        bindView(v);
        // Method from this class
        setupView();
    }

    private void bindView(View v) {
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        tvMessage = (TextView) v.findViewById(R.id.tv_message);
        btnPositive = (Button) v.findViewById(R.id.btn_positive);
        btnNegative = (Button) v.findViewById(R.id.btn_negative);
        View vLine = v.findViewById(R.id.v_line);
        switch (type) {
            case ONE:
                vLine.setVisibility(View.GONE);
                btnNegative.setVisibility(View.GONE);
                break;
            case TWO:
                vLine.setVisibility(View.VISIBLE);
                btnNegative.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupView() {
        tvTitle.setText(title);
        tvMessage.setText(message);
        btnPositive.setText(positive);
        btnNegative.setText(negative);

        btnPositive.setOnClickListener(onClickListener);
        btnNegative.setOnClickListener(onClickListener);
    }

    private OnDialogListener getOnDialogListener() {
        Fragment fragment = getParentFragment();
        try {
            if (fragment != null) {
                return (OnDialogListener) fragment;
            } else {
                return (OnDialogListener) getActivity();
            }
        } catch (ClassCastException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    /********************************************************************************
     ********************************** Listener ***********************************
     ********************************************************************************/

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnPositive) {
                OnDialogListener onDialogListener = getOnDialogListener();
                if (onDialogListener != null) {
                    onDialogListener.onPositiveButtonClick(getDialog());
                }
            }

            if (v == btnNegative) {
                OnDialogListener onDialogListener = getOnDialogListener();
                if (onDialogListener != null) {
                    onDialogListener.onNegativeButtonClick(getDialog());
                }
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TYPE, type.ordinal());
        outState.putInt(KEY_TITLE, title);
        outState.putInt(KEY_MESSAGE, message);
        outState.putInt(KEY_POSITIVE, positive);
        outState.putInt(KEY_NEGATIVE, negative);
        outState.putBoolean(KEY_CANCELABLE, cancelable);
    }

    private void restoreInstanceState(Bundle bundle) {
        type = (bundle.getInt(KEY_TYPE) == 0) ? Type.ONE : Type.TWO;
        title = bundle.getInt(KEY_TITLE);
        message = bundle.getInt(KEY_MESSAGE);
        positive = bundle.getInt(KEY_POSITIVE);
        negative = bundle.getInt(KEY_NEGATIVE);
        cancelable = bundle.getBoolean(KEY_CANCELABLE);
    }

    private void restoreArguments(Bundle bundle) {
        type = (bundle.getInt(KEY_TYPE) == 0) ? Type.ONE : Type.TWO;
        title = bundle.getInt(KEY_TITLE);
        message = bundle.getInt(KEY_MESSAGE);
        positive = bundle.getInt(KEY_POSITIVE);
        negative = bundle.getInt(KEY_NEGATIVE);
        cancelable = bundle.getBoolean(KEY_CANCELABLE);
    }

    /********************************************************************************
     ********************************** Interface ***********************************
     ********************************************************************************/

    public interface OnDialogListener {
        void onPositiveButtonClick(Dialog dialog);

        void onNegativeButtonClick(Dialog dialog);
    }

    /********************************************************************************
     ************************************ Builder ***********************************
     ********************************************************************************/

    public static class Builder {

        private Type type;

        private int title;
        private int message;
        private int positive;
        private int negative;

        private boolean cancelable = true;

        public Builder(Type type) {
            this.type = type;
        }

        public Builder setTitle(@StringRes int title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(@StringRes int message) {
            this.message = message;
            return this;
        }

        public Builder setPositive(@StringRes int positive) {
            this.positive = positive;
            return this;
        }

        public Builder setNegative(@StringRes int negative) {
            this.negative = negative;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public DialogConfirm build() {
            DialogConfirm fragment = DialogConfirm.newInstance(type,
                    title, message, positive, negative, cancelable);
            fragment.setCancelable(cancelable);
            return fragment;
        }
    }
}
