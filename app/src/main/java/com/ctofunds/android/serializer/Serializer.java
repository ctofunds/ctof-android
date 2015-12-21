package com.ctofunds.android.serializer;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public interface Serializer {

    byte[] serialize(Object obj);

    <T> T deserialize(Class<T> type, byte[] data);
}
