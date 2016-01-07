package com.ctofunds.android.widget.adapater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by qianhao.zhou on 1/7/16.
 */
public abstract class DynamicLoadingAdapter<T> extends BaseAdapter {

    private final List<AdapterItem<T>> items = Lists.newArrayList();
    private final Context context;
    private final LayoutInflater layoutInflater;
    private volatile boolean requestSubmitted = false;

    public DynamicLoadingAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public AdapterItem<T> getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("adapter", "position:" + position);
        AdapterItem<T> item = getItem(position);
        if (position == getCount() - 1 && hasMore()) {
            submitRequest();
        }
        switch (item.type) {
            case DATA:
                return onGetDataView(item.data, convertView, parent);
            case EMPTY:
                return onGetEmptyView(convertView);
            case ERROR:
                return onGetErrorView(convertView);
            case PROGRESS:
                return onGetProgressView(convertView);
        }
        return convertView;
    }

    public final Context getContext() {
        return context;
    }

    public final LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public final void init() {
        items.clear();
        if (hasMore()) {
            showProgress();
        }
        requestSubmitted = false;
        notifyDataSetChanged();
    }

    protected abstract View onGetDataView(T data, View convertView, ViewGroup parent);

    protected abstract View onGetProgressView(View convertView);

    protected abstract View onGetErrorView(View convertView);

    protected abstract View onGetEmptyView(View convertView);

    protected abstract void onSubmitRequest();

    protected abstract boolean hasMore();

    protected final void onRequestFailed() {
        this.requestSubmitted = false;
        tryRemove(AdapterItem.Type.PROGRESS);
        showError();
    }

    protected final void onRequestSucceed() {
        this.requestSubmitted = false;
        tryRemove(AdapterItem.Type.PROGRESS);
    }

    protected void showProgress() {
        tryAdd(AdapterItem.Type.PROGRESS);
    }

    protected void showError() {
        tryAdd(AdapterItem.Type.ERROR);
    }

    protected void showEmpty() {
        tryAdd(AdapterItem.Type.EMPTY);
    }

    protected final void addAll(List<T> items) {
        tryRemoveAnyBut(AdapterItem.Type.DATA);
        if (items != null) {
            this.items.addAll(Lists.transform(items, new Function<T, AdapterItem<T>>() {
                @Override
                public AdapterItem<T> apply(T input) {
                    return new AdapterItem<>(input);
                }
            }));
        }
        notifyDataSetChanged();
    }

    protected final AdapterItem.Type getType() {
        if (items.size() > 0) {
            return items.get(items.size() - 1).type;
        } else {
            return null;
        }
    }

    private void submitRequest() {
        if (!requestSubmitted) {
            requestSubmitted = true;
            showProgress();
            onSubmitRequest();
        }
    }

    private void tryRemove(AdapterItem.Type type) {
        if (items.size() > 0 && items.get(items.size() - 1).type == type) {
            items.remove(items.size() - 1);
        }
    }

    private void tryRemoveAnyBut(AdapterItem.Type type) {
        if (type != AdapterItem.Type.EMPTY) {
            tryRemove(AdapterItem.Type.EMPTY);
        }
        if (type != AdapterItem.Type.ERROR) {
            tryRemove(AdapterItem.Type.ERROR);
        }
        if (type != AdapterItem.Type.PROGRESS) {
            tryRemove(AdapterItem.Type.PROGRESS);
        }
    }

    private void tryAdd(AdapterItem.Type type) {
        tryRemoveAnyBut(type);
        if (items.size() == 0 || items.get(items.size() - 1).type != type) {
            items.add(new AdapterItem(type));
        }
        notifyDataSetChanged();
    }

}
