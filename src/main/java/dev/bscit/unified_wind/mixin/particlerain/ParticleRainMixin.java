package dev.bscit.unified_wind.mixin.particlerain;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pigcart.particlerain.ParticleRain;

@Restriction(require = {
    @Condition("particlerain")
})
@Mixin(ParticleRain.class)
public class ParticleRainMixin
{
    @Inject(method = "getWind", cancellable = true, at = @At(value = "HEAD"))
    private static void unifiedWind$replaceGetWind(double x, double y, double z, CallbackInfoReturnable<Vector3f> cir)
    {
        if(!CommonConfig.compatParticleRainEnabled)
            return;
        cir.setReturnValue(UnifiedWind.getWind(x, y, z));
    }

    @Inject(method = "yLevelWindMultiplier", cancellable = true, at = @At(value = "HEAD"))
    private static void unifiedWind$replaceYLevelWindMultiplier(double y, CallbackInfoReturnable<Float> cir)
    {
        if(!CommonConfig.compatParticleRainEnabled)
            return;
        cir.setReturnValue(UnifiedWind.yLevelWindMultiplier(y));
    }
}
