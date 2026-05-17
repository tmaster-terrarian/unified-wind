package dev.bscit.unified_wind.mixin.client.fallingleaves;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.cheaterpaul.fallingleaves.util.FallingLeafParticle;
import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import dev.bscit.unified_wind.mixin.client.accessor.ParticleAccessor;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = {
    @Condition("fallingleaves")
})
@Mixin(FallingLeafParticle.class)
public class FallingLeafParticleMixin
{
    @ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lde/cheaterpaul/fallingleaves/util/Wind;windX:F"))
    private float unifiedWind$modifyWindX(float original)
    {
        if(!CommonConfig.compatFallingLeavesEnabled)
            return original;
        var p = (ParticleAccessor)this;
        return UnifiedWind.getWind(p.unifiedWind$getX(), p.unifiedWind$getY(), p.unifiedWind$getZ()).x * 0.8f;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lde/cheaterpaul/fallingleaves/util/Wind;windZ:F"))
    private float unifiedWind$modifyWindZ(float original)
    {
        if(!CommonConfig.compatFallingLeavesEnabled)
            return original;
        var p = (ParticleAccessor)this;
        return UnifiedWind.getWind(p.unifiedWind$getX(), p.unifiedWind$getY(), p.unifiedWind$getZ()).z * 0.8f;
    }
}
