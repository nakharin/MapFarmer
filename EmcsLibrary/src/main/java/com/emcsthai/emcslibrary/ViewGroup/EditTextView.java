package com.emcsthai.emcslibrary.ViewGroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.emcsthai.emcslibrary.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;

/**
 * Created by nakharin on 11/16/2014.
 */
public class EditTextView extends BaseCustomViewGroup {

    /*******************************************************************
     * ************************* Variable ******************************
     *******************************************************************/

    private FrameLayout frameEditTextViewLayout;

    private LinearLayout linearEditTextViewLayout;

    private TextView txtEditTextViewTitle;

    private ImageView imgEditTextViewRequire;

    private EditText edtEditTextViewText;

    private TextView txtEditTextViewHelper;

    private TextView txtEditTextViewError;

    private Boolean isDisable = false;
    private Boolean isRequireField = false;
    private boolean isShowPassword = false;

    private String title = "";
    private String text = "";
    private String hint = "";
    private String helper = "";
    private String error = "";
    private int maxLength = -1;
    private int minHeight = -1;

    private InputType inputType = InputType.TEXT;

    private ClickState clickState = ClickState.NORMAL;

    private OnEditTextClickListener onEditTextClickListener;

    /**
     * Do not use
     */
    protected EditTextView view;

    /*******************************************************************
     * ********************* Default Method ****************************
     *******************************************************************/

    public EditTextView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    /*******************************************************************
     * *********************** Init Method *****************************
     *******************************************************************/

    private void initInflate() {
        inflate(getContext(), R.layout.view_group_edit_text, this);
    }

    private void initInstances() {
        // findViewById here
        frameEditTextViewLayout = (FrameLayout) findViewById(R.id.frame_edit_text_view_layout);

        linearEditTextViewLayout = (LinearLayout) findViewById(R.id.linear_edit_text_view_layout);

        txtEditTextViewTitle = (TextView) findViewById(R.id.txt_edit_text_view_title);

        imgEditTextViewRequire = (ImageView) findViewById(R.id.img_edit_text_view_require);

        edtEditTextViewText = (EditText) findViewById(R.id.edt_edit_text_view_text);

        txtEditTextViewHelper = (TextView) findViewById(R.id.txt_edit_text_view_helper);

        txtEditTextViewError = (TextView) findViewById(R.id.txt_edit_text_view_error);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EditTextView,
                defStyleAttr, defStyleRes);

        try {
            if (a.hasValue(R.styleable.EditTextView_em_disable)) {
                isDisable = a.getBoolean(R.styleable.EditTextView_em_disable, false);
            }

            if (a.hasValue(R.styleable.EditTextView_em_require_field)) {
                isRequireField = a.getBoolean(R.styleable.EditTextView_em_require_field, false);
            }

            if (a.hasValue(R.styleable.EditTextView_em_txt_title)) {
                title = a.getString(R.styleable.EditTextView_em_txt_title);
            }

            if (a.hasValue(R.styleable.EditTextView_em_txt_text)) {
                text = a.getString(R.styleable.EditTextView_em_txt_text);
            }

            if (a.hasValue(R.styleable.EditTextView_em_txt_hint)) {
                hint = a.getString(R.styleable.EditTextView_em_txt_hint);
            }

            if (a.hasValue(R.styleable.EditTextView_em_txt_helper)) {
                helper = a.getString(R.styleable.EditTextView_em_txt_helper);
            }

            if (a.hasValue(R.styleable.EditTextView_em_max_length)) {
                maxLength = a.getInt(R.styleable.EditTextView_em_max_length, -1);
            }

            if (a.hasValue(R.styleable.EditTextView_em_edt_min_height)) {
                minHeight = a.getInt(R.styleable.EditTextView_em_edt_min_height, -1);
            }

            if (a.hasValue(R.styleable.EditTextView_em_input_type)) {
                inputType = InputType.fromId(a.getInt(R.styleable.EditTextView_em_input_type, 0));
            }
        } finally {
            a.recycle();
        }

        // Method from this class
        invalidates();
    }

    private void invalidates() {

        if (title != null && !title.equals("")) {
            txtEditTextViewTitle.setText(title);
        } else {
            txtEditTextViewTitle.setVisibility(GONE);
        }

        if (text != null && !text.equals("")) {
            edtEditTextViewText.setText(text);
            text = "";
        }

        if (hint != null && !hint.equals("")) {
            edtEditTextViewText.setHint(hint);
        }

//        if (!helper.equals("")) {
//            txtEditTextViewHelper.setText(helper);
//        }

        if (minHeight != -1) {
            edtEditTextViewText.setMinHeight(minHeight);
        }

        switch (inputType) {
            case TEXT:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                break;
            case TEXT_MULTI_LINE:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case TEXT_PASSWORD:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                // Method from this class
                setEditTextViewPasswordVisibilityOff();
                edtEditTextViewText.setOnTouchListener(onPasswordTouchListener);
                break;
            case NUMBER:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                break;
            case NUMBER_PASSWORD:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                // Method from this class
                setEditTextViewPasswordVisibilityOff();
                edtEditTextViewText.setOnTouchListener(onPasswordTouchListener);
                break;
            case PHONE:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
                edtEditTextViewText.addTextChangedListener(textWatcher);
                maxLength = 12;
                break;
            case TELEPHONE:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
                edtEditTextViewText.addTextChangedListener(textWatcher);
                maxLength = 11;
                break;
            case TEXT_POLICY:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                edtEditTextViewText.addTextChangedListener(textWatcher);
                break;
            case DATE_TIME:
//                edtEditTextViewText.setFocusable(false);
                edtEditTextViewText.setOnTouchListener(onDateTimeTouchListener);
                break;
            case TEXT_EMAIL:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case TEXT_POLICY_STD:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                break;
            case ID_CARD:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
                edtEditTextViewText.addTextChangedListener(textWatcher);
                maxLength = 17;
                break;

        }

        if (maxLength != -1) {
            edtEditTextViewText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }

        if (isRequireField) {
            imgEditTextViewRequire.setVisibility(VISIBLE);
        } else {
            imgEditTextViewRequire.setVisibility(GONE);
        }

        if (isDisable) {
            edtEditTextViewText.setEnabled(false);
            edtEditTextViewText.setOnFocusChangeListener(null);
            // Method from this class
            setBackgroundDisable();
        } else {
            edtEditTextViewText.setEnabled(true);
            edtEditTextViewText.setOnFocusChangeListener(onFocusChangeListener);
            // Method from this class
            setBackgroundNormal();
        }
    }

    private void setBackgroundNormal() {
        clickState = ClickState.NORMAL;
        edtEditTextViewText.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_edit_text_normal));
    }

    private void setBackgroundNormalFocused() {
        clickState = ClickState.NORMAL_FOCUSED;
        edtEditTextViewText.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_edit_text_focused));
    }

    private void setBackgroundError() {
        clickState = ClickState.ERROR;
        edtEditTextViewText.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_edit_text_error_normal));
    }

    private void setBackgroundErrorFocused() {
        clickState = ClickState.ERROR_FOCUSED;
        edtEditTextViewText.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_edit_text_error_focused));
    }

    private void setBackgroundDisable() {
        clickState = ClickState.DISABLE;
        edtEditTextViewText.setBackground(ContextCompat.getDrawable(getContext(),
                R.drawable.shape_edit_text_disable));
    }

    private void hidePassword() {
        isShowPassword = false;
        // Method from this class
        setEditTextViewPasswordVisibilityOff();

        switch (inputType) {
            case TEXT_PASSWORD:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case NUMBER_PASSWORD:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
        }

        switch (clickState) {
            case NORMAL:
                // Method from this class
                setBackgroundNormalFocused();
                break;
            case ERROR:
                // Method from this class
                setBackgroundErrorFocused();
                break;
        }
    }

    private void showPassword() {
        isShowPassword = true;
        // Method from this class
        setEditTextViewPasswordVisibilityOn();

        switch (inputType) {
            case TEXT_PASSWORD:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                break;
            case NUMBER_PASSWORD:
                edtEditTextViewText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                break;
        }

        switch (clickState) {
            case NORMAL:
                // Method from this class
                setBackgroundNormalFocused();
                break;
            case ERROR:
                // Method from this class
                setBackgroundErrorFocused();
                break;
        }
    }

    private void setSelectionLastCharacter() {
        edtEditTextViewText.setSelection(edtEditTextViewText.getText().length());
    }

    private void setEditTextViewPasswordVisibilityOn() {
        edtEditTextViewText.setCompoundDrawablesWithIntrinsicBounds(null, null,
                ContextCompat.getDrawable(getContext(), R.drawable.vector_visibility),
                null);
    }

    private void setEditTextViewPasswordVisibilityOff() {
        edtEditTextViewText.setCompoundDrawablesWithIntrinsicBounds(null, null,
                ContextCompat.getDrawable(getContext(), R.drawable.vector_visibility_off),
                null);
    }

    public void showError(String error) {
        this.error = error;
        if (!error.equals("")) {
            txtEditTextViewError.setText(error);
            txtEditTextViewError.setVisibility(VISIBLE);
            // Method from this class
            setBackgroundError();
            edtEditTextViewText.requestFocusFromTouch();

            // Method from this class
            setSelectionLastCharacter();

            YoYo.with(Techniques.Shake).duration(200).playOn(edtEditTextViewText);

            edtEditTextViewText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(edtEditTextViewText, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 200);
        }
    }

    public void hideError() {
        txtEditTextViewError.setVisibility(GONE);
        // Method from this class
        setBackgroundNormal();
    }

    /*******************************************************************
     * ************************* Setter & Getter ***********************
     *******************************************************************/

    public String getError() {
        return error;
    }

    public void setDisable(boolean isDisable) {
        this.isDisable = isDisable;
        // Method from this class
        invalidates();
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setRequireField(boolean isRequireField) {
        this.isRequireField = isRequireField;
        // Method from this class
        invalidates();
    }

    public Boolean getRequireField() {
        return isRequireField;
    }

    public void setText(String text) {
        this.text = text;
        // Method from this class
        invalidates();
    }

    public Editable getText() {
        return edtEditTextViewText.getText();
    }

    public void setTextCardId(String textCardId) {

        StringBuilder newPhone = new StringBuilder();

        if (textCardId != null && !textCardId.equals("")) {
            String textCardIdAfterReplace = textCardId.replace("-", "");
            char c[] = textCardIdAfterReplace.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (i == 0 || i == 4 || i == 9 || i == 11) {
                    newPhone.append(c[i]).append("-");
                } else {
                    newPhone.append(c[i]);
                }
            }
        }

        setText(newPhone.toString());
    }

    public void setTextPhone(String textPhone) {

        StringBuilder newPhone = new StringBuilder();

        if (textPhone != null && !textPhone.equals("")) {
            String textPhoneAfterReplace = textPhone.replace("-", "");
            char c[] = textPhoneAfterReplace.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (i == 1 || i == 4) {
                    newPhone.append(c[i]).append("-");
                } else {
                    newPhone.append(c[i]);
                }
            }
        }

        setText(newPhone.toString());
    }

    public void setTextMobilePhone(String textMobilePhone) {

        StringBuilder newPhone = new StringBuilder();

        if (textMobilePhone != null && !textMobilePhone.equals("")) {
            String textMobilePhoneAfterReplace = textMobilePhone.replace("-", "");
            char c[] = textMobilePhoneAfterReplace.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (i == 2 || i == 5) {
                    newPhone.append(c[i]).append("-");
                } else {
                    newPhone.append(c[i]);
                }
            }
        }

        setText(newPhone.toString());
    }

    public String getTextNotSpecialChar() {

        StringBuilder textPhone = new StringBuilder();
        String[] phoneNumberSplit = getText().toString().split("-");
        for (int i = 0; i < phoneNumberSplit.length; i++) {
            textPhone.append(phoneNumberSplit[i]);
        }

        return textPhone.toString();
    }

    public void setHelper(String helper) {
        this.helper = helper;
        // Method from this class
        invalidates();
    }

    public String getHelper() {
        return helper;
    }

    public void setTitle(String title) {
        this.title = title;
        // Method from this class
        invalidates();
    }

    public String getTitle() {
        return title;
    }

    public void setHint(String hint) {
        this.hint = hint;
        // Method from this class
        invalidates();
    }

    public void setOnEditTextClickListener(EditTextView view, OnEditTextClickListener onEditTextClickListener) {
        this.view = view;
        this.onEditTextClickListener = onEditTextClickListener;
    }

    /*******************************************************************
     * *************************** Listener ****************************
     *******************************************************************/

    final View.OnTouchListener onPasswordTouchListener = new View.OnTouchListener() {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (event.getX() >= (edtEditTextViewText.getRight() - edtEditTextViewText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (!isShowPassword) {
                            // Method from this class
                            showPassword();
                        } else {
                            // Method from this class
                            hidePassword();
                        }

                        return true;
                    }
                    break;
            }
            return false;
        }
    };

    final View.OnTouchListener onDateTimeTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (clickState != ClickState.NORMAL) {
                        // Method from this class
                        setBackgroundNormal();
                    }
                    if (onEditTextClickListener != null) {
                        onEditTextClickListener.onEditTextClick(view);
                    }
                    return true;
                case MotionEvent.ACTION_DOWN:
                    if (clickState != ClickState.NORMAL_FOCUSED) {
                        // Method from this class
                        setBackgroundNormalFocused();
                    }
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    if (clickState != ClickState.NORMAL) {
                        // Method from this class
                        setBackgroundNormal();
                    }
                    return true;
            }

            return false;
        }
    };

    final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {

                switch (clickState) {
                    case NORMAL:
                        // Method from this class
                        setBackgroundNormalFocused();
                        break;
                    case ERROR:
                        // Method from this class
                        setBackgroundErrorFocused();
                        break;
                }

//                if (!helper.equals("")) {
//                    txtEditTextViewHelper.setVisibility(VISIBLE);
//                }

            } else {

                switch (clickState) {
                    case NORMAL_FOCUSED:
                        // Method from this class
                        setBackgroundNormal();
                        break;
                    case ERROR_FOCUSED:
                        // Method from this class
                        setBackgroundError();
                        break;
                }

//                if (!helper.equals("")) {
//                    txtEditTextViewHelper.setVisibility(GONE);
//                }
            }
        }
    };

    final TextWatcher textWatcher = new TextWatcher() {

        boolean addHyphen;
        boolean removeHyphen;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            switch (inputType) {
                case PHONE:
                    addHyphen = (start == 3 && count == 1 && after == 0)
                            || (start == 7 && count == 1 && after == 0);
                    break;
                case TEXT_POLICY:
                    addHyphen = (start == 6 && count == 1 && after == 0);
                    break;
                case TELEPHONE:
                    addHyphen = (start == 2 && count == 1 && after == 0)
                            || (start == 6 && count == 1 && after == 0);
                    break;
                case ID_CARD:
                    addHyphen = (start == 1 && count == 1 && after == 0)
                            || (start == 6 && count == 1 && after == 0)
                            || (start == 12 && count == 1 && after == 0)
                            || (start == 15 && count == 1 && after == 0);
                    break;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (inputType) {
                case PHONE:
                    removeHyphen = (start == 3 && before == 1 && count == 0)
                            || (start == 7 && before == 1 && count == 0);
                    break;
                case TEXT_POLICY:
                    removeHyphen = (start == 7 && before == 1 && count == 0);
                    break;
                case TELEPHONE:
                    removeHyphen = (start == 2 && before == 1 && count == 0)
                            || (start == 6 && before == 1 && count == 0);
                    break;
                case ID_CARD:
                    addHyphen = (start == 1 && before == 1 && count == 0)
                            || (start == 6 && before == 1 && count == 0)
                            || (start == 12 && before == 1 && count == 0)
                            || (start == 15 && before == 1 && count == 0);
                    break;

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (inputType) {
                case PHONE:
                    if (s.length() == 3 || s.length() == 7) {
                        if (!addHyphen) {
                            s.append('-');
                        }
                    }

                    if (removeHyphen) {
                        s.delete(s.length() - 1, s.length());
                    }
                    break;
                case TEXT_POLICY:
                    if (s.length() == 6) {
                        if (!addHyphen) {
                            s.append('/');
                            s.append('M');
                        }
                    }

                    if (removeHyphen) {
                        s.delete(s.length() - 2, s.length());
                    }
                    break;
                case TELEPHONE:
                    if (s.length() == 2 || s.length() == 6) {
                        if (!addHyphen) {
                            s.append('-');
                        }
                    }

                    if (removeHyphen) {
                        s.delete(s.length() - 1, s.length());
                    }
                    break;
                case ID_CARD:
                    if (s.length() == 1 || s.length() == 6 ||
                            s.length() == 12 || s.length() == 15) {
                        if (!addHyphen) {
                            s.append('-');
                        }
                    }

                    if (removeHyphen) {
                        s.delete(s.length() - 1, s.length());
                    }
                    break;
            }
        }
    };

    /*******************************************************************
     * ************************* Enum Class ****************************
     *******************************************************************/

    public enum ClickState {
        NORMAL, NORMAL_FOCUSED, ERROR, ERROR_FOCUSED, DISABLE
    }

    public enum InputType {
        TEXT(1),
        TEXT_MULTI_LINE(2),
        TEXT_PASSWORD(3),
        NUMBER(4),
        NUMBER_PASSWORD(5),
        PHONE(6),
        TELEPHONE(7),
        TEXT_POLICY(8),
        DATE_TIME(9),
        TEXT_EMAIL(10),
        TEXT_POLICY_STD(11),
        ID_CARD(12);

        int id;

        InputType(int id) {
            this.id = id;
        }

        static InputType fromId(int id) {
            for (InputType inputType : values()) {
                if (inputType.id == id) {
                    return inputType;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    /*******************************************************************
     * ********************** Interface Class **************************
     *******************************************************************/

    public interface OnEditTextClickListener {
        void onEditTextClick(EditTextView v);
    }
}
