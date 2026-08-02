package dev.bscit.unified_wind.mixin.client.particular;

import com.leclowndu93150.particular.particles.CuboidParticle;
import com.leclowndu93150.particular.particles.CuboidParticleType;
import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {
    @Condition("particular")
})
@Mixin(CuboidParticle.class)
public abstract class CuboidParticleMixin extends TextureSheetParticle
{
    protected CuboidParticleMixin(ClientLevel level, double x, double y, double z)
    {
        super(level, x, y, z);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void unifiedWind$injectTick(CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.particular || this.onGround)
            return;
        UnifiedWind.applyWindToParticleWithUnknownType(this, false);
    }
}
