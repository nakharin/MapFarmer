package com.emcsthai.emcslibrary.Model.Utils;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nakarin on 9/20/2016 AD.
 */

public class DateTimeUtils {

    private static DateTimeUtils instance = null;

    private Context mContext = null;

    private DateTimeUtils() {
        mContext = Contextor.getInstance().getContext();
    }

    public static DateTimeUtils getInstance() {
        if (instance == null) {
            instance = new DateTimeUtils();
        }
        return instance;
    }

    public String getDateNow() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        Date date = new Date();

        return simpleDateFormat.format(date);
    }

    public String getTimeNow() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    public String getDateTimeNow() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());

        Date date = new Date();

        return simpleDateFormat.format(date);
    }

    public String convertStringFromDateTime(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
            return df.format(date);
        } else {
            return null;
        }
    }

    public String convertStringFromTime(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return df.format(date);
        } else {
            return null;
        }
    }

    public String convertStringFromDate(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return simpleDateFormat.format(date);
        } else {
            return null;
        }
    }

    public Date convertDateFromString(String strDate) {
        if (strDate != null && !strDate.equals("")) {
            DateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            try {
                return formatter.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
