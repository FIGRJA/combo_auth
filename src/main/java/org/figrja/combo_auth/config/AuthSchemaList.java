package org.figrja.combo_auth.config;

import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.figrja.combo_auth.ely.by.propery;

public class AuthSchemaList {
    private String url_check;

    private String url_property;

    private Property[] AddProperty;

    @SerializedName("AddProperty")
    public Property[] getAddProperty() {
        return AddProperty;
    }

    public PropertyMap getProperty(){
        PropertyMap map = new PropertyMap();
        for (Property p : AddProperty){
            map.put(p.getName(),p);
        }
        return map;
    }

    @SerializedName("url_check")
    public String getUrlCheck() {
        return url_check;
    }

    @SerializedName("url_property")
    public String getUrlProperty() {
        return url_property;
    }
}
