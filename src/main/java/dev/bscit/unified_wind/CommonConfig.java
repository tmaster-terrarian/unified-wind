package dev.bscit.unified_wind;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

import javax.annotation.Nullable;
import java.util.List;

@Config(name = UnifiedWind.MODID)
public class CommonConfig extends PartitioningSerializer.GlobalData
{
    public static CommonConfig get()
    {
        return AutoConfig.getConfigHolder(CommonConfig.class).getConfig();
    }

    @ConfigEntry.Category("wind")
    @ConfigEntry.Gui.TransitiveObject
    public WindSection wind = new WindSection();

    @ConfigEntry.Category("compat")
    @ConfigEntry.Gui.TransitiveObject
    public CompatSection compat = new CompatSection();

    @Config(name = "wind")
    public static class WindSection implements ConfigData
    {
        @ConfigEntry.Category("global")
        @ConfigEntry.Gui.Tooltip
        public boolean yLevelAdjustment = true;

        public boolean allowUnderwater = false;

        public List<String> windyDimensions = List.of(
            "minecraft:overworld",
            "minecraft:the_end"
        );

        @ConfigEntry.Category("weather")
        @ConfigEntry.Gui.CollapsibleObject
        public WindSettings base = new WindSettings("base");

        @ConfigEntry.Gui.CollapsibleObject
        public WindSettings rain = new WindSettings("rain");

        @ConfigEntry.Gui.CollapsibleObject
        public WindSettings storm = new WindSettings("storm");
    }

    @Config(name = "compat")
    public static class CompatSection implements ConfigData
    {
        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip
        public boolean vanilla = true;

        @ConfigEntry.Gui.Tooltip
        public boolean burnt = true;

        @ConfigEntry.Gui.Tooltip
        public boolean particleRain = true;

        @ConfigEntry.Gui.Tooltip
        public boolean sootyChimneys = true;

        @ConfigEntry.Gui.Tooltip
        public boolean simpleClouds = true;

        @ConfigEntry.Gui.Tooltip
        public boolean fallingLeaves = true;

        @ConfigEntry.Gui.Tooltip
        public boolean grassierGrass = true;

        @ConfigEntry.Gui.Tooltip
        public boolean visuality = true;

        @ConfigEntry.Gui.Tooltip
        public boolean particular = true;
    }

    public static class WindSettings
    {
        public float strength;
        public float strengthVariance;
        public float gustFrequency;
        public float modulationSpeed = 0.04f;
        public float directionVariance = 0.002f;

        public WindSettings(@Nullable String preset)
        {
            switch(preset)
            {
                case "base":
                    strength = 0.2f;
                    strengthVariance = 0.15f;
                    gustFrequency = 0.1f;
                    break;
                case "rain":
                    strength = 0.4f;
                    strengthVariance = 0.3f;
                    gustFrequency = 0.15f;
                    break;
                case "storm":
                    strength = 3.2f;
                    strengthVariance = 0.3f;
                    gustFrequency = 0.2f;
                    break;
                case null, default:
                    break;
            }
        }
    }
}
