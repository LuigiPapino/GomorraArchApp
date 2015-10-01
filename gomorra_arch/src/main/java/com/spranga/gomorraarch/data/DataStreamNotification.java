package com.spranga.gomorraarch.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ttuo on 06/05/15.
 */
public class DataStreamNotification<T> {

    private static final String TAG = DataStreamNotification.class.getName();

    private enum Type {
        FETCHING_START, FETCHING_ERROR, ON_NEXT
    }

    @NonNull
    final private Type type;
    final private T value;
    final private Throwable error;
    final private Integer networkErrorCode;
    final private String networkErrorMessage;

    public Integer getNetworkErrorCode() {
        return networkErrorCode;
    }

    public String getNetworkErrorMessage() {
        return networkErrorMessage;
    }

    private DataStreamNotification(@NonNull Type type, T value, Throwable error, String networkErrorMessage, Integer networkErrorCode) {

        this.type = type;
        this.value = value;
        this.error = error;
        this.networkErrorCode = networkErrorCode;
        this.networkErrorMessage = networkErrorMessage;
    }

    @Nullable
    public T getValue() {
        return value;
    }

    @NonNull
    public static<T> DataStreamNotification<T> fetchingStart() {
        Log.i(TAG, "fetchingStart DataStreamNotification");

        return new DataStreamNotification<>(Type.FETCHING_START, null, null, null, null);
    }

    @NonNull
    public static<T> DataStreamNotification<T> onNext(T value) {
        Log.i(TAG, "onNext DataStreamNotification");

        return new DataStreamNotification<>(Type.ON_NEXT, value, null, null, null);
    }

    @NonNull
    public static<T> DataStreamNotification<T> fetchingError(Throwable error, Integer networkErrorCode, String networkErrorMessage) {
        Log.i(TAG, "fetchingError DataStreamNotification");
        return new DataStreamNotification<>(Type.FETCHING_ERROR, null, error, networkErrorMessage, networkErrorCode);
    }

    public boolean isFetchingStart() {
        return type.equals(Type.FETCHING_START);
    }

    public boolean isOnNext() {
        return type.equals(Type.ON_NEXT);
    }

    public boolean isFetchingError() {
        return type.equals(Type.FETCHING_ERROR);
    }

    @Nullable
    public Throwable getError() {
        return error;
    }
}
