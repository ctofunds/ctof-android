package com.ctofunds.android.utility;

import java.io.File;

/**
 * Created by qianhao.zhou on 12/16/15.
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
}
