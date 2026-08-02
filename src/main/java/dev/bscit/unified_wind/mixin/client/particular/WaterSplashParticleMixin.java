package dev.bscit.unified_wind.mixin.client.particular;

import com.leclowndu93150.particular.particles.splashes.WaterSplashParticle;
import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {
    @Condition("particular")
})
@Mixin({
    WaterSplashParticle.class,
})
public class WaterSplashParticleMixin
{
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void unifiedWind$injectTick(CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.particular)
            return;
        UnifiedWind.applyWindToParticleWithUnknownType(this, false);
    }
}
