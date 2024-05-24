package org.figrja.combo_auth.config;

import org.figrja.combo_auth.ely.by.propery;

import java.util.HashMap;
import java.util.List;

public class configGson {
    private List<String> AuthList;

    private HashMap<String,AuthSchemaList> AuthSchema;

    private String gebug;

    public HashMap<String,AuthSchemaList> getAuthSchema() {
        return AuthSchema;
    }

    public String getGebugStatus() {
        return gebug;
    }

    public List<String> getAuthList() {
        return AuthList;
    }
}
