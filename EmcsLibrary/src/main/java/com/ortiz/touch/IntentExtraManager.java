package com.ortiz.touch;

import android.content.Context;
import android.content.Intent;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by nakharin on 4/6/2017 AD.
 */

public class IntentExtraManager {

    private static IntentExtraManager instance = null;

    private Context mContext;

    private IntentExtraManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public static IntentExtraManager getInstance() {
        if (instance == null) {
            instance = new IntentExtraManager();
        }

        return instance;
    }

    public static class ExtraType {
        /**
         * 0 = load from Internal Storage
         * 1 = load from Web Service (Link & URL)
         */
        public static final String IMAGE_TYPE = "IMAGE_TYPE";
        public static final String IMAGE_NAME = "IMAGE_NAME";
        public static final String IMAGE_PATH = "IMAGE_PATH";
    }

    public int getImageType(Intent intent) {
        return intent.getIntExtra(ExtraType.IMAGE_TYPE, 0);
    }

    public String getImageName(Intent intent) {
        if (intent.hasExtra(ExtraType.IMAGE_NAME)) {
            return intent.getStringExtra(ExtraType.IMAGE_NAME);
        }

        return "";
    }

    public String getImagePath(Intent intent) {
        if (intent.hasExtra(ExtraType.IMAGE_PATH)) {
            return intent.getStringExtra(ExtraType.IMAGE_PATH);
        }

        return "";
    }
}
