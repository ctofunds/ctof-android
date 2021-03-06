package com.ctofunds.android.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public final class Constants {

    public static final String LOGOUT = "com.ctofunds.android.logout";

    private Constants(){}

    public static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<>();

    public static final DateFormat getDateTimeFormat() {
        if (dateFormat.get() == null) {
            dateFormat.set(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT_PATTERN));
        }
        return dateFormat.get();
    }

    public static final DateFormat getDateFormat() {
        if (dateFormat.get() == null) {
            dateFormat.set(new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN));
        }
        return dateFormat.get();
    }

    public static final int REQUEST_LOGIN = 1;

    public static final int TOKEN_EXPIRATION = 60 * 24 * 7;//one week

}
