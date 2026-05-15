package dev.bscit.unified_wind.mixin;

import dev.bscit.unified_wind.Config;
import dev.bscit.unified_wind.UnifiedWind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.Minecraft;
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
        if(!Config.compatBurntEnabled)
            return;

        BurntModVariables.MapVariables mvSC = BurntModVariables.MapVariables.get(world);
        var player = Minecraft.getInstance().gameRenderer.getMainCamera();

        var wind = UnifiedWind.getWind(player.getPosition().x, player.getPosition().y, player.getPosition().z);
        double newSpeed = wind.length();
        double newAngle = Math.atan2(wind.z, wind.x);
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
