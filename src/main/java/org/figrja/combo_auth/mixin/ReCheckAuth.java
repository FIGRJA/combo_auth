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
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
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

    LoggerMain LOGGER = auth.Logger;

    configGson CONFIG = auth.getConfig();

    @Inject(at = @At("HEAD"),method = "hasJoinedServer",remap = false,cancellable = true)
    public void AuthListCheck(String profileName, String serverId, InetAddress address, CallbackInfoReturnable<ProfileResult> cir) throws AuthenticationUnavailableException {
        Map<String, Object> arguments = new HashMap();
        arguments.put("username", profileName);
        arguments.put("serverId", serverId);
        boolean tr = false;
        AuthenticationUnavailableException var6 = null;
        boolean AuthenticationException = false;
        for(String name :CONFIG.getAuthList()) {
            LOGGER.debug("try "+name);
            AuthSchemaList authSchema = CONFIG.getAuthSchema().get(name);
            LOGGER.debugRes("in "+authSchema.getUrlCheck());
            URL url = httpHelper.concatenateURL(httpHelper.constantURL(authSchema.getUrlCheck()), httpHelper.buildQuery(arguments));
            try {
                resultElyGson response = httpHelper.makeRequest(url);
                if (response != null && response.getId() != null) {
                    LOGGER.debug("response not null");
                    final GameProfile result = new GameProfile(response.getId(), response.getName());

                    if (response.getProperties() != null) {
                        PropertyMap properties = new PropertyMap();
                        LOGGER.debug("properties not null");
                        if (authSchema.getUrlProperty()!=null) {
                            LOGGER.debug("custom property");
                            LOGGER.debugRes("in "+authSchema.getUrlProperty() );
                            String PROPERTY_URL = authSchema.getUrlProperty();
                            URL p_url = httpHelper.concatenateURL(httpHelper.constantURL(MessageFormat.format(PROPERTY_URL, profileName, response.getId())), httpHelper.buildQuery(null));
                            resultElyGson pr = httpHelper.makeRequest(p_url);
                            if (pr != null) {
                                properties = pr.getProperties();
                            }else {
                                LOGGER.debug("custom property is null");
                            }
                        } else {
                            properties = response.getProperties();
                        }
                        result.getProperties().putAll(properties);
                    }
                    if (authSchema.getAddProperty()!=null){
                        LOGGER.debug("add custom property");
                        result.getProperties().putAll(authSchema.getProperty());
                    }
                    LOGGER.info("logging from "+name);
                    cir.setReturnValue(new ProfileResult(result));
                    cir.cancel();
                    tr = false;
                    AuthenticationException = false;
                    break;
                }
            } catch (AuthenticationUnavailableException var7) {
                tr = true;
                var6 = var7;
            } catch (AuthenticationException var7) {
                AuthenticationException = true;
            }
        }
        if (tr){
            throw var6;
        }
        if (AuthenticationException){
            cir.setReturnValue(null);
        }
        cir.cancel();
    }

}
