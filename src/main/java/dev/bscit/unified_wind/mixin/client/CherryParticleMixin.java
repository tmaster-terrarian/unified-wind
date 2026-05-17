package dev.bscit.unified_wind.mixin.client;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import dev.bscit.unified_wind.mixin.client.accessor.ParticleAccessor;
import net.minecraft.client.particle.CherryParticle;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CherryParticle.class)
public class CherryParticleMixin
{
    @Shadow
    private float rotSpeed;

    @Shadow @Final
    private float spinAcceleration;

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void unifiedWind$injectTick(CallbackInfo ci)
    {
        if(!CommonConfig.compatVanillaEnabled)
            return;
        var p = (CherryParticle)(Object)this;
        var accessor = (ParticleAccessor)this;

        accessor.unifiedWind$setXo(accessor.unifiedWind$getX());
        accessor.unifiedWind$setYo(accessor.unifiedWind$getY());
        accessor.unifiedWind$setZo(accessor.unifiedWind$getZ());

        p.setLifetime(p.getLifetime() - 1);
        if(p.getLifetime() <= 0)
            p.remove();

        if(!p.isAlive())
            return;

        float f = 300 - p.getLifetime();
        float f1 = Math.min(f / 300, 1);
        Vector3f wind = UnifiedWind.getWind(p.getPos().x, p.getPos().y, p.getPos().z);
        double d0 = wind.x * Math.pow(f1, 1.25) * 0.008;
        double d1 = wind.z * Math.pow(f1, 1.25) * 0.008;
        accessor.unifiedWind$setXd(accessor.unifiedWind$getXd() + d0);
        accessor.unifiedWind$setZd(accessor.unifiedWind$getZd() + d1);
        accessor.unifiedWind$setYd(accessor.unifiedWind$getYd() - accessor.unifiedWind$getGravity());
        this.rotSpeed += this.spinAcceleration / 20;
        accessor.unifiedWind$setORoll(accessor.unifiedWind$getRoll());
        accessor.unifiedWind$setRoll(accessor.unifiedWind$getRoll() + this.rotSpeed / 20);
        p.move(accessor.unifiedWind$getXd(), accessor.unifiedWind$getYd(), accessor.unifiedWind$getZd());
        if(accessor.unifiedWind$getOnGround() || p.getLifetime() < 299 && (accessor.unifiedWind$getXd() == 0 || accessor.unifiedWind$getZd() == 0))
            p.remove();

        accessor.unifiedWind$setXd(accessor.unifiedWind$getXd() * accessor.unifiedWind$getFriction());
        accessor.unifiedWind$setYd(accessor.unifiedWind$getYd() * accessor.unifiedWind$getFriction());
        accessor.unifiedWind$setZd(accessor.unifiedWind$getZd() * accessor.unifiedWind$getFriction());

        ci.cancel();
    }
}
