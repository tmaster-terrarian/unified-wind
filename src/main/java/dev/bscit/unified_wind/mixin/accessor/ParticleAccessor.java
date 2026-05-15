package dev.bscit.unified_wind.mixin.accessor;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor("xo")
    double unifiedWind$getXo();

    @Accessor("xo")
    void unifiedWind$setXo(double var1);

    @Accessor("yo")
    double unifiedWind$getYo();

    @Accessor("yo")
    void unifiedWind$setYo(double var1);

    @Accessor("zo")
    double unifiedWind$getZo();

    @Accessor("zo")
    void unifiedWind$setZo(double var1);

    @Accessor("x")
    double unifiedWind$getX();

    @Accessor("x")
    void unifiedWind$setX(double var1);

    @Accessor("y")
    double unifiedWind$getY();

    @Accessor("y")
    void unifiedWind$setY(double var1);

    @Accessor("z")
    double unifiedWind$getZ();

    @Accessor("z")
    void unifiedWind$setZ(double var1);

    @Accessor("xd")
    double unifiedWind$getXd();

    @Accessor("xd")
    void unifiedWind$setXd(double var1);

    @Accessor("yd")
    double unifiedWind$getYd();

    @Accessor("yd")
    void unifiedWind$setYd(double var1);

    @Accessor("zd")
    double unifiedWind$getZd();

    @Accessor("zd")
    void unifiedWind$setZd(double var1);
}
