package org.figrja.combo_auth.ely.by;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class auth_api {

    private static final URL CHECK_URL = httpHelper.constantURL("http://minecraft.ely.by/session/hasJoined");

    public static GameProfile hasJoinedServer(GameProfile user, String serverId) throws AuthenticationUnavailableException {
    Map<String, Object> arguments = new HashMap();
    arguments.put("username", user.getName());
    arguments.put("serverId", serverId);
    URL url = httpHelper.concatenateURL(CHECK_URL, httpHelper.buildQuery(arguments));


    try {
        UUID response = httpHelper.makeRequest(url);
        Logger.getLogger("Ely.by-User Authenticator").info(String.valueOf(response != null));
        return response != null ? new GameProfile(response, user.getName()) : null;
    } catch (AuthenticationUnavailableException var6) {
        throw var6;
    } catch (AuthenticationException var7) {
        return null;
    }
}


}
