package dev.bscit.unified_wind.mixin.client.grassiergrass;

import com.leonardoinc22.shortgrass.config.GrassConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.bscit.unified_wind.CommonConfig;
import dev.bscit.unified_wind.UnifiedWind;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {
    @Condition("grassiergrass")
})
@Mixin(GrassConfig.class)
public abstract class GrassConfigMixin
{
    @Shadow
    private static float effectiveWindSpeed;

    @ModifyReturnValue(method = "smoothWindDirectionOffset", at = @At("RETURN"))
    private static float unifiedWind$windDirection(float orig)
    {
        if(!CommonConfig.get().compat.grassierGrass)
            return orig;
        var client = Minecraft.getInstance();
        Camera camera = client.gameRenderer.getMainCamera();
        Vec3 pos = camera.getPosition();
        return UnifiedWind.getWindAngle(pos.x, pos.y, pos.z, client.level);
    }

    @Inject(method = "updateDynamicWind", at = @At("HEAD"), cancellable = true)
    private static void unifiedWind$updateWind(float windTime, CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.grassierGrass)
            return;
        var client = Minecraft.getInstance();
        Camera camera = client.gameRenderer.getMainCamera();
        Vec3 pos = camera.getPosition();
        Vector3f wind = UnifiedWind.getWind(pos.x, pos.y, pos.z, client.level);

        float angle = (float)Math.toDegrees(UnifiedWind.getAngle(wind.x, wind.z) + Math.PI * 0.5);
        angle = angle < 0.0F ? angle + 360.0F : angle;

        GrassConfig.windDirectionDegrees = angle;
        effectiveWindSpeed = wind.length() * 5 * 15 * GrassConfig.dynamicWindSpeedLimit();
        GrassConfig.windSpeed = Math.round(effectiveWindSpeed);

        ci.cancel();
    }

    @Inject(method = "setWindDirectionDegrees", at = @At("HEAD"), cancellable = true)
    private static void unifiedWind$makeDirectionReadOnly(float degrees, CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.grassierGrass)
            return;
        ci.cancel();
    }

    @Inject(method = "setWindSpeed", at = @At("HEAD"), cancellable = true)
    private static void unifiedWind$makeSpeedReadOnly(int speed, CallbackInfo ci)
    {
        if(!CommonConfig.get().compat.grassierGrass)
            return;
        ci.cancel();
    }
}
