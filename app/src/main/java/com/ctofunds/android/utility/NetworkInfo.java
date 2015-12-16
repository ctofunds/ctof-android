package com.ctofunds.android.utility;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by qianzhou on 3/21/15.
 */
public final class NetworkInfo {

    private NetworkInfo() {}

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            android.net.NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
