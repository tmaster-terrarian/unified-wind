package dev.bscit.unified_wind.mixin.client;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import net.minecraft.client.particle.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public class ParticleMixin
{
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void unifiedWind$injectTick(CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.vanilla)
            return;
        var obj = (Object)this;
        //noinspection ConstantValue
        if((obj instanceof FlameParticle)
            || (obj instanceof BaseAshSmokeParticle)
            || (obj instanceof AshParticle)
            || (obj instanceof SuspendedParticle)
            || (obj instanceof PlayerCloudParticle)
            || (obj instanceof TerrainParticle)
            || (obj instanceof DustParticle)
            || (obj instanceof SpellParticle)
            || (obj instanceof TotemParticle)
            || (obj instanceof SplashParticle))
            UnifiedWind.applyWindToParticleWithUnknownType(this, false);
    }
}
