package com.spranga.gomorraarch.data.schematicProvider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


/**
 * Created by Luigi Papino on 22/07/15.
 */
@ContentProvider(authority = MainProvider.AUTHORITY, database = MainDatabase.class)
public class MainProvider {

    public static final String AUTHORITY = "com.spranga.gomorraarch.data.schematicProvider.MainProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MainDatabase.NETWORK_REQUEST_STATUSES)
    public static class NetworkRequestStatuses{
        @ContentUri(
                path = MainDatabase.NETWORK_REQUEST_STATUSES,
                type = "vnd.android.cursor.dir/networkrequeststatus",
                defaultSort = JsonIdColumns.ID + " ASC"
        )
        public static final Uri NETWORK_REQUEST_STATUSES = Uri.parse(BASE_CONTENT_URI + "/" + MainDatabase.NETWORK_REQUEST_STATUSES);

        @InexactContentUri(
                path = MainDatabase.NETWORK_REQUEST_STATUSES + "/*",
                name = "NETWORK_REQUEST_STATUSES_ID",
                type = "vnd.android.cursor.item/networkrequeststatus",
                whereColumn = NetworkRequestStatusColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(MainDatabase.NETWORK_REQUEST_STATUSES, String.valueOf(id));
        }
    }


}
