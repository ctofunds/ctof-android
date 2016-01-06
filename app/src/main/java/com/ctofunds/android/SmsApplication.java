package com.ctofunds.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.ctofunds.android.markdown.MarkdownParser;
import com.ctofunds.android.markdown.impl.DefaultMarkdownParser;
import com.ctofunds.android.serializer.JacksonSerializer;
import com.ctofunds.android.serializer.Serializer;
import com.ctofunds.android.service.AccountService;
import com.ctofunds.android.service.CodeService;
import com.ctofunds.android.service.impl.AccountServiceImpl;
import com.ctofunds.android.service.impl.CodeServiceImpl;
import com.ctofunds.android.utility.ImageLruCache;
import com.ctofunds.android.utility.TimeUtils;
import com.google.common.base.Preconditions;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by qianhao.zhou on 12/15/15.
 */
public class SmsApplication extends Application {

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_IMAGE_CACHE_DIR = "volley-image";
    private static final String DEFAULT_NORMAL_CACHE_DIR = "volley-normal";

    private volatile static SmsApplication instance;

    public static SmsApplication getInstance() {
        return Preconditions.checkNotNull(instance, "instance has not been initialized in Thread " + Thread.currentThread().getName() + " at " + System.currentTimeMillis());
    }

    private RequestQueue imageRequestQueue;
    private RequestQueue normalRequestQueue;
    private AccountService accountService;
    private CodeService codeService;
    private Serializer serializer;
    private ImageLoader imageLoader;
    private EventBus eventBus;
    private MarkdownParser markdownParser;
    private TimeUtils timeUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        imageRequestQueue = initImageRequestQueue();
        normalRequestQueue = initNormalRequestQueue();
        accountService = new AccountServiceImpl(this.getApplicationContext());
        codeService = new CodeServiceImpl(this.getApplicationContext());
        serializer = new JacksonSerializer();
        imageLoader = new ImageLoader(imageRequestQueue, new ImageLruCache(1024 * 1024 * 4));
        eventBus = new EventBus();
        markdownParser = new DefaultMarkdownParser();
        timeUtils = new TimeUtils(getApplicationContext().getResources());
        instance = this;
        Log.d("SmsApplication", "SmsApplication has been initialized at " + System.currentTimeMillis());
    }

    private RequestQueue initImageRequestQueue() {
        Context context = this.getApplicationContext();
        File cacheDir = new File(context.getCacheDir(), DEFAULT_IMAGE_CACHE_DIR);

        Network network = new BasicNetwork(new HurlStack());

        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        queue.start();

        return queue;
    }

    private RequestQueue initNormalRequestQueue() {
        Context context = this.getApplicationContext();
        File cacheDir = new File(context.getCacheDir(), DEFAULT_NORMAL_CACHE_DIR);

        Network network = new BasicNetwork(new HurlStack());

        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        queue.start();

        return queue;
    }

    public static final RequestQueue getImageRequestQueue() {
        return getInstance().imageRequestQueue;
    }

    public static final RequestQueue getNormalRequestQueue() {
        return getInstance().normalRequestQueue;
    }

    public static final ImageLoader getImageLoader() {
        return getInstance().imageLoader;
    }

    public static final AccountService getAccountService() {
        return getInstance().accountService;
    }

    public static final Serializer getSerializer() {
        return getInstance().serializer;
    }

    public static final CodeService getCodeService() {
        return getInstance().codeService;
    }

    public static final EventBus getEventBus() {
        return getInstance().eventBus;
    }

    public static final MarkdownParser getMarkdownParser() {
        return getInstance().markdownParser;
    }

    public static TimeUtils getTimeUtils() {
        return getInstance().timeUtils;
    }
}
