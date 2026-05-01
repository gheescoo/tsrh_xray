/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package tsrh.xraying.client.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tsrh.xraying.client.XrayETL;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {
    @Unique private final ThreadLocal<Integer> alphas = new ThreadLocal<>();
    @Unique private final ThreadLocal<Boolean> ambient = ThreadLocal.withInitial(() -> false);

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo info) {
//        Ambience ambience = Modules.get().get(Ambience.class);
//        ambient.set(ambience.isActive() && ambience.customLavaColor.get() && fluidState.isIn(FluidTags.LAVA));
//
//        // Xray and Wallhack
        int alpha = XrayETL.getAlpha(fluidState.getBlockState(), pos);
        if (alpha == 0) info.cancel();
        else alphas.set(alpha);
    }

    @Inject(method = "vertex", at = @At("HEAD"), cancellable = true)
    private void onVertex(VertexConsumer vertexConsumer, float x, float y, float z, float red, float green, float blue, float u, float v, int light, CallbackInfo info) {
        int alpha = alphas.get();

        if (ambient.get()) {
//            Color color = Modules.get().get(Ambience.class).lavaColor.get();
            vertex(vertexConsumer, x, y, z, 0x39, 0xc5, 0xbb, alpha, u, v, light);
            info.cancel();
        }
        else if (alpha != -1) {
            vertex(vertexConsumer, x, y, z, (int) (red * 255), (int) (green * 255), (int) (blue * 255), alpha, u, v, light);
            info.cancel();
        }
    }

    @Unique
    private void vertex(VertexConsumer vertexConsumer, float x, float y, float z, int red, int green, int blue, int alpha, float u, float v, int light) {
        vertexConsumer.vertex(x, y, z).color(red, green, blue, alpha).texture(u, v).light(light).normal(0.0f, 1.0f, 0.0f);
    }
}
