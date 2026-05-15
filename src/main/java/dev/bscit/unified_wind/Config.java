package dev.bscit.unified_wind;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = UnifiedWind.MODID)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue WIND_STRENGTH = BUILDER
        .defineInRange("wind.strength", 0.4, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue WIND_STRENGTH_VARIANCE = BUILDER
        .defineInRange("wind.strengthVariance", 0.3, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue WIND_GUST_FREQUENCY = BUILDER
        .defineInRange("wind.gustFrequency", 0.2, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue WIND_MODULATION_SPEED = BUILDER
        .defineInRange("wind.modulationSpeed", 0.04, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.BooleanValue WIND_Y_LEVEL_ADJUSTMENT = BUILDER
        .define("wind.yLevelAdjustment", true);

    private static final ModConfigSpec.BooleanValue COMPAT_BURNT_ENABLED = BUILDER
        .comment("When enabled, Burnt's smoke and ember particles will follow unified wind.")
        .define("compat.burnt", true);

    private static final ModConfigSpec.BooleanValue COMPAT_PARTICLE_RAIN_ENABLED = BUILDER
        .comment("When enabled, Particle Rain's weather particles will follow unified wind.")
        .define("compat.particleRain", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static float windStrength = 0.4F;
    public static float windStrengthVariance = 0.3F;
    public static float windGustFrequency = 0.2F;
    public static float windModulationSpeed = 0.04F;
    public static boolean windYLevelAdjustment = true;

    public static boolean compatBurntEnabled;
    public static boolean compatParticleRainEnabled;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        windStrength = WIND_STRENGTH.get().floatValue();
        windStrengthVariance = WIND_STRENGTH_VARIANCE.get().floatValue();
        windGustFrequency = WIND_GUST_FREQUENCY.get().floatValue();
        windModulationSpeed = WIND_MODULATION_SPEED.get().floatValue();
        windYLevelAdjustment = WIND_Y_LEVEL_ADJUSTMENT.get();

        compatBurntEnabled = COMPAT_BURNT_ENABLED.get();
        compatParticleRainEnabled = COMPAT_PARTICLE_RAIN_ENABLED.get();
    }
}
