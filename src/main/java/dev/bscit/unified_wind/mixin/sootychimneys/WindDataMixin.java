package dev.bscit.unified_wind.mixin.sootychimneys;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.bscit.unified_wind.CommonConfig;
import io.github.mortuusars.sootychimneys.data.wind.WindData;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = {
    @Condition("sootychimneys")
})
@Mixin(WindData.class)
public class WindDataMixin
{
    @Inject(method = "update", at = @At(value = "HEAD"), cancellable = true)
    private static void unifiedWind$nopeUpdate(CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.sootyChimneys)
            return;
        ci.cancel();
    }

    @Inject(method = "getAdjustedStrength", at = @At(value = "RETURN"), cancellable = true)
    private static void unifiedWind$modifyAdjustedStrength(CallbackInfoReturnable<Float> cir)
    {
        if(!CommonConfig.get().compat.sootyChimneys)
            return;
        cir.setReturnValue(1f);
    }
}
