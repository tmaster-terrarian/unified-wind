package dev.bscit.unified_wind.mixin.client;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import net.minecraft.client.particle.*;
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
        if(!CommonConfig.get().compat.vanilla)
            return;
        UnifiedWind.applyWindToParticleWithUnknownType(this, false);
    }
}
