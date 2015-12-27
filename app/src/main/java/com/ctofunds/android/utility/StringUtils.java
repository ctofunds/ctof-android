package com.ctofunds.android.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public final class StringUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private StringUtils() {
    }

    public static boolean isEmpty(CharSequence charSequence) {
        if (charSequence == null) {
            return true;
        }
        for (int i = 0, length = charSequence.length(); i < length; i++) {
            if (charSequence.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmailValid(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.find();
    }
}
