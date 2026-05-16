package dev.bscit.unified_wind.mixin.client;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import dev.bscit.unified_wind.mixin.accessor.ParticleAccessor;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.pixelbank.burnt.client.particle.*;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {
    @Condition("burnt")
})
@Mixin({
    AirParticleParticle.class,
    BurntLeavesParticleParticle.class,
    SmolderLeavesParticleParticle.class,
    BlueFlameParticleParticle.class,
    FireParticleParticle.class,
    FlameSmokeParticle.class,
    LargeFlameSmokeParticle.class,
    LargeSteamParticleParticle.class,
    LightFlameSmokeParticle.class,
    MediumSteamParticleParticle.class,
    MossFireParticleParticle.class,
    TorchFireParticleParticle.class,
    CampfireSmokeParticle.class
})
public class BurntParticleMixin
{
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void unifiedWind$injectTick(CallbackInfo ci)
    {
        if(!CommonConfig.compatBurntEnabled)
            return;
        var p = (ParticleAccessor)this;
        Vector3f wind = UnifiedWind.getWind(p.unifiedWind$getX(), p.unifiedWind$getY(), p.unifiedWind$getZ()).mul(0.05f);
        p.unifiedWind$setXd(wind.x);
        p.unifiedWind$setZd(wind.z);
    }
}
