package com.ctofunds.android.module.topic;

/**
 * Created by yangliu on 15/12/29.
 */
public class BannerPageObj {
    String imageUrl;
    String linkUrl;
    String title;
    String subTitle;

    public BannerPageObj() {
    }

    public BannerPageObj(String imageUrl, String linkUrl, String title, String subTitle) {
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.title = title;
        this.subTitle = subTitle;
    }
}
