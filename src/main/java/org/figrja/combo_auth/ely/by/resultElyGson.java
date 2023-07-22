package org.figrja.combo_auth.ely.by;

import com.mojang.authlib.properties.Property;

import java.util.UUID;

public class resultElyGson {

    private String error;

    private String errorMessage;

    String id;

    private UUID Id;

    private String name;

    private Property[] properties;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public Property[] getProperties() {
        return properties;
    }

    public String getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}
