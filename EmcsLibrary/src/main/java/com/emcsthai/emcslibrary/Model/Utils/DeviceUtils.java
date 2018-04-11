package com.emcsthai.emcslibrary.Model.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by ton on 8/2/2016 AD.
 */
public class DeviceUtils {
    private static DeviceUtils instance = null;

    private Context mContext;
    public static String DEVICE_TYPE = "android";

    private DeviceUtils() {
        mContext = Contextor.getInstance().getContext();

    }

    public static DeviceUtils getInstance() {
        if (instance == null) {
            instance = new DeviceUtils();
        }
        return instance;
    }

    @SuppressLint("HardwareIds")
    public String getDeviceId() {
        return Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public String getDeviceName() {
        return Build.DEVICE;
    }

    public String getDeviceModel() {
        return Build.BRAND;
    }

    public String getDeviceOSVersion() {
        return Build.VERSION.RELEASE;
    }

    @SuppressLint("HardwareIds")
    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public String getPackageName() {
        return mContext.getPackageName();
    }
}
