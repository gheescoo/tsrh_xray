/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package tsrh.xraying.client.mixins.indigo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tsrh.xraying.client.XrayETL;

import java.util.Random;

@Mixin(BlockRenderInfo.class)
public abstract class BlockRenderInfoMixin {
    @Shadow
    public BlockState blockState;

    @Shadow
    public BlockRenderView blockView;

    @Shadow
    public BlockPos blockPos;

    @ModifyReturnValue(method = "shouldDrawSide", at = @At("RETURN"))
    private boolean modifyShouldDrawSide(boolean original, Direction side) {
        if (XrayETL.isXrayActive) {
            return XrayETL.shouldDrawSide(blockState, blockView, blockPos, side, original);
        }
        return original;
    }
}
