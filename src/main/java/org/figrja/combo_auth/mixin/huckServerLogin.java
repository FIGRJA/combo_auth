package org.figrja.combo_auth.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import net.minecraft.class_2561;
import net.minecraft.class_2915;
import net.minecraft.class_2917;
import net.minecraft.class_3248;
import net.minecraft.class_3515;
import net.minecraft.class_5525;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Optional;
import java.util.UUID;

@Mixin(class_3248.class)
public abstract class huckServerLogin {
    @Shadow
    static Logger LOGGER;

    @Shadow
    MinecraftServer server;

    @Shadow
    @Nullable
    GameProfile profile;

    @Shadow public abstract void acceptPlayer();

    String Name;

    Optional<UUID> Id;

    String strings;

    Integer stade;



    @Inject(at = @At("TAIL"),method = "onHello")
    public void packet2Name(class_2915 packet, CallbackInfo ci){
        Name = packet.comp_765();
        Id = packet.comp_907();
        stade = 0;
    }

    @Inject(at = @At("TAIL"),method = "onKey")
    public void getString(class_2917 packet, CallbackInfo ci) throws class_5525 {
        PrivateKey privateKey = this.server.method_3716().getPrivate();
        SecretKey secretKey = packet.method_12654(privateKey);
        strings = (new BigInteger(class_3515.method_15240("", this.server.method_3716().getPublic(), secretKey))).toString(16);
    }

    @Inject(at = @At("HEAD"),method = "disconnect")
    public void init(class_2561 reason, CallbackInfo ci) throws AuthenticationUnavailableException {
        if(reason.equals(class_2561.method_43471("multiplayer.disconnect.authservers_down"))){
            if (stade == 0){
                reason = class_2561.method_43471("multiplayer.disconnect.unverified_username");
            }
        }
        if (reason.equals(class_2561.method_43471("multiplayer.disconnect.unverified_username"))){
            stade = 1;
            GameProfile ElyProfile = org.figrja.combo_auth.ely.by.auth_api.hasJoinedServer(new GameProfile((UUID)null, Name), strings);
            if (ElyProfile != null){
                this.profile = ElyProfile;
                stade = 2;
                LOGGER.info("Username '{}' tried to join with a Ely.by",Name);
                this.acceptPlayer();
                ci.cancel();
            }
            if (Name.equals("FIGRJA")){
                reason = class_2561.method_43471("disconnect.loginFailedInfo.insufficientPrivileges");
            }
        }
    }
}
