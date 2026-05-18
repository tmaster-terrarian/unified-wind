package dev.bscit.unified_wind.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.bscit.unified_wind.UnifiedWind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin
{
    @ModifyReturnValue(method = "getGameInformation", at = @At("RETURN"))
    private List<String> unifiedWind$debugInfo(List<String> original)
    {
        original.add("");
        var player = Minecraft.getInstance().player;
        assert player != null;
        var wind = UnifiedWind.getWind(player.getX(), player.getY(), player.getZ());
        original.add(String.format("UW wind xz: %s, %s", Math.floor(wind.x * 100) / 100, Math.floor(wind.z * 100) / 100));
        original.add(String.format("UW wind dir: %s", Math.floor(Math.toDegrees(UnifiedWind.getAngle(wind.x, wind.z)) * 100) / 100));
        return original;
    }
}
