package tsrh.xraying.client.mixins.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.ShulkerBoxBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ShulkerBoxBlockEntityRenderer.class)
public class ShulkerBERMixin {
    @Redirect(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IILnet/minecraft/util/math/Direction;FLnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;Lnet/minecraft/client/util/SpriteIdentifier;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/SpriteIdentifier;getRenderLayer(Ljava/util/function/Function;)Lnet/minecraft/client/render/RenderLayer;"
            )
    )
    private RenderLayer redirectRenderLayer(SpriteIdentifier instance, Function<Identifier, RenderLayer> layerFactory) {
        // Use our custom X-Ray layer instead of the vanilla one
        RenderLayer xrayLayer = tsrh.xraying.client.render.TsrhRenderLayers.entityChest(instance.getTextureId());

        // We still pass the provider to ensure it's managed correctly
        return instance.getRenderLayer(texture -> {
            layerFactory.apply(texture);
            return xrayLayer;
        });
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IILnet/minecraft/util/math/Direction;FLnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;Lnet/minecraft/client/util/SpriteIdentifier;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
            ),
            index = 6
    )
    private int modifyAlpha(int originalColor) {
        // Apply the custom alpha value from the config
//        int customAlpha = tsrh.xraying.client.config.Configs.BlockEntities.SHULKER_ALPHA.getIntegerValue();
        int customAlpha = 64;
        return (originalColor & 0x00FFFFFF) | (customAlpha << 24);
    }
}
