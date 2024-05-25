package org.figrja.combo_auth.config;

import com.google.gson.annotations.SerializedName;
import org.figrja.combo_auth.ely.by.propery;

import java.util.HashMap;
import java.util.List;

public class configGson {

    private List<String> AuthList;

    private HashMap<String,AuthSchemaList> AuthSchema;

    private String gebug;

    @SerializedName("AuthSchema")
    public HashMap<String,AuthSchemaList> getAuthSchema() {
        return AuthSchema;
    }

    @SerializedName("gebug")
    public String getGebugStatus() {
        return gebug;
    }

    @SerializedName("AuthList")
    public List<String> getAuthList() {
        return AuthList;
    }
}
