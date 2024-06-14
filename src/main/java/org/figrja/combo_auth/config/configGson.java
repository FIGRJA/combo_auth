package org.figrja.combo_auth.config;

import com.google.gson.annotations.SerializedName;
import org.figrja.combo_auth.ely.by.propery;

import java.util.HashMap;
import java.util.List;

public class configGson {

    private List<String> AuthList;

    private HashMap<String,AuthSchemaList> AuthSchema;

    private String debug;

    @SerializedName("AuthSchema")
    public HashMap<String,AuthSchemaList> getAuthSchema() {
        return AuthSchema;
    }

    @SerializedName("debug")
    public String getGebugStatus() {
        return debug;
    }

    @SerializedName("AuthList")
    public List<String> getAuthList() {
        return AuthList;
    }
}
