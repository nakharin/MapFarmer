package com.emcsthai.emcslibrary.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.emcsthai.emcslibrary.Adapter.SearchAdapter;
import com.emcsthai.emcslibrary.Model.Utils.ToastUtils;
import com.emcsthai.emcslibrary.R;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by nakarin on 4/11/2017 AD.
 * Referance from https://github.com/MiguelCatalan/MaterialSearchView
 */

public class SearchActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CAMERA_PERMISSION = 100;

    private final static int RESULT_CODE_ZXING_SCANNER = 999;

    private static final int REQUEST_VOICE = 9999;

    private ListAdapter mAdapter;

    private ImageView imgActionUp;
    private EditText edtSearch;
    private ImageView imgActionVoice;
    private ImageView imgActionQRCode;
    private ImageView imgActionClear;

    private ListView listSuggestion;

    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;

    private boolean submit = false;

    private boolean ellipsize = false;

    public final static String EXTRA_NAME = "Suggestions";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Method From this class
        initInstances();

        imgActionUp.setOnClickListener(onClickListener);
        imgActionVoice.setOnClickListener(onClickListener);
        imgActionQRCode.setOnClickListener(onClickListener);
        imgActionClear.setOnClickListener(onClickListener);
        edtSearch.setOnClickListener(onClickListener);

        showQRCode(true);
//        showVoice(true);

        if (getIntent().hasExtra(EXTRA_NAME)) {
            setSuggestions(getIntent().getStringArrayExtra(EXTRA_NAME));
        }

        initSearchView();
    }

    private void initInstances() {
        imgActionUp = (ImageView) findViewById(R.id.img_action_up);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        imgActionVoice = (ImageView) findViewById(R.id.img_action_voice);
        imgActionQRCode = (ImageView) findViewById(R.id.img_action_qr_code);
        imgActionClear = (ImageView) findViewById(R.id.img_action_clear);

        listSuggestion = (ListView) findViewById(R.id.list_suggestion);
    }

    private void initSearchView() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserQuery = s;
                startFilter(s);
                SearchActivity.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(edtSearch);
                    showSuggestions();
                }
            }
        });
    }

    private void startFilter(CharSequence s) {
        if (mAdapter != null && mAdapter instanceof Filterable) {
            ((Filterable) mAdapter).getFilter().filter(s, filterListener);
        }
    }

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak an item name or number");    // user hint
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);    // setting recognition model, optimized for short phrases – search queries
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);    // quantity of results we want to receive
        startActivityForResult(intent, REQUEST_VOICE);
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = edtSearch.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            imgActionClear.setVisibility(View.VISIBLE);
            showQRCode(false);
//            showVoice(false);
        } else {
            imgActionClear.setVisibility(GONE);
            showQRCode(true);
//            showVoice(true);
        }

        mOldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        CharSequence query = edtSearch.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
//            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
//                closeSearch();
//                mSearchSrcTextView.setText(null);
//            }
        }
    }

    private boolean isVoiceAvailable() {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() == 0;
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public void showQRCode(boolean show) {
        if (show) {
            imgActionQRCode.setVisibility(View.VISIBLE);
        } else {
            imgActionQRCode.setVisibility(GONE);
        }
    }

    public void showVoice(boolean show) {
        if (show && isVoiceAvailable()) {
            imgActionVoice.setVisibility(View.VISIBLE);
        } else {
            imgActionVoice.setVisibility(GONE);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        listSuggestion.setAdapter(adapter);
        startFilter(edtSearch.getText());
    }

    private void setRequestCodeCameraPermission() {
        int hasOpenCameraPermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        if (hasOpenCameraPermission != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(this, ZXingScannerActivity.class);
        startActivityForResult(intent, RESULT_CODE_ZXING_SCANNER);
    }

    /**
     * Set Adapter for suggestions list with the given suggestion array
     *
     * @param suggestions array of suggestions
     */
    public void setSuggestions(String[] suggestions) {
        if (suggestions != null && suggestions.length > 0) {
            final SearchAdapter adapter = new SearchAdapter(this, suggestions, null, ellipsize);
            setAdapter(adapter);

            listSuggestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setQuery((String) adapter.getItem(position), submit);
                }
            });
        }
    }

    public void setQuery(CharSequence query, boolean submit) {
        edtSearch.setText(query);
        if (query != null) {
            edtSearch.setSelection(edtSearch.length());
            mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    /**
     * Call this method to show suggestions list. This shows up when adapter is set. Call  before calling this.
     */
    public void showSuggestions() {
        if (mAdapter != null && mAdapter.getCount() > 0) {
            listSuggestion.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Dismiss the suggestions list.
     */
    public void dismissSuggestions() {
        if (listSuggestion.getVisibility() == View.VISIBLE) {
            listSuggestion.setVisibility(GONE);
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == imgActionUp) {
                finish();
            }

            if (v == imgActionVoice) {

            }

            if (v == imgActionQRCode) {
                setRequestCodeCameraPermission();
            }

            if (v == imgActionClear) {
                edtSearch.setText(null);
            }
        }
    };

    private final Filter.FilterListener filterListener = new Filter.FilterListener() {
        @Override
        public void onFilterComplete(int count) {
            if (count > 0) {
                showSuggestions();
            } else {
                dismissSuggestions();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_CODE_ZXING_SCANNER) {

                String result = data.getStringExtra("result");
                String arrResult[] = result.split("\\|");

                if (arrResult.length == 2) {
                    edtSearch.setText(arrResult[0]);
                } else {
                    ToastUtils.getInstance().showText("Format Exception : " + result);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openCamera();
                } else {
                    // Permission Denied
                    ToastUtils.getInstance().showText("CAMERA Permission Denied, Please allow this permission in app setting.");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
