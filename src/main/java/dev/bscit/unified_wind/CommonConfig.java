package dev.bscit.unified_wind;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = UnifiedWind.MODID)
public class CommonConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // wind
    private static final ModConfigSpec.DoubleValue WIND_STRENGTH = BUILDER
        .translation(String.format("%s.configuration.wind.strength", UnifiedWind.MODID))
        .defineInRange("wind.base.strength", 0.2, 0, Double.MAX_VALUE);
    public static float windStrength = 0.2F;

    private static final ModConfigSpec.DoubleValue WIND_STRENGTH_VARIANCE = BUILDER
        .translation(String.format("%s.configuration.wind.strength_variance", UnifiedWind.MODID))
        .defineInRange("wind.base.strengthVariance", 0.15, 0, Double.MAX_VALUE);
    public static float windStrengthVariance = 0.15F;

    private static final ModConfigSpec.DoubleValue WIND_GUST_FREQUENCY = BUILDER
        .translation(String.format("%s.configuration.wind.gust_frequency", UnifiedWind.MODID))
        .defineInRange("wind.base.gustFrequency", 0.1, 0, Double.MAX_VALUE);
    public static float windGustFrequency = 0.1F;

    private static final ModConfigSpec.DoubleValue WIND_MODULATION_SPEED = BUILDER
        .translation(String.format("%s.configuration.wind.modulation_speed", UnifiedWind.MODID))
        .defineInRange("wind.base.modulationSpeed", 0.04, 0, Double.MAX_VALUE);
    public static float windModulationSpeed = 0.04F;

    private static final ModConfigSpec.DoubleValue WIND_DIRECTION_VARIANCE = BUILDER
        .translation(String.format("%s.configuration.wind.direction_variance", UnifiedWind.MODID))
        .defineInRange("wind.base.directionVariance", 0.002, 0, Double.MAX_VALUE);
    public static float windDirectionVariance = 0.002F;

    private static final ModConfigSpec.DoubleValue WIND_RAIN_STRENGTH = BUILDER
        .translation(String.format("%s.configuration.wind.strength", UnifiedWind.MODID))
        .defineInRange("wind.rain.strength", 0.4, 0, Double.MAX_VALUE);
    public static float windRainStrength = 0.4F;

    private static final ModConfigSpec.DoubleValue WIND_RAIN_STRENGTH_VARIANCE = BUILDER
        .translation(String.format("%s.configuration.wind.strength_variance", UnifiedWind.MODID))
        .defineInRange("wind.rain.strengthVariance", 0.3, 0, Double.MAX_VALUE);
    public static float windRainStrengthVariance = 0.3F;

    private static final ModConfigSpec.DoubleValue WIND_RAIN_GUST_FREQUENCY = BUILDER
        .translation(String.format("%s.configuration.wind.gust_frequency", UnifiedWind.MODID))
        .defineInRange("wind.rain.gustFrequency", 0.2, 0, Double.MAX_VALUE);
    public static float windRainGustFrequency = 0.2F;

    private static final ModConfigSpec.DoubleValue WIND_RAIN_MODULATION_SPEED = BUILDER
        .translation(String.format("%s.configuration.wind.modulation_speed", UnifiedWind.MODID))
        .defineInRange("wind.rain.modulationSpeed", 0.04, 0, Double.MAX_VALUE);
    public static float windRainModulationSpeed = 0.04F;

    private static final ModConfigSpec.DoubleValue WIND_RAIN_DIRECTION_VARIANCE = BUILDER
        .translation(String.format("%s.configuration.wind.direction_variance", UnifiedWind.MODID))
        .defineInRange("wind.rain.directionVariance", 0.002, 0, Double.MAX_VALUE);
    public static float windRainDirectionVariance = 0.002F;

    private static final ModConfigSpec.DoubleValue WIND_STORM_STRENGTH = BUILDER
        .translation(String.format("%s.configuration.wind.strength", UnifiedWind.MODID))
        .defineInRange("wind.storm.strength", 3.2, 0, Double.MAX_VALUE);
    public static float windStormStrength = 3.2F;

    private static final ModConfigSpec.DoubleValue WIND_STORM_STRENGTH_VARIANCE = BUILDER
        .translation(String.format("%s.configuration.wind.strength_variance", UnifiedWind.MODID))
        .defineInRange("wind.storm.strengthVariance", 0.3, 0, Double.MAX_VALUE);
    public static float windStormStrengthVariance = 0.3F;

    private static final ModConfigSpec.DoubleValue WIND_STORM_GUST_FREQUENCY = BUILDER
        .translation(String.format("%s.configuration.wind.gust_frequency", UnifiedWind.MODID))
        .defineInRange("wind.storm.gustFrequency", 0.15, 0, Double.MAX_VALUE);
    public static float windStormGustFrequency = 0.15F;

    private static final ModConfigSpec.DoubleValue WIND_STORM_MODULATION_SPEED = BUILDER
        .translation(String.format("%s.configuration.wind.modulation_speed", UnifiedWind.MODID))
        .defineInRange("wind.storm.modulationSpeed", 0.04, 0, Double.MAX_VALUE);
    public static float windStormModulationSpeed = 0.04F;

    private static final ModConfigSpec.DoubleValue WIND_STORM_DIRECTION_VARIANCE = BUILDER
        .translation(String.format("%s.configuration.wind.direction_variance", UnifiedWind.MODID))
        .defineInRange("wind.storm.directionVariance", 0.002, 0, Double.MAX_VALUE);
    public static float windStormDirectionVariance = 0.002F;

    private static final ModConfigSpec.BooleanValue WIND_Y_LEVEL_ADJUSTMENT = BUILDER
        .translation(String.format("%s.configuration.wind.y_level_adjustment", UnifiedWind.MODID))
        .comment("Makes wind stronger at higher elevations, and weaker at lower elevations")
        .define("wind.yLevelAdjustment", true);
    public static boolean windYLevelAdjustment = true;

    private static final ModConfigSpec.BooleanValue WIND_UNDERWATER = BUILDER
        .translation(String.format("%s.configuration.wind.underwater", UnifiedWind.MODID))
        .define("wind.underwater", false);
    public static boolean windUnderwater = false;

    // compat
    private static final ModConfigSpec.BooleanValue COMPAT_VANILLA_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.vanilla", UnifiedWind.MODID))
        .comment("When enabled, many built-in particles will follow unified wind.")
        .define("compat.vanilla", true);
    public static boolean compatVanillaEnabled = true;

    private static final ModConfigSpec.BooleanValue COMPAT_BURNT_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.burnt", UnifiedWind.MODID))
        .comment("When enabled, Burnt's smoke and ember particles will follow unified wind.")
        .define("compat.burnt", true);
    public static boolean compatBurntEnabled = true;

    private static final ModConfigSpec.BooleanValue COMPAT_PARTICLE_RAIN_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.particle_rain", UnifiedWind.MODID))
        .comment("When enabled, Particle Rain's weather particles will follow unified wind.")
        .define("compat.particleRain", true);
    public static boolean compatParticleRainEnabled = true;

    private static final ModConfigSpec.BooleanValue COMPAT_SOOTY_CHIMNEYS_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.sooty_chimneys", UnifiedWind.MODID))
        .comment("When enabled, Sooty Chimneys' smoke particles will follow unified wind.")
        .define("compat.sootyChimneys", true);
    public static boolean compatSootyChimneysEnabled = true;

    private static final ModConfigSpec.BooleanValue COMPAT_SIMPLE_CLOUDS_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.simple_clouds", UnifiedWind.MODID))
        .comment("When enabled, some logic in Simple Clouds is altered to make unified wind behave correctly.")
        .define("compat.simpleClouds", true);
    public static boolean compatSimpleCloudsEnabled = true;

    private static final ModConfigSpec.BooleanValue COMPAT_FALLING_LEAVES_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.falling_leaves", UnifiedWind.MODID))
        .comment("When enabled, some logic in Simple Clouds is altered to make unified wind behave correctly.")
        .define("compat.fallingLeaves", true);
    public static boolean compatFallingLeavesEnabled = true;

    static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        windStrength = WIND_STRENGTH.get().floatValue();
        windStrengthVariance = WIND_STRENGTH_VARIANCE.get().floatValue();
        windGustFrequency = WIND_GUST_FREQUENCY.get().floatValue();
        windModulationSpeed = WIND_MODULATION_SPEED.get().floatValue();
        windDirectionVariance = WIND_DIRECTION_VARIANCE.get().floatValue();
        windRainStrength = WIND_RAIN_STRENGTH.get().floatValue();
        windRainStrengthVariance = WIND_RAIN_STRENGTH_VARIANCE.get().floatValue();
        windRainGustFrequency = WIND_RAIN_GUST_FREQUENCY.get().floatValue();
        windRainModulationSpeed = WIND_RAIN_MODULATION_SPEED.get().floatValue();
        windRainDirectionVariance = WIND_RAIN_DIRECTION_VARIANCE.get().floatValue();
        windStormStrength = WIND_STORM_STRENGTH.get().floatValue();
        windStormStrengthVariance = WIND_STORM_STRENGTH_VARIANCE.get().floatValue();
        windStormGustFrequency = WIND_STORM_GUST_FREQUENCY.get().floatValue();
        windStormModulationSpeed = WIND_STORM_MODULATION_SPEED.get().floatValue();
        windStormDirectionVariance = WIND_STORM_DIRECTION_VARIANCE.get().floatValue();

        windYLevelAdjustment = WIND_Y_LEVEL_ADJUSTMENT.get();
        windUnderwater = WIND_UNDERWATER.get();

        compatVanillaEnabled = COMPAT_VANILLA_ENABLED.get();
        compatBurntEnabled = COMPAT_BURNT_ENABLED.get();
        compatParticleRainEnabled = COMPAT_PARTICLE_RAIN_ENABLED.get();
        compatSootyChimneysEnabled = COMPAT_SOOTY_CHIMNEYS_ENABLED.get();
        compatSimpleCloudsEnabled = COMPAT_SIMPLE_CLOUDS_ENABLED.get();
        compatFallingLeavesEnabled = COMPAT_FALLING_LEAVES_ENABLED.get();
    }
}
