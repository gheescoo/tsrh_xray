/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package tsrh.xraying.client.mixins.sodium;

import net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tsrh.xraying.client.XrayETL;

@Mixin(value = LightDataAccess.class, remap = false)
public abstract class SodiumLightDataAccessMixin {
    @Unique
    private static final int FULL_LIGHT = 15 | 15 << 4 | 15 << 8;

    @Shadow
    protected BlockRenderView level;
    @Shadow @Final
    private BlockPos.Mutable pos;

    @ModifyVariable(method = "compute", at = @At(value = "TAIL"), name = "bl")
    private int compute_modifyBL(int bl) {
        if (XrayETL.isXrayActive) {
            BlockState state = level.getBlockState(pos);
            if (XrayETL.notBlocked(state.getBlock(), pos)) return FULL_LIGHT;
        }

        return bl;
    }

    // fullbright
//
//    @ModifyVariable(method = "compute", at = @At(value = "STORE"), name = "sl")
//    private int compute_assignSL(int sl) {
//        return Math.max(fb.getLuminance(LightType.SKY), sl);
//    }
//
//    @ModifyVariable(method = "compute", at = @At(value = "STORE"), name = "bl")
//    private int compute_assignBL(int bl) {
//        return Math.max(fb.getLuminance(LightType.BLOCK), bl);
//    }
}
