package org.figrja.combo_auth.ely.by;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

public class propery {

    private String name;
    private String value;
    private String signature;

    @SerializedName("name")
    public String name() {
        return this.name;
    }

    @SerializedName("value")
    public String value() {
        return this.value;
    }

    @SerializedName("signature")
    @Nullable
    public String signature() {
        return this.signature;
    }
    public propery(String name, String value) {
        this(name, value, (String)null);
    }

    public propery(String name, String value, @Nullable String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

}
