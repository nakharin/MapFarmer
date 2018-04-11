package com.emcsthai.emcslibrary.Model.Utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by nakarin on 3/27/2017 AD.
 */

public class ToastUtils {

    private static ToastUtils instance = null;

    private Context mContext;

    private ToastUtils() {
        mContext = Contextor.getInstance().getContext();
    }

    public static ToastUtils getInstance() {
        if (instance == null) {
            instance = new ToastUtils();
        }
        return instance;
    }

    public void showText(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public void showText(@StringRes int strRedId) {
        Toast.makeText(mContext, strRedId, Toast.LENGTH_SHORT).show();
    }
}
