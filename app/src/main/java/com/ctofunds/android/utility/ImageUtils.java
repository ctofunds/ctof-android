package com.ctofunds.android.utility;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by qianhao.zhou on 3/4/15.
 */
public final class ImageUtils {

    private ImageUtils() {

    }

    public static byte[] bitmapToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        try {
            return output.toByteArray();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                Log.w("ImageUtils", "error close output stream", e);
            }
        }
    }

    /**
     * 对图片进行裁剪，返回正方形图片
     *
     * @param srcBmp 源图片
     * @param width  目标宽度
     * @param height 目标高度
     * @return 裁剪后的图片
     */
    public static Bitmap cropCenterSquareBitmap(Bitmap srcBmp, int width, int height) {
        return cropSquareBitmap(cropCenterBitmap(srcBmp, width, height));
    }

    public static Bitmap cropCenterBitmap(Bitmap srcBmp, int width, int height) {
        int srcWidth = srcBmp.getWidth();
        int srcHeight = srcBmp.getHeight();

        if ((srcWidth < width) && (srcHeight < height)) {
            return srcBmp;
        }

        int x = Math.max((srcWidth - width) / 2, 0);
        int y = Math.max((srcHeight - height) / 2, 0);
        int w = Math.min(srcWidth, width);
        int h = Math.min(srcHeight, height);

        Bitmap dstBmp = Bitmap.createBitmap(srcBmp, x, y, w, h);
        return dstBmp;
    }

    public static Bitmap scaleIn(Bitmap src, int width, int height) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if (srcWidth <= width && srcHeight <= height) {
            return src;
        }
        float wRatio = (float) width / srcWidth;
        float hRatio = (float) height / srcHeight;
        float ratio = wRatio < hRatio ? wRatio : hRatio;
        Bitmap result = Bitmap.createScaledBitmap(src, ((int) (srcWidth * ratio)), ((int) (srcHeight * ratio)), false);
        return result;
    }

    public static Bitmap scaleFit(Bitmap src, int width, int height) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if (srcWidth <= width && srcHeight <= height) {
            return src;
        }
        float wRatio = (float) width / srcWidth;
        float hRatio = (float) height / srcHeight;
        float ratio = wRatio > hRatio ? wRatio : hRatio;
        Bitmap result = Bitmap.createScaledBitmap(src, ((int) (srcWidth * ratio)), ((int) (srcHeight * ratio)), false);
        return result;
    }

    public static Bitmap cropSquareBitmap(Bitmap srcBmp) {
        Bitmap dstBmp = null;
        int srcWidth = srcBmp.getWidth();
        int srcHeight = srcBmp.getHeight();

        if (srcWidth == srcHeight) {
            return srcBmp;
        }

        if (srcWidth >= srcHeight) {
            dstBmp = Bitmap.createBitmap(srcBmp, (srcWidth / 2) - (srcHeight / 2), 0, srcHeight,
                    srcHeight);

        } else {
            dstBmp = Bitmap.createBitmap(srcBmp, 0, (srcHeight / 2) - (srcWidth / 2), srcWidth,
                    srcWidth);
        }

        return dstBmp;
    }

    private static final String IMAGE_RESIZE_PATTERN = "@%dw_%dh_%dQ";

    public static final String getResizedImageUrl(String url, int width, int height, int quality) {
        return url + String.format(IMAGE_RESIZE_PATTERN, width, height, quality);
    }

    public static final String getAvatarUrl(String url) {
        return getResizedImageUrl(url, 160, 160, 100);
    }

    public static final String getCoverUrl(String url) {
        return getResizedImageUrl(url, 1000, 1000, 100);
    }

}
