package org.figrja.combo_auth.ely.by;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.properties.Property;
import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.ConfigPro;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class auth_api {

    private static URL CHECK_URL = httpHelper.constantURL("http://minecraft.ely.by/session/hasJoined");
    private static String PROPERTY_URL = "http://skinsystem.ely.by/textures/signed/{0}";

    private static ConfigPro CONFIG = auth.getConfigPro();

    public static GameProfile hasJoinedServer(GameProfile user, String serverId) throws AuthenticationUnavailableException {
        Map<String, Object> arguments = new HashMap();
        arguments.put("username", user.getName());
        arguments.put("serverId", serverId);
        CHECK_URL = CONFIG.isString("CHECK_URL") == null ? CHECK_URL : httpHelper.constantURL(CONFIG.isString("CHECK_URL"));
        URL url = httpHelper.concatenateURL(CHECK_URL, httpHelper.buildQuery(arguments));

        try {
            resultElyGson response = httpHelper.makeRequest(url);
            if (response != null && response.getId() != null) {
                final GameProfile result = new GameProfile(response.getId(), response.getName());

                if (response.getProperties() != null) {
                    Property[] properties;
                    if (CONFIG.isBoolean("singEnable")) {
                        PROPERTY_URL = CONFIG.isString("PROPERTY_URL") == null ? PROPERTY_URL : CONFIG.isString("PROPERTY_URL");
                        URL p_url = httpHelper.concatenateURL(httpHelper.constantURL(MessageFormat.format(PROPERTY_URL,user.getName(),response.getId())), httpHelper.buildQuery(null));
                        resultElyGson pr = httpHelper.makeRequest(p_url);
                        properties = pr.getProperties();
                    }else {
                        properties = response.getProperties();
                    }
                    result.getProperties().putAll(properties2map(properties));
                }

                return result;
            } else {
                return null;
            }
        } catch (AuthenticationUnavailableException var6) {
            throw var6;
        } catch (AuthenticationException var7) {
            return null;
        }
    }

    private static Multimap<String,Property> properties2map(Property[] properties){
            Multimap<String,Property> result = LinkedHashMultimap.create();
            for(Property p :properties){
                result.put(p.getName(), p);
            }
            return result;

    }


}
