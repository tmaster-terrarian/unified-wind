package dev.bscit.unified_wind;

import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = UnifiedWind.MODID, dist = Dist.CLIENT)
public class UnifiedWindClient
{
    public UnifiedWindClient(IEventBus modEventBus, ModContainer container)
    {
        container.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) ->
            AutoConfig.getConfigScreen(CommonConfig.class, parent).get()
        );
    }
}
