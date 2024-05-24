package org.figrja.combo_auth.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.AuthSchemaList;
import org.figrja.combo_auth.config.configGson;
import org.figrja.combo_auth.ely.by.httpHelper;
import org.figrja.combo_auth.ely.by.resultElyGson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetAddress;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Mixin(YggdrasilMinecraftSessionService.class)
public class ReCheckAuth {

    configGson CONFIG = auth.getConfig();

    @Inject(at = @At("HEAD"),method = "hasJoinedServer")
    public void AuthListCheck(String profileName, String serverId, InetAddress address, CallbackInfoReturnable<ProfileResult> cir) throws AuthenticationUnavailableException {
        Map<String, Object> arguments = new HashMap();
        arguments.put("username", profileName);
        arguments.put("serverId", serverId);
        for(String name :CONFIG.getAuthList()) {
            AuthSchemaList authSchema = CONFIG.getAuthSchema().get(name);
            URL url = httpHelper.concatenateURL(httpHelper.constantURL(authSchema.getUrlCheck()), httpHelper.buildQuery(arguments));
            try {
                resultElyGson response = httpHelper.makeRequest(url);
                if (response != null && response.getId() != null) {
                    final GameProfile result = new GameProfile(response.getId(), response.getName());

                    if (response.getProperties() != null) {
                        PropertyMap properties = new PropertyMap();
                        if (authSchema.getUrlProperty()!=null) {
                            String PROPERTY_URL = authSchema.getUrlProperty();
                            URL p_url = httpHelper.concatenateURL(httpHelper.constantURL(MessageFormat.format(PROPERTY_URL, profileName, response.getId())), httpHelper.buildQuery(null));
                            resultElyGson pr = httpHelper.makeRequest(p_url);
                            if (pr != null) {
                                properties = pr.getProperties();
                            }
                        } else {
                            properties = response.getProperties();
                        }
                        if (authSchema.getProperty()!=null){
                            properties.putAll(authSchema.getProperty());
                        }
                        result.getProperties().putAll(properties);
                    }

                    cir.setReturnValue(new ProfileResult(result));
                }
            } catch (AuthenticationUnavailableException var6) {
                throw var6;
            } catch (AuthenticationException var7) {
                cir.setReturnValue(null);
            }
        }
    }

}
