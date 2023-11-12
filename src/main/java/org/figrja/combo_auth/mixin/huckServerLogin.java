package org.figrja.combo_auth.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import org.figrja.combo_auth.data.uuidBase;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.ConfigPro;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.UUID;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class huckServerLogin {


    @Shadow
    static Logger LOGGER;
    @Shadow
    MinecraftServer server;


    uuidBase base = auth.getConfigJson().getUuidBase();

    private static Integer stade;

    private ConfigPro CONFIG = auth.getConfigPro();
    private String strings;

    @Inject(at = @At("TAIL"),method = "onHello")
    public void packet2Name(LoginHelloC2SPacket packet, CallbackInfo ci){

        stade = 0;
    }

    @Inject(at = @At("TAIL"),method = "onKey")
    public void getString( LoginKeyC2SPacket packet, CallbackInfo ci) throws NetworkEncryptionException {
        PrivateKey privateKey = this.server.getKeyPair().getPrivate();
        SecretKey secretKey = packet.decryptSecretKey(privateKey);
        strings = (new BigInteger(NetworkEncryptionUtils.computeServerId("", this.server.getKeyPair().getPublic(), secretKey))).toString(16);
    }


    @Inject(at = @At("HEAD"),method = "disconnect",cancellable = true)
    public void init(Text reason, CallbackInfo ci) throws AuthenticationUnavailableException {
        if(reason.equals(Text.translatable("multiplayer.disconnect.authservers_down"))){
            if (stade == 0){
                reason = Text.translatable("multiplayer.disconnect.unverified_username");
            }
        }
        if (reason.equals(Text.translatable("multiplayer.disconnect.unverified_username"))){
            stade = 2;
            GameProfile ElyProfile = org.figrja.combo_auth.ely.by.auth_api.hasJoinedServer(new GameProfile(UUID.fromString("00000000-0000-0000-0000-000000000000"), profileName), strings);

            if (ElyProfile != null){

                stade = 1;
                LOGGER.info("UUID of player {} is {} with a Ely.by", ElyProfile.getName(), ElyProfile.getId());
                this.startVerify(ElyProfile);
                ci.cancel();
            }
        }
    }

    @Shadow
    public abstract void startVerify(GameProfile profile);

    @Shadow @Nullable private String profileName;

    @Shadow @Nullable private GameProfile profile;

    @Inject(at = @At("HEAD"),method = "startVerify")
    public void uuidDefend(GameProfile profile,CallbackInfo ci){

        if (server.isOnlineMode()){
            UUID[] uuid = base.baseUUID.get(profile.getId());

            if (uuid == null){
                base.baseUUID.put(profile.getId(),new UUID[2]);
                base.baseUUID.get(profile.getId())[stade] = profile.getId();
                auth.getConfigJson().setJsonSans(base);
                LOGGER.info("new user!");
            }
            else if (uuid[stade]==null){
                UUID randomUUID = UUID.randomUUID();
                while (base.baseUUID.containsKey(randomUUID)){

                    randomUUID = UUID.randomUUID();
                }

                base.baseUUID.get(profile.getId())[stade] = randomUUID;

                PropertyMap propertyMap = profile.getProperties();
                profile = new GameProfile(randomUUID, profile.getName());
                profile.getProperties().putAll(propertyMap);

                base.baseUUID.put(profile.getId(),new UUID[2]);
                base.baseUUID.get(profile.getId())[stade] = profile.getId();

                auth.getConfigJson().setJsonSans(base);
                LOGGER.info("update UUID : {}",profile.getId().toString());
            }
            else if (!uuid[stade].equals(profile.getId())){
                PropertyMap propertyMap = profile.getProperties();
                profile = new GameProfile(uuid[stade], profile.getName());
                profile.getProperties().putAll(propertyMap);
                LOGGER.info("update UUID : {}",profile.getId().toString());
            }

            if (CONFIG.isBoolean("emulateEly")&&profile.getProperties().get("ely")==null){
                profile.getProperties().put("ely",new Property("ely",CONFIG.isString("MessageEly")));
            }
        }

    }
}
