package com.emcsthai.emcslibrary.Model.Utils;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import timber.log.Timber;

/**
 * Created by nakarin on 9/20/2016 AD.
 */

public class StringUtils {

    private static StringUtils instance = null;

    private Context mContext;

    private StringUtils() {
        mContext = Contextor.getInstance().getContext();
    }

    public static StringUtils getInstance() {
        if (instance == null) {
            instance = new StringUtils();
        }
        return instance;
    }

    public ArrayList<String> getKeyHashMapFromXML(int stringArrayResourceId) {

        ArrayList<String> keyList = new ArrayList<>();

        String[] stringArray = mContext.getResources().getStringArray(
                stringArrayResourceId);

        for (String string : stringArray) {
            String[] splitResult = string.split("\\|", 2);
            keyList.add(splitResult[0]);
        }

        Timber.d("keyList : " + keyList.toString());

        return keyList;
    }

    public ArrayList<String> getValuesHashMapFromXML(int stringArrayResourceId) {

        ArrayList<String> valuesList = new ArrayList<>();

        String[] stringArray = mContext.getResources().getStringArray(
                stringArrayResourceId);

        for (String string : stringArray) {
            String[] splitResult = string.split("\\|", 2);
            valuesList.add(splitResult[1]);
        }

        Timber.d("valuesList : " + valuesList.toString());

        return valuesList;
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
}
