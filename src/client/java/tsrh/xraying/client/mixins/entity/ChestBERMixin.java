package tsrh.xraying.client.mixins.entity;

import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import tsrh.xraying.client.XrayETL;
import tsrh.xraying.client.config.Configs;
import tsrh.xraying.client.render.TsrhRenderLayers;

@Mixin(ChestBlockEntityRenderer.class)
public class ChestBERMixin {
    @Redirect(
            method = "render(Lnet/minecraft/client/render/block/entity/state/ChestBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getRenderLayer(Ljava/util/function/Function;)Lnet/minecraft/client/render/RenderLayer;")
    )
    private RenderLayer getTranslucentLayer(SpriteIdentifier instance, java.util.function.Function<net.minecraft.util.Identifier, RenderLayer> layerFactory) {
        if (!XrayETL.isXrayActive) {
            return instance.getRenderLayer(layerFactory);
        }

        return instance.getRenderLayer(TsrhRenderLayers::entityChest);
//        return instance.getRenderLayer(RenderLayers::entitySmoothCutout);
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/client/render/block/entity/state/ChestBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"),
            index = 6
    )
    private int applyCustomAlpha(int originalColor) {
        if (!XrayETL.isXrayActive) {
            return originalColor;
        }

        return 0x00FFFFFF | (Configs.BlockEntities.CHEST_ALPHA.getIntegerValue() << 24);
    }
}
