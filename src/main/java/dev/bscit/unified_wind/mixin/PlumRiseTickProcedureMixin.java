package dev.bscit.unified_wind.mixin;

import dev.bscit.unified_wind.Config;
import dev.bscit.unified_wind.UnifiedWind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;
import net.pixelbank.burnt.configuration.BurntBasicConfigConfiguration;
import net.pixelbank.burnt.network.BurntModVariables;
import net.pixelbank.burnt.procedures.PlumRiseTickProcedure;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Restriction(require = {
    @Condition("burnt")
})
@Mixin(PlumRiseTickProcedure.class)
public class PlumRiseTickProcedureMixin
{
    @Inject(method = "execute", cancellable = true, at = @At(value = "HEAD"))
    private static void unifiedWind$execute(LevelAccessor world, CallbackInfo ci)
    {
        if(!Config.compatBurntEnabled || !BurntBasicConfigConfiguration.DISTANT_SMOKE.get() || !world.isClientSide() || !(world instanceof ServerLevel level))
            return;

        long gameTime = level.getLevelData().getGameTime();

        for(Entity e : level.getAllEntities())
        {
            if(e != null && e.getType() == EntityType.ITEM_DISPLAY && !e.isRemoved())
            {
                Vector3f wind = UnifiedWind.getWind(e.getX(), e.getY(), e.getZ());
                double dx = wind.x;
                double dz = wind.z;

                Set<String> tags = e.getTags();
                if(tags.contains("burnt_any_plume") && ((!tags.contains("far_burnt_plume") || gameTime % 2L == 0L) && (!tags.contains("distant_burnt_plume") || gameTime % 4L == 0L)))
                {
                    e.setPos(e.getX() + dx, e.getY() + 0.03, e.getZ() + dz);
                }
            }
        }

        ci.cancel();
    }
}
