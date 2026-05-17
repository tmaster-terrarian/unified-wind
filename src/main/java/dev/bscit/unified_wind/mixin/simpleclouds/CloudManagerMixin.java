package dev.bscit.unified_wind.mixin.simpleclouds;

import dev.bscit.unified_wind.CommonConfig;
import dev.nonamecrackers2.simpleclouds.common.world.CloudManager;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Restriction(require = {
    @Condition("simpleclouds")
})
@Mixin(CloudManager.class)
public class CloudManagerMixin
{
    // fix rain being slow when above 128 y
    @ModifyConstant(method = "getPrecipitationAt", constant = @Constant(floatValue = 128.0f))
    private static float unifiedWind$modifyRainLevel(float original)
    {
        if(!CommonConfig.compatSimpleCloudsEnabled)
            return original;
        return 1000000;
    }
}
