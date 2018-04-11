package com.emcsthai.emcslibrary.Model.Utils;

import android.content.Context;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.emcsthai.emcslibrary.ViewGroup.EditTextView;
import com.emcsthai.emcslibrary.ViewGroup.SpinnerView;
import com.google.gson.GsonBuilder;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by nakarin on 9/20/2016 AD.
 */

public class EMCSUtils {

    private static EMCSUtils instance = null;

    private Context mContext = null;

    private EMCSUtils() {
        mContext = Contextor.getInstance().getContext();
    }

    public static EMCSUtils getInstance() {
        if (instance == null) {
            instance = new EMCSUtils();
        }
        return instance;
    }

    // isEmpty
    public boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    // isEmpty
    public boolean isEmpty(EditTextView editTextView) {
        return editTextView.getText().toString().trim().length() == 0;
    }

    public void setEditText(EditText edittext, String value) {

        if (value != null) {
            if (value.length() > 0) {
                edittext.setText(value);
            }
        }
    }

    public void setEditText(EditTextView editTextView, String value) {

        if (value != null) {
            if (value.length() > 0) {
                editTextView.setText(value);
            }
        }
    }

    // mappingSpinner
    public void mappingSpinner(Context context,
                               LinkedHashMap<String, String> hm_name, Spinner spinner) {
        if (hm_name != null && hm_name.size() > 0) {
            String[] data = hm_name.values().toArray(new String[hm_name.size()]);

            ArrayAdapter<String> adap_content = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_spinner_item,
                    data);

            adap_content.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adap_content);
        }
    }

    // setSpinner checkLength
    public void setSpinner(Spinner spinner, int index) {
        if (index > 0) {
            spinner.setSelection(index);
        } else {
            spinner.setSelection(0);
        }
    }

    // setSpinner checkLength
    public void setSpinner(SpinnerView spinner, int index) {
        if (index > 0) {
            spinner.setSelection(index);
        } else {
            spinner.setSelection(0);
        }
    }

    public String getKeyFromIndex(
            LinkedHashMap<String, String> hm_value, int index) {

        String key = "0";

        if (hm_value != null && hm_value.size() > 0) {
            if (index > -1 && index < hm_value.size()) {
                key = (new ArrayList<>(hm_value.keySet())).get(index);
            }
        }

        Timber.d("Key of " + index + " : " + key);

        return key;
    }

    public String getKeyFromValue(
            LinkedHashMap<String, String> hm_value, String value) {

        String key = "0";

        if (hm_value != null && hm_value.size() > 0) {

            for (Map.Entry<String, String> entry : hm_value.entrySet()) {
                if (entry.getValue().equals(value)) {
                    key = entry.getKey();
                }
            }
        }

        Timber.d("Key of " + value + " : " + key);

        return key;
    }

    public int getIndexFromKey(LinkedHashMap<String, String> hm_value,
                               String key) {

        int index = 0;

        if (hm_value != null && hm_value.size() > 0) {
            if (key != null) {
                if (key.length() > 0) {
                    ArrayList<String> Keys = new ArrayList<>(
                            hm_value.keySet());

                    for (int i = 0; i < Keys.size(); i++) {
                        if (Keys.get(i).equals(key)) {
                            index = i;
                        }
                    }
                }
            }
        }

        Timber.d("Index of " + key + " : " + index);

        return index;
    }

    public int getIndexFromValue(SpinnerView spinnerView, String value) {

        int index = 0;

        if (value != null) {
            if (value.length() > 0) {
                ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spinnerView.getSpinner()
                        .getAdapter();

                index = myAdap.getPosition(value);
            }
        }

        Timber.d("Index of " + value + " : " + index);

        return index;
    }

    public int getIndexFromValue(Spinner spinner, String value) {

        int index = 0;

        if (value != null) {
            if (value.length() > 0) {
                ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spinner
                        .getAdapter();

                index = myAdap.getPosition(value);
            }
        }

        Timber.d("Index of " + value + " : " + index);

        return index;
    }

//    public boolean isLocationEnabledGPS() {
//        LocationManager locationManager = (LocationManager) mContext
//                .getSystemService(Context.LOCATION_SERVICE);
//
//        boolean gpsEnabled = locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return gpsEnabled;
//    }

    public void logGsonToJson(String str, Object src) {
        String jsonStr = new GsonBuilder().setPrettyPrinting().create().toJson((src));
        Timber.i(str + " : " + jsonStr);
    }

    public LinkedHashMap<String, String> getHashMapFromXML(int stringArrayResourceId) {

        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

        String[] stringArray = mContext.getResources().getStringArray(
                stringArrayResourceId);

        for (String string : stringArray) {
            String[] splitResult = string.split("\\|", 2);
            linkedHashMap.put(splitResult[0], splitResult[1]);
        }

        return linkedHashMap;
    }

    public interface DelayCallback {
        void afterDelay();
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
        }, (long) secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }

//    public void hideSoftKeyboard() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)‌​;
//    }
}
