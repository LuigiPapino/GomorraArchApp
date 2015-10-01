package com.spranga.gomorraarch.data.schematicProvider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Luigi Papino on 22/07/15.
 */
public interface JsonIdColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    String ID = "id";
    @DataType(DataType.Type.TEXT)
    String JSON = "json";
}
