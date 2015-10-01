package com.spranga.gomorraarch.data.schematicProvider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Luigi Papino on 22/07/15.
 */
@Database(version = MainDatabase.VERSION)
public final class MainDatabase {
    public static final  int VERSION = 1;

    @Table(NetworkRequestStatusColumns.class) public static final String NETWORK_REQUEST_STATUSES = "networkRequestStatuses";



}
