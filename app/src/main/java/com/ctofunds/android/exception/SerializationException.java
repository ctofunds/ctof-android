package com.ctofunds.android.exception;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public class SerializationException extends RuntimeException {

    public SerializationException() {
    }

    public SerializationException(String detailMessage) {
        super(detailMessage);
    }

    public SerializationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SerializationException(Throwable throwable) {
        super(throwable);
    }
}
