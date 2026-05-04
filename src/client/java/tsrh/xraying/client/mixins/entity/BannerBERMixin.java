package tsrh.xraying.client.mixins.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BannerBlockEntityRenderer.class)
public class BannerBERMixin {
    @Redirect(
            method = "render(Lnet/minecraft/client/texture/SpriteHolder;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIFLnet/minecraft/client/render/block/entity/model/BannerBlockModel;Lnet/minecraft/client/render/block/entity/model/BannerFlagBlockModel;FLnet/minecraft/util/DyeColor;Lnet/minecraft/component/type/BannerPatternsComponent;Lnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getRenderLayer(Ljava/util/function/Function;)Lnet/minecraft/client/render/RenderLayer;"
            ))
    private static RenderLayer getTranslucentLayer(net.minecraft.client.util.SpriteIdentifier instance, java.util.function.Function<net.minecraft.util.Identifier, RenderLayer> layerFactory) {
        return instance.getRenderLayer(RenderLayers::entityTranslucent);
    }
}
