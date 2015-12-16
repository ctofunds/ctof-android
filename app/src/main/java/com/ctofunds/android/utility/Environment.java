package com.ctofunds.android.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ctofunds.android.SmsApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by qianhao.zhou on 2/13/15.
 */
public final class Environment {

    public static Environment getInstance() {
        return SingletonHolder.instance;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getVersionName() {
        return packageInfo.versionName;
    }

    private static final String USER_AGENT_PATTERN = "SMS/1.0(&package& &version&; &model&; Android &android_version&; &source&)";

    private String imei;
    private String userAgent;
    private SmsApplication application;
    private PackageInfo packageInfo;

    private Environment() {
        application = SmsApplication.getInstance();

        try {
            packageInfo = application.getPackageManager().getPackageInfo(
                    application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("Environment", e);
            throw new RuntimeException("this should not happen", e);
        }
        this.userAgent = initUserAgent();
        this.imei = initImei();

    }

    public String getImei() {
        return imei;
    }

    public int screenWidthPixels() {
        Context c = application;
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int screenHeightPixels() {
        Context c = application;
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public float screenSensity() {
        Context c = application;
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        return dm.density;
    }

    private static class SingletonHolder {

        public static Environment instance = new Environment();
    }

    private String initUserAgent() {
        String result = USER_AGENT_PATTERN;
        result = result.replaceAll("&package&", application.getPackageName());
        result = result.replaceAll("&version&", packageInfo.versionName);
        result = result.replaceAll("&model&", escapeSource(android.os.Build.MODEL));
        result = result.replaceAll("&android_version&", Build.VERSION.RELEASE);
        result = result.replaceAll("&source&", source());
        return result;
    }

    /**
     * 手机的IMEI设备序列号
     * <p/>
     * 第一次启动时会保存该序列号，可以频繁调用
     *
     * @return IMEI or "00000000000000" if error
     */
    private String initImei() {
        // update cached imei when identity changed. including brand, model,
        // radio and system version
        String result;
        String deviceIdentity = Build.VERSION.RELEASE + ";" + Build.MODEL
                + ";" + Build.BRAND;
        if (deviceIdentity.length() > 64) {
            deviceIdentity = deviceIdentity.substring(0, 64);
        }
        if (deviceIdentity.indexOf('\n') >= 0) {
            deviceIdentity = deviceIdentity.replace('\n', ' ');
        }

        String cachedIdentity = null;
        String cachedImei = null;
        try {
            // do not use file storage, use cached instead
            File path = new File(application.getCacheDir(),
                    "cached_imei");
            FileInputStream fis = new FileInputStream(path);
            byte[] buf = new byte[1024];
            int l = fis.read(buf);
            fis.close();
            String str = new String(buf, 0, l, "UTF-8");
            int a = str.indexOf('\n');
            cachedIdentity = str.substring(0, a);
            int b = str.indexOf('\n', a + 1);
            cachedImei = str.substring(a + 1, b);
        } catch (Exception e) {
        }

        if (deviceIdentity.equals(cachedIdentity)) {
            result = cachedImei;
        } else {
            result = null;
        }

        // cache fail, read from telephony manager
        if (result == null) {
            try {
                TelephonyManager tel = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
                result = tel.getDeviceId();
                if (result != null) {
                    if (result.length() < 8) {
                        result = null;
                    } else {
                        char c0 = result.charAt(0);
                        boolean allSame = true;
                        for (int i = 0, n = result.length(); i < n; i++) {
                            if (c0 != result.charAt(i)) {
                                allSame = false;
                                break;
                            }
                        }
                        if (allSame)
                            result = null;
                    }
                }
            } catch (Exception e) {
            }
            if (result != null) {
                try {
                    File path = new File(application
                            .getCacheDir(), "cached_imei");
                    FileOutputStream fos = new FileOutputStream(path);
                    String str = deviceIdentity + "\n" + result + "\n";
                    fos.write(str.getBytes("UTF-8"));
                    fos.close();
                } catch (Exception e) {
                }
            } else {
                File path = new File(application
                        .getCacheDir(), "cached_imei");
                path.delete();
            }
        }

        if (result == null) {
            result = "00000000000000";
        }
        return result;
    }

    private String source() {
        try {
            InputStream ins = application.getAssets().open("source.txt");
            if (ins != null) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(ins));
                    return escapeSource(br.readLine());
                } finally {
                    if (br != null) {
                        br.close();
                    }
                }
            } else {
                Log.d("Environment", "cannot find source.txt");
            }
        } catch (IOException e) {
            Log.i("Environment", "cannot find source.txt");
        }
        return "Google_Play";
    }

    private static String escapeSource(String src) {
        StringBuilder sb = new StringBuilder();
        for (char c : src.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append(c);
            } else if (c >= 'A' && c <= 'Z') {
                sb.append(c);
            } else if (c >= '0' && c <= '9') {
                sb.append(c);
            } else if (c == '.' || c == '_' || c == '-' || c == '/') {
                sb.append(c);
            } else if (c == ' ') {
                sb.append('_');
            }
        }
        return sb.toString();
    }
}
