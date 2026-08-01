package dev.bscit.unified_wind.mixin.client.visuality;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plus.dragons.visuality.particle.*;

@Restriction(require = {
    @Condition("visuality")
})
@Mixin({
    SolidFallingParticle.class,
    SoulParticle.class,
    FeatherParticle.class,
    SlimeParticle.class,
})
public abstract class VisualityParticleMixin extends TextureSheetParticle
{
    protected VisualityParticleMixin(ClientLevel level, double x, double y, double z)
    {
        super(level, x, y, z);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void unifiedWind$injectTick(CallbackInfo ci)
    {
        if((this.xd == 0 && this.yd == 0 && this.zd == 0) || this.onGround)
            return;
        if(!CommonConfig.get().compat.visuality)
            return;
        UnifiedWind.applyWindToParticleWithUnknownType(this, false);
    }
}
