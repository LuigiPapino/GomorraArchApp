package com.spranga.gomorraarch.data.stores;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.spranga.gomorraarch.data.base.store.ContentProviderStoreBase;
import com.spranga.gomorraarch.data.schematicProvider.JsonIdColumns;
import com.spranga.gomorraarch.data.schematicProvider.MainProvider;
import com.spranga.gomorraarch.pojo.NetworkRequestStatus;


/**
 * Created by ttuo on 26/04/15.
 */
public class NetworkRequestStatusStore extends ContentProviderStoreBase<NetworkRequestStatus, Integer> {
    private static final String TAG = NetworkRequestStatusStore.class.getSimpleName();

    public NetworkRequestStatusStore(@NonNull ContentResolver contentResolver) {
        super(contentResolver);
    }

    @NonNull
    @Override
    protected Integer getIdFor(@NonNull NetworkRequestStatus item) {
        return item.getUri().hashCode();
    }

    @NonNull
    @Override
    public Uri getContentUri() {
        return MainProvider.NetworkRequestStatuses.NETWORK_REQUEST_STATUSES;
    }

    @Nullable
    @Override
    protected NetworkRequestStatus query(@NonNull Uri uri) {
        return super.query(uri);
    }

    @Override
    public void insertOrUpdate(@NonNull NetworkRequestStatus item) {

        Log.v(TAG, "insertOrUpdate(" + item.getStatus() + ", " + item.getUri() + ")");
        super.insertOrUpdate(item);
    }

    @NonNull
    @Override
    protected String[] getProjection() {
        return new String[] { JsonIdColumns.ID, JsonIdColumns.JSON };
    }

    @NonNull
    @Override
    protected ContentValues getContentValuesForItem(NetworkRequestStatus item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(JsonIdColumns.ID, item.getUri().hashCode());
        contentValues.put(JsonIdColumns.JSON, new Gson().toJson(item));
        return contentValues;
    }

    @NonNull
    @Override
    protected NetworkRequestStatus read(Cursor cursor) {
        final String json = cursor.getString(cursor.getColumnIndex(JsonIdColumns.JSON));
        final NetworkRequestStatus value = new Gson().fromJson(json, NetworkRequestStatus.class);
        return value;
    }

    @NonNull
    @Override
    public Uri getUriForKey(@NonNull Integer id) {

        return MainProvider.NetworkRequestStatuses.withId(id);
    }
}
