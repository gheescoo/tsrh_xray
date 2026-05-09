/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package tsrh.xraying.client.mixins.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsrh.xraying.client.XrayETL;

@Mixin(value = AbstractBlockRenderContext.class, remap = false)
public abstract class SodiumBlockOcclusionCacheMixin {

    @Shadow protected BlockState state;
    @Shadow protected BlockPos pos; 
    @Shadow protected LevelSlice slice;

    // For More Culling compatibility - runs before More Culling's inject to force-render whitelisted Xray blocks
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private void meteor$forceXrayFace(Direction facing, CallbackInfoReturnable<Boolean> cir) {
        if (XrayETL.isXrayActive && XrayETL.notBlocked(state.getBlock(), null)) {
            cir.setReturnValue(true);
        }
    }

    @ModifyReturnValue(method = "shouldDrawSide", at = @At("RETURN"))
    private boolean shouldDrawSide(boolean original, Direction facing) {
        if (!XrayETL.isXrayActive) return original;
        return XrayETL.shouldDrawSide(state, slice, pos, facing, original);
    }
}
