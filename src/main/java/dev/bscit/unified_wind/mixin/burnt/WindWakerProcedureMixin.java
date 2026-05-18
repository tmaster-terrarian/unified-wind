package dev.bscit.unified_wind.mixin.burnt;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.pixelbank.burnt.network.BurntModVariables;
import net.pixelbank.burnt.procedures.WindWakerProcedure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {
    @Condition("burnt")
})
@Mixin(WindWakerProcedure.class)
public class WindWakerProcedureMixin
{
    @Inject(method = "execute", at = @At(value = "HEAD"), cancellable = true)
    private static void unifiedWind$redirectExecute(LevelAccessor world, CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.burnt || world.isClientSide() || !(world instanceof ServerLevel level))
            return;

        BurntModVariables.MapVariables mvSC = BurntModVariables.MapVariables.get(world);

        var wind = UnifiedWind.getWind(0, 63, 0, level);
        double newSpeed = wind.length();
        double newAngle = UnifiedWind.getAngle(wind.x, wind.z);
        boolean changedSC = false;

        if (Math.abs(newSpeed - mvSC.windSpeed) > 0.1) {
            mvSC.windSpeed = newSpeed;
            changedSC = true;
        }

        if (Math.abs(newAngle - mvSC.windAngle) > 0.1) {
            mvSC.windAngle = newAngle;
            changedSC = true;
        }

        if (changedSC) {
            mvSC.markSyncDirty();
        }

        ci.cancel();
    }
}
