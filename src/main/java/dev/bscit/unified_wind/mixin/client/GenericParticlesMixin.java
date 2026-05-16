package dev.bscit.unified_wind.mixin.client;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import dev.bscit.unified_wind.mixin.accessor.ParticleAccessor;
import net.minecraft.client.particle.*;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
    CampfireSmokeParticle.class,
    PlayerCloudParticle.class,
    FallingDustParticle.class,
    LavaParticle.class
})
public class GenericParticlesMixin
{
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void unifiedWind$injectTick(CallbackInfo ci)
    {
        if(!CommonConfig.compatVanillaEnabled)
            return;
        var p = (ParticleAccessor)this;
        Vector3f wind = UnifiedWind.getWind(p.unifiedWind$getX(), p.unifiedWind$getY(), p.unifiedWind$getZ()).mul(0.05f);
        p.unifiedWind$setXd(p.unifiedWind$getXd() + 0.2 * (wind.x - p.unifiedWind$getXd()));
        p.unifiedWind$setZd(p.unifiedWind$getZd() + 0.2 * (wind.z - p.unifiedWind$getZd()));
    }
}
