package com.ctofunds.android.widget.adapater;

/**
 * Created by qianhao.zhou on 1/7/16.
 */
public class AdapterItem<T> {

    public enum Type {
        DATA,
        PROGRESS,
        ERROR,
        EMPTY
    }

    final Type type;
    final T data;

    public AdapterItem(T data) {
        this.type = Type.DATA;
        this.data = data;
    }

    AdapterItem(Type type) {
        this.type = type;
        this.data = null;
    }
}
