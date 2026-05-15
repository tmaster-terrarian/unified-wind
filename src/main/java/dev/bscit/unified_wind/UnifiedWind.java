package dev.bscit.unified_wind;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.joml.Vector3f;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(UnifiedWind.MODID)
public class UnifiedWind
{
    public static final String MODID = "unified_wind";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public UnifiedWind(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (UnifiedWind) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if(Config.compatBurntEnabled && ModList.get().isLoaded("burnt"))
            LOGGER.info("Compat for Burnt is active");
        if(Config.compatParticleRainEnabled && ModList.get().isLoaded("particlerain"))
            LOGGER.info("Compat for Particle Rain is active");
    }

    // particle rain's wind is very very cool
    public static Vector3f getWind(double x, double y, double z) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return new Vector3f();
        } else {
            float frequency = Config.windGustFrequency;
            float shift = (float)level.getGameTime() * Config.windModulationSpeed;
            float variance = Config.windStrengthVariance;
            float strength = Config.windStrength;
            float multiplier = Config.windYLevelAdjustment ? yLevelWindMultiplier(y) : 0.0F;
            return new Vector3f((Mth.sin((float)(x * (double)frequency + (double)shift)) * variance + variance + strength) * multiplier + 0.001F, 0.0F, (Mth.sin((float)(z * (double)frequency + (double)shift)) * variance + variance + strength) * multiplier + 0.001F);
        }
    }

    public static float yLevelWindMultiplier(double y) {
        int transitionStart = 50;
        int transitionDistance = 40;
        return (float)Mth.clamp((y - (double)transitionStart) / (double)transitionDistance, (double)0.0F, (double)1.0F);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
