package com.spranga.gomorraarch.data.utils;

import android.support.annotation.NonNull;

import com.spranga.gomorraarch.data.DataStreamNotification;
import com.spranga.gomorraarch.pojo.NetworkRequestStatus;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ttuo on 06/05/15.
 */
public class DataLayerUtils {
    private static final String TAG = DataLayerUtils.class.getName();

    private DataLayerUtils() {

    }

    @NonNull
    public static<T> Observable<DataStreamNotification<T>> createDataStreamNotificationObservable(
            @NonNull Observable<NetworkRequestStatus> networkRequestStatusObservable,
            @NonNull Observable<T> valueObservable) {

        final Observable<DataStreamNotification<T>> networkStatusStream =
                networkRequestStatusObservable
                        .filter(networkRequestStatus -> !networkRequestStatus.isCompleted())
                        .map(new Func1<NetworkRequestStatus, DataStreamNotification<T>>() {
                            @Override
                            public DataStreamNotification<T> call(NetworkRequestStatus networkRequestStatus) {
                                if (networkRequestStatus.isError()) {
                                    return DataStreamNotification.fetchingError(null, networkRequestStatus.getErrorCode(), networkRequestStatus.getErrorMessage());
                                } else if (networkRequestStatus.isOngoing()) {
                                    return DataStreamNotification.fetchingStart();
                                } else {
                                    return null;
                                }
                            }
                        })
                        .filter(dataStreamNotification -> dataStreamNotification != null);
        final Observable<DataStreamNotification<T>> valueStream =
                valueObservable.map(DataStreamNotification::onNext);
        return Observable.merge(networkStatusStream, valueStream);
    }

    @NonNull
    public static<T> Observable<DataStreamNotification<T>> createDataStreamNotificationObservable(
            @NonNull Observable<NetworkRequestStatus> networkRequestStatusObservable) {

        final Observable<DataStreamNotification<T>> networkStatusStream =
                networkRequestStatusObservable
                        .filter(networkRequestStatus -> !networkRequestStatus.isCompleted())
                        .map(new Func1<NetworkRequestStatus, DataStreamNotification<T>>() {
                            @Override
                            public DataStreamNotification<T> call(NetworkRequestStatus networkRequestStatus) {
                                if (networkRequestStatus.isError()) {
                                    return DataStreamNotification.fetchingError(null, networkRequestStatus.getErrorCode(), networkRequestStatus.getErrorMessage());
                                } else if (networkRequestStatus.isOngoing()) {
                                    return DataStreamNotification.fetchingStart();
                                } else if (networkRequestStatus.isCompleted()){
                                    return DataStreamNotification.onNext(null);
                                }
                                else {
                                    return null;
                                }
                            }
                        })
                        .filter(dataStreamNotification -> dataStreamNotification != null);

        return networkStatusStream;
    }
}
