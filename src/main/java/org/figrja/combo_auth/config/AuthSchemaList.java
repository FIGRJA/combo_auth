package org.figrja.combo_auth.config;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.figrja.combo_auth.ely.by.propery;

public class AuthSchemaList {
    private String url_check;

    private String url_property;

    private PropertyMap AddProperty;

    public PropertyMap getProperty() {
        return AddProperty;
    }

    public String getUrlCheck() {
        return url_check;
    }

    public String getUrlProperty() {
        return url_property;
    }
}
