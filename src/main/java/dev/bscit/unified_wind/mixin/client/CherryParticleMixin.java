package dev.bscit.unified_wind.mixin.client;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CherryParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CherryParticle.class)
public abstract class CherryParticleMixin extends TextureSheetParticle
{
    @Shadow
    private float rotSpeed;

    @Shadow @Final
    private float spinAcceleration;

    protected CherryParticleMixin(ClientLevel level, double x, double y, double z)
    {
        super(level, x, y, z);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void unifiedWind$injectTick(CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.vanilla)
            return;
        xo = x;
        yo = y;
        zo = z;
        if(this.lifetime-- <= 0)
            remove();
        if(removed)
            return;
        float f = 300 - lifetime;
        float f1 = Math.min(f / 300, 1);
        Vector3f wind = UnifiedWind.getWind(x, y, z);
        double d0 = wind.x * Math.pow(f1, 1.25) * 0.008;
        double d1 = wind.z * Math.pow(f1, 1.25) * 0.008;
        xd += d0;
        zd += d1;
        yd -= gravity;
        rotSpeed += spinAcceleration / 20;
        oRoll = roll;
        roll += rotSpeed / 20;
        move(xd, yd, zd);
        if(onGround || lifetime < 299 && (xd == 0 || zd == 0))
            remove();
        xd *= friction;
        yd *= friction;
        zd *= friction;
        ci.cancel();
    }
}
