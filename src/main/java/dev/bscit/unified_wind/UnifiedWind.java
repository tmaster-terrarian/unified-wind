package dev.bscit.unified_wind;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
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
import org.joml.SimplexNoise;
import org.joml.Vector3f;
import org.slf4j.Logger;

@Mod(UnifiedWind.MODID)
public class UnifiedWind
{
    public static final String MODID = "unified_wind";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public UnifiedWind(IEventBus modEventBus, ModContainer modContainer, Dist dist)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (UnifiedWind) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        LOGGER.info(logCompat(CommonConfig.compatBurntEnabled, "burnt", "Burnt"));
        LOGGER.info(logCompat(CommonConfig.compatParticleRainEnabled, "particlerain", "Particle Rain"));
        LOGGER.info(logCompat(CommonConfig.compatSootyChimneysEnabled, "sootychimneys", "Sooty Chimneys"));
    }

    private static String logCompat(boolean configCondition, String modId, String modName)
    {
        if(ModList.get().isLoaded(modId))
            return configCondition
                ? String.format("Enabling compat for %s (%s) [mod is present and config is enabled]", modName, modId)
                : String.format("Not enabling compat for %s (%s) [mod is present but config is disabled]", modName, modId);
        else
            return String.format("Not enabling compat for %s (%s) [mod is not present]", modName, modId);
    }

    // based on particle rain
    // particle rain's wind is very very cool
    public static Vector3f getWind(double x, double y, double z) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return new Vector3f();
        } else {
            float frequency = CommonConfig.windGustFrequency;
            float shift = (float)level.getGameTime() * CommonConfig.windModulationSpeed;
            float variance = CommonConfig.windStrengthVariance;
            float strength = CommonConfig.windStrength;
            float multiplier = CommonConfig.windYLevelAdjustment ? yLevelWindMultiplier(y) : 0.0F;
            float dir = (float)(Math.PI * 4 * SimplexNoise.noise(
                (float)x * CommonConfig.windDirectionVariance,
                0,
                (float)z * CommonConfig.windDirectionVariance,
                level.getGameTime() * 0.0001f)
            );
            return new Vector3f(
                Mth.cos(dir) * ((Mth.sin((float)(x * (double)frequency + (double)shift)) * variance + variance + strength) * multiplier + 0.001F),
                0.0F,
                Mth.sin(dir) * ((Mth.sin((float)(z * (double)frequency + (double)shift)) * variance + variance + strength) * multiplier + 0.001F)
            );
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
        }
    }
}
