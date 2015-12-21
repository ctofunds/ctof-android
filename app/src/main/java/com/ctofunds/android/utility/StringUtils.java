package com.ctofunds.android.utility;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public final class StringUtils {
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
}
