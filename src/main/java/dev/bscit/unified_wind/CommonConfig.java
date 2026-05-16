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
        .defineInRange("wind.strength", 0.4, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue WIND_STRENGTH_VARIANCE = BUILDER
        .translation(String.format("%s.configuration.wind.strength_variance", UnifiedWind.MODID))
        .defineInRange("wind.strengthVariance", 0.3, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue WIND_GUST_FREQUENCY = BUILDER
        .translation(String.format("%s.configuration.wind.gust_frequency", UnifiedWind.MODID))
        .defineInRange("wind.gustFrequency", 0.2, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue WIND_MODULATION_SPEED = BUILDER
        .translation(String.format("%s.configuration.wind.modulation_speed", UnifiedWind.MODID))
        .defineInRange("wind.modulationSpeed", 0.04, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue WIND_DIRECTION_VARIANCE = BUILDER
        .translation(String.format("%s.configuration.wind.direction_variance", UnifiedWind.MODID))
        .defineInRange("wind.directionVariance", 0.002, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue WIND_Y_LEVEL_ADJUSTMENT = BUILDER
        .translation(String.format("%s.configuration.wind.y_level_adjustment", UnifiedWind.MODID))
        .comment("Makes wind stronger at higher elevations, and weaker at lower elevations")
        .define("wind.yLevelAdjustment", true);

    // compat
    private static final ModConfigSpec.BooleanValue COMPAT_VANILLA_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.vanilla", UnifiedWind.MODID))
        .comment("When enabled, many built-in particles will follow unified wind.")
        .define("compat.vanilla", true);

    private static final ModConfigSpec.BooleanValue COMPAT_BURNT_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.burnt", UnifiedWind.MODID))
        .comment("When enabled, Burnt's smoke and ember particles will follow unified wind.")
        .define("compat.burnt", true);

    private static final ModConfigSpec.BooleanValue COMPAT_PARTICLE_RAIN_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.particle_rain", UnifiedWind.MODID))
        .comment("When enabled, Particle Rain's weather particles will follow unified wind.")
        .define("compat.particleRain", true);

    private static final ModConfigSpec.BooleanValue COMPAT_SOOTY_CHIMNEYS_ENABLED = BUILDER
        .translation(String.format("%s.configuration.compat.sooty_chimneys", UnifiedWind.MODID))
        .comment("When enabled, Sooty Chimneys' smoke particles will follow unified wind.")
        .define("compat.sootyChimneys", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static float windStrength = 0.4F;
    public static float windStrengthVariance = 0.3F;
    public static float windGustFrequency = 0.2F;
    public static float windModulationSpeed = 0.04F;
    public static float windDirectionVariance = 0.002F;
    public static boolean windYLevelAdjustment = true;

    public static boolean compatVanillaEnabled;
    public static boolean compatBurntEnabled;
    public static boolean compatParticleRainEnabled;
    public static boolean compatSootyChimneysEnabled;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        windStrength = WIND_STRENGTH.get().floatValue();
        windStrengthVariance = WIND_STRENGTH_VARIANCE.get().floatValue();
        windGustFrequency = WIND_GUST_FREQUENCY.get().floatValue();
        windModulationSpeed = WIND_MODULATION_SPEED.get().floatValue();
        windDirectionVariance = WIND_DIRECTION_VARIANCE.get().floatValue();
        windYLevelAdjustment = WIND_Y_LEVEL_ADJUSTMENT.get();

        compatVanillaEnabled = COMPAT_VANILLA_ENABLED.get();
        compatBurntEnabled = COMPAT_BURNT_ENABLED.get();
        compatParticleRainEnabled = COMPAT_PARTICLE_RAIN_ENABLED.get();
        compatSootyChimneysEnabled = COMPAT_SOOTY_CHIMNEYS_ENABLED.get();
    }
}
