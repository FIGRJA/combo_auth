package org.figrja.combo_auth.config;

import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.figrja.combo_auth.ely.by.propery;

public class AuthSchemaList {
    private String url_check;

    private String url_property;

    private PropertyMap AddProperty;

    @SerializedName("AddProperty")
    public PropertyMap getProperty() {
        return AddProperty;
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
