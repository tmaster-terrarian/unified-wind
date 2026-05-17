package dev.bscit.unified_wind;

import dev.nonamecrackers2.simpleclouds.common.world.CloudManager;
import dev.nonamecrackers2.simpleclouds.common.world.CloudManagerHolder;
import net.minecraft.world.level.Level;

public class SimpleCloudsBridge
{
    public static float getRainLevel(double x, double y, double z, Level level)
    {
        CloudManager<?> manager = CloudManager.get(level);
        return manager.getRainLevel((float)x, (float)y, (float)z);
    }
}
