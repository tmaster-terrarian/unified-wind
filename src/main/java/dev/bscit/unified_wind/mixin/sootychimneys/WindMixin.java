package dev.bscit.unified_wind.mixin.sootychimneys;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import io.github.mortuusars.sootychimneys.data.wind.Wind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {
    @Condition("sootychimneys")
})
@Mixin(Wind.class)
public class WindMixin
{
    @Inject(method = "update", cancellable = true, at = @At(value = "HEAD"))
    private static void unifiedWind$update(Level level, CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.sootyChimneys)
            return;
        var wind = UnifiedWind.getWind(0, 63, 0, level);
        Wind.getWind().set(Math.toDegrees(UnifiedWind.getAngle(wind.x, wind.z)), wind.length());
        ci.cancel();
    }
}
