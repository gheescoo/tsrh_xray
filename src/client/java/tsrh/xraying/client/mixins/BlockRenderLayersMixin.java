package tsrh.xraying.client.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayers;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tsrh.xraying.client.XrayETL;

import static tsrh.xraying.client.config.Configs.Generic.XRAY_ALPHA;

@Mixin(BlockRenderLayers.class)
public class BlockRenderLayersMixin {
        @Inject(method = "getBlockLayer", at = @At("HEAD"), cancellable = true)
        private static void onGetBlockLayer(BlockState state, CallbackInfoReturnable<BlockRenderLayer> cir) {
            if (!XrayETL.isXrayActive) return;
//
            int alpha = XrayETL.getAlpha(state, null);
            if (0 < alpha && alpha < 255) cir.setReturnValue(BlockRenderLayer.TRANSLUCENT);
        }

        @Inject(method = "getFluidLayer", at = @At("HEAD"), cancellable = true)
        private static void onGetFluidLayer(FluidState state, CallbackInfoReturnable<BlockRenderLayer> cir) {
            int alpha = XrayETL.getAlpha(state.getBlockState(), null);
            if (alpha > 0 && alpha < 255) {
                cir.setReturnValue(BlockRenderLayer.TRANSLUCENT);
            }

//            else {
//                Ambience ambience = Modules.get().get(Ambience.class);
//                int a = ambience.lavaColor.get().a;
//                if (ambience.isActive() && ambience.customLavaColor.get() && a > 0 && a < 255) {
//                    cir.setReturnValue(RenderLayer.TRANSLUCENT);
//                }
//            }
        }
}

