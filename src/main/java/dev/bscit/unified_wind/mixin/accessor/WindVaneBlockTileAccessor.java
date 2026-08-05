package dev.bscit.unified_wind.mixin.accessor;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.mehvahdjukaar.supplementaries.common.block.tiles.WindVaneBlockTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Restriction(require = {
    @Condition("supplementaries")
})
@Mixin(WindVaneBlockTile.class)
public interface WindVaneBlockTileAccessor
{
    @Accessor("windChargedTicks")
    int unifiedWind$getWindChargedTicks();
}
