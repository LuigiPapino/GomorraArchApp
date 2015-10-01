package com.spranga.gomorraarch.data.base.store;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by ttuo on 26/04/15.
 */
abstract public class ContentProviderStoreBase<T, U> {
    private static final String TAG = ContentProviderStoreBase.class.getSimpleName();

    // the subject is added when getStream was requested and onNext is invoked on subject when there is a change in contentObserver
    final private ConcurrentMap<Uri, Subject<T, T>> subjectMap = new ConcurrentHashMap<>();

    @NonNull
    final protected ContentResolver contentResolver;

    public ContentProviderStoreBase(@NonNull ContentResolver contentResolver) {

        this.contentResolver = contentResolver;
        this.contentResolver.registerContentObserver(getContentUri(), true, getContentObserver());
    }

    @NonNull
    private ContentObserver getContentObserver() {
        return new ContentObserver(createHandler(this.getClass().getSimpleName())) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                Log.v(TAG, "onChange(" + uri + ")");

                if (subjectMap.containsKey(uri)) {
                    subjectMap.get(uri).onNext(query(uri));
                }
            }
        };
    }

    @NonNull
    private static Handler createHandler(@NonNull String name) {

        HandlerThread handlerThread = new HandlerThread(name);
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    public void put(T item) {
        insertOrUpdate(item);
    }

    @NonNull
    public Observable<T> getStream(U id) {
        return  getStream(id, false);
    }

    @NonNull
    public Observable<T> getStream(U id, boolean forceWithStart) {
        Log.v(TAG, "getStream(" + id + ")");
        final T item = query(id);
        final Observable<T> observable = lazyGetSubject(id);
        if (item != null && !forceWithStart) {
            Log.v(TAG, "Found existing item for id=" + id);
            return observable.startWith(item);
        }else if (forceWithStart){
            return  observable.startWith(item);
        }

        return observable;
    }

    @NonNull
    private Observable<T> lazyGetSubject(U id) {
        Log.v(TAG, "lazyGetSubject(" + id + ")");
        final Uri uri = getUriForKey(id);
        subjectMap.putIfAbsent(uri, PublishSubject.<T>create());
        return subjectMap.get(uri);
    }

    public void insertOrUpdate(@NonNull T item) {

        Uri uri = getUriForKey(getIdFor(item));
        Log.v(TAG, "insertOrUpdate to " + uri);
        ContentValues values = getContentValuesForItem(item);
        Log.v(TAG, "values(" + values + ")");
        if (contentResolver.update(uri, values, null, null) == 0) {
            final Uri resultUri = contentResolver.insert(uri, values);
            Log.v(TAG, "Inserted at " + resultUri);
        } else {
            Log.v(TAG, "Updated at " + uri);
        }

    }

    public void delete(@NonNull U id){
        Uri uri = getUriForKey(id);
        Log.v(TAG, "delete " + uri);
        int count = contentResolver.delete(uri, null, null);
        Log.v(TAG, String.format("deleted count:%d uri:%s", count, uri));
    }

    @Nullable
    protected T query(@NonNull U id) {

        return query(getUriForKey(id));
    }

    public boolean contains(@NonNull U id){
        return  query(id) != null;
    }

    @Nullable
    protected T query(@NonNull Uri uri) {

        Cursor cursor = contentResolver.query(uri,
                getProjection(), null, null, null);

        T value = null;
        if (cursor != null && cursor.moveToFirst()) {
            value = read(cursor);
            cursor.close();
        }
        if (value == null) {
            Log.v(TAG, "Could not find with id: " + uri);
        }
        return value;
    }

    @NonNull
    abstract public Uri getUriForKey(@NonNull U id);

    @NonNull
    abstract protected U getIdFor(@NonNull T item);

    @NonNull
    abstract protected Uri getContentUri();

    @NonNull
    abstract protected String[] getProjection();

    @NonNull
    abstract protected ContentValues getContentValuesForItem(T item);

    @NonNull
    abstract protected T read(Cursor cursor);

}
