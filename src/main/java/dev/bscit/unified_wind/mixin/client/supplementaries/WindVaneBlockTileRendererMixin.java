package dev.bscit.unified_wind.mixin.client.supplementaries;

import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import dev.bscit.unified_wind.mixin.accessor.WindVaneBlockTileAccessor;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.WindVaneBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.common.block.blocks.WindVaneBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.WindVaneBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Restriction(require = {
    @Condition("supplementaries")
})
@Mixin(WindVaneBlockTileRenderer.class)
public class WindVaneBlockTileRendererMixin
{
    @Redirect(
        method = "render(Lnet/mehvahdjukaar/supplementaries/common/block/tiles/WindVaneBlockTile;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at = @At(value = "INVOKE", target = "Lnet/mehvahdjukaar/supplementaries/common/block/tiles/WindVaneBlockTile;getYaw(F)F")
    )
    private float unifiedWind$GetYaw(WindVaneBlockTile instance, float partialTicks)
    {
        float orig = instance.getYaw(partialTicks);
        WindVaneBlockTileAccessor accessor = (WindVaneBlockTileAccessor)instance;

        var config = CommonConfig.get();
        if(!config.compat.supplementaries)
            return orig;

        Level level = instance.getLevel();
        if(level == null)
            level = Minecraft.getInstance().level;
        if(level == null)
            return orig;

        float windStrength = config.wind.base.strength;
        if (level.isThundering()) {
            windStrength = config.wind.storm.strength;
        } else if (level.isRaining()) {
            windStrength = config.wind.rain.strength;
        }

        double gameTime = (level.getGameTime() + (double)partialTicks) * 0.5;
        float shakiness = (float)(Math.sin(gameTime * windStrength) * Math.PI * 0.06 * windStrength/3.2);
        var pos = instance.getBlockPos();
        float yaw = (float)Math.toDegrees(shakiness + UnifiedWind.getWindAngle(pos.getX(), pos.getY(), pos.getZ(), instance.getLevel()));

        if(accessor.unifiedWind$getWindChargedTicks() > 0)
            yaw += (float)(Math.pow((accessor.unifiedWind$getWindChargedTicks() - partialTicks) / 40f, 3) * 360 * 6);

        return yaw;
    }
}
