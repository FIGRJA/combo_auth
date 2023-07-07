package org.figrja.combo_auth.mixin;

import com.mojang.authlib.HttpAuthenticationService;
import org.figrja.combo_auth.ely.by.httpHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

@Mixin(HttpAuthenticationService.class)
public class huckHttpTest {

    @Shadow @Final private Proxy proxy;

    @Inject(at = @At("HEAD"),method = "createUrlConnection",remap = false)
    public void init(URL url, CallbackInfoReturnable<HttpURLConnection> cir){
        httpHelper.setProxy(proxy);
    }
}
