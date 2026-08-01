package dev.bscit.unified_wind;

import com.mojang.logging.LogUtils;
import dev.bscit.unified_wind.mixin.client.accessor.ParticleAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
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

    private static final boolean SIMPLECLOUDS_ENABLED = ModList.get().isLoaded("simpleclouds");

    private static float clientStrength;
    private static float clientVariance;

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

        // Register our mod's config
        AutoConfig.register(
            CommonConfig.class,
            PartitioningSerializer.wrap(Toml4jConfigSerializer::new)
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        var config = CommonConfig.get();
        LOGGER.info(logCompat(config.compat.vanilla, "minecraft", "Vanilla"));
        LOGGER.info(logCompat(config.compat.burnt, "burnt", "Burnt"));
        LOGGER.info(logCompat(config.compat.particleRain, "particlerain", "Particle Rain"));
        LOGGER.info(logCompat(config.compat.sootyChimneys, "sootychimneys", "Sooty Chimneys"));
        LOGGER.info(logCompat(config.compat.simpleClouds, "simpleclouds", "Simple Clouds"));
        LOGGER.info(logCompat(config.compat.fallingLeaves, "fallingleaves", "Falling Leaves"));
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

    public static boolean isWindy(double x, double y, double z, Level level)
    {
        if(level == null)
            return false;

        var dimension = level.dimension().location().toString();
        if(!CommonConfig.get().wind.windyDimensions.contains(dimension))
            return false;

        var pos = BlockPos.containing(x, y, z);
        if(level.dimensionType().hasSkyLight() && level.getBrightness(LightLayer.SKY, pos) == 0)
            return false;

        var fluid = level.getFluidState(pos);
        return fluid.isEmpty() || CommonConfig.get().wind.allowUnderwater;
    }

    // based on particle rain
    // particle rain's wind is very very cool
    public static Vector3f getWind(double x, double y, double z, Level level)
    {
        level.getProfiler().push("uw:getWind");
        if (!isWindy(x, y, z, level))
        {
            level.getProfiler().pop();
            return new Vector3f();
        }

        var pos = BlockPos.containing(x, y, z);
        var hPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
        // annoying and laggy fix :/
        if(level.getBlockState(hPos).getBlock() instanceof PowderSnowBlock)
            hPos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos);
        hPos = new BlockPos(hPos.getX(), Math.max(pos.getY(), hPos.getY()), hPos.getZ());

        float skyExposureMultiplier = level.dimensionType().hasSkyLight()
            ? level.getBrightness(LightLayer.SKY, pos) / 15f
            : 1;

        CommonConfig config = CommonConfig.get();
        float frequency = config.wind.base.gustFrequency;
        float shift = (float)level.getGameTime() * config.wind.base.modulationSpeed;
        float variance = config.wind.base.strengthVariance;
        float strength = config.wind.base.strength;
        float directionVariance = config.wind.base.directionVariance;
        if(level.isRainingAt(hPos))
        {
            float mix = level.getRainLevel(1);
            if(level.isThundering() || SIMPLECLOUDS_ENABLED)
            {
                mix = level.getThunderLevel(1);
                if(SIMPLECLOUDS_ENABLED)
                    mix = SimpleCloudsBridge.getRainLevel(x, y, z, level);
                frequency = config.wind.storm.gustFrequency;
                shift = (float)level.getGameTime() * config.wind.storm.modulationSpeed;
                variance = Mth.lerp(config.wind.rain.strengthVariance, config.wind.storm.strengthVariance, Math.clamp(mix, 0, 1));
                strength = Mth.lerp(config.wind.rain.strength, config.wind.storm.strength, Math.clamp(mix, 0, 1));
                directionVariance = config.wind.storm.directionVariance;
            }
            else
            {
                frequency = config.wind.rain.gustFrequency;
                shift = (float)level.getGameTime() * config.wind.rain.modulationSpeed;
                variance = Mth.lerp(config.wind.base.strengthVariance, config.wind.rain.strengthVariance, Math.clamp(mix, 0, 1));
                strength = Mth.lerp(config.wind.base.strength, config.wind.rain.strength, Math.clamp(mix, 0, 1));
                directionVariance = config.wind.rain.directionVariance;
            }
        }
        if(level.isClientSide())
        {
            // interpolate between newly calculated value and global client value by distance to prevent weird issues
            var camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            var targetPos = new Vec3(x, y, z);
            var interp = (float)(Math.min(targetPos.subtract(camPos).length(), 32) / 32);
            variance = Mth.lerp(clientVariance, variance, interp);
            strength = Mth.lerp(clientStrength, strength, interp);
        }
        float multiplier = config.wind.yLevelAdjustment ? yLevelWindMultiplier(y) : 0.0F;
        float dir = (float)(Math.PI * 4 * SimplexNoise.noise(
            (float)x * directionVariance,
            0,
            (float)z * directionVariance,
            level.getGameTime() * 0.0001f)
        );

        Vector3f vector = new Vector3f(
            Mth.cos(dir) * ((Mth.sin((float)(x * (double)frequency + (double)shift)) * variance + variance + strength) * multiplier + 0.001F),
            0.0F,
            Mth.sin(dir) * ((Mth.sin((float)(z * (double)frequency + (double)shift)) * variance + variance + strength) * multiplier + 0.001F)
        ).mul(skyExposureMultiplier);

        level.getProfiler().pop();
        return vector;
    }

    public static boolean isWindy(double x, double y, double z)
    {
        return isWindy(x, y, z, Minecraft.getInstance().level);
    }

    public static Vector3f getWind(double x, double y, double z)
    {
        return getWind(x, y, z, Minecraft.getInstance().level);
    }

    public static double getAngle(double x, double z)
    {
        return -Math.atan2(x, z);
    }

    public static float getAngle(float x, float z)
    {
        return (float)getAngle((double)x, (double)z);
    }

    public static float getWindAngle(double x, double y, double z, Level level)
    {
        var dir = getWind(x, y, z, level);
        return getAngle(dir.x, dir.z);
    }

    public static double getWindAngle(double x, double y, double z)
    {
        return getWindAngle(x, y, z, Minecraft.getInstance().level);
    }

    public static float yLevelWindMultiplier(double y) {
        int transitionStart = 50;
        int transitionDistance = 40;
        return (float)Mth.clamp((y - (double)transitionStart) / (double)transitionDistance, (double)0.0F, (double)1.0F);
    }

    public static void applyWindToParticle(Particle particle, boolean forced)
    {
        applyWindToParticleWithUnknownType(particle, forced);
    }

    public static void applyWindToParticleWithUnknownType(Object particle, boolean forced)
    {
        var p = (ParticleAccessor)particle;
        if(!isWindy(p.unifiedWind$getX(), p.unifiedWind$getY(), p.unifiedWind$getZ()))
            return;
        Vector3f wind = UnifiedWind.getWind(p.unifiedWind$getX(), p.unifiedWind$getY(), p.unifiedWind$getZ()).mul(0.05f);
        if(forced)
        {
            p.unifiedWind$setXd(wind.x);
            p.unifiedWind$setZd(wind.z);
        }
        else
        {
            p.unifiedWind$setXd(p.unifiedWind$getXd() + 0.2 * (wind.x - p.unifiedWind$getXd()));
            p.unifiedWind$setZd(p.unifiedWind$getZd() + 0.2 * (wind.z - p.unifiedWind$getZd()));
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private static float expDecay(float a, float b, float decay, float dt)
    {
        return (float)(b+(a-b)*Math.pow(Math.E, -decay*dt));
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        private static float clientVarianceInterp;
        private static float clientStrengthInterp;
        private static float clientVarianceOld;
        private static float clientStrengthOld;

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
        }

        @SubscribeEvent
        public static void onTick(ClientTickEvent.Pre event)
        {
            ClientLevel level = Minecraft.getInstance().level;
            if(level == null)
                return;

            var posD = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            var pos = BlockPos.containing(posD);
            var hPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
            // annoying and laggy fix :/
            if(level.getBlockState(hPos).getBlock() instanceof PowderSnowBlock)
                hPos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos);
            hPos = new BlockPos(hPos.getX(), Math.max(pos.getY(), hPos.getY()), hPos.getZ());

            CommonConfig config = CommonConfig.get();
            float variance = config.wind.base.strengthVariance;
            float strength = config.wind.base.strength;
            if(level.isRainingAt(hPos))
            {
                float mix = level.getRainLevel(1);
                if(level.isThundering() || SIMPLECLOUDS_ENABLED)
                {
                    mix = level.getThunderLevel(1);
                    if(SIMPLECLOUDS_ENABLED)
                        mix = SimpleCloudsBridge.getRainLevel(posD.x, posD.y, posD.z, level);
                    variance = Mth.lerp(config.wind.rain.strengthVariance, config.wind.storm.strengthVariance, Math.clamp(mix, 0, 1));
                    strength = Mth.lerp(config.wind.rain.strength, config.wind.storm.strength, Math.clamp(mix, 0, 1));
                }
                else
                {
                    variance = Mth.lerp(config.wind.base.strengthVariance, config.wind.rain.strengthVariance, Math.clamp(mix, 0, 1));
                    strength = Mth.lerp(config.wind.base.strength, config.wind.rain.strength, Math.clamp(mix, 0, 1));
                }
            }

            clientVarianceOld = clientVarianceInterp;
            clientStrengthOld = clientStrengthInterp;
            clientVarianceInterp = expDecay(clientVarianceInterp, variance, 0.4f, 1/20f);
            clientStrengthInterp = expDecay(clientStrengthInterp, strength, 0.4f, 1/20f);
        }

        @SubscribeEvent
        public static void onRender(RenderFrameEvent.Pre event)
        {
            clientVariance = Mth.lerp(clientVarianceOld, clientVarianceInterp, event.getPartialTick().getRealtimeDeltaTicks());
            clientStrength = Mth.lerp(clientStrengthOld, clientStrengthInterp, event.getPartialTick().getRealtimeDeltaTicks());
        }
    }
}
