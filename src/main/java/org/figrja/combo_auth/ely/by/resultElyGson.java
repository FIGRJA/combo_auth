package org.figrja.combo_auth.ely.by;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import java.util.UUID;

public class resultElyGson {

    private String error;

    private String errorMessage;

    String id;

    private UUID Id;

    private String name;

    private propery[] properties;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public PropertyMap getProperties() {
        PropertyMap pm = new PropertyMap();
        for (propery p :properties){
            pm.put(p.name(),new Property(p.name(),p.value(),p.signature()));
        }
        return pm;
    }

    public String getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}


