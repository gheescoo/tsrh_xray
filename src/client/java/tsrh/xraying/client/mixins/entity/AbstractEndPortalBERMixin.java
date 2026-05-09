//package tsrh.xraying.client.mixins.entity;
//
//import net.minecraft.client.gl.RenderPipelines;
//import net.minecraft.client.render.RenderLayer;
//import net.minecraft.client.render.RenderLayers;
//import net.minecraft.client.render.RenderSetup;
//import net.minecraft.client.render.block.entity.AbstractEndPortalBlockEntityRenderer;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyArg;
//import tsrh.xraying.client.XrayETL;
//
//@Mixin(AbstractEndPortalBlockEntityRenderer.class)
//public class AbstractEndPortalBERMixin {
//    @ModifyArg(
//            method = "render(Lnet/minecraft/client/render/block/entity/state/EndPortalBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
//            at = @At(value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitCustom(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue$Custom;)V"),
//            index = 1)
//    private static RenderLayer applyTranslucent(RenderLayer renderLayer){
//        if(!XrayETL.isXrayActive) return renderLayer;
//        return RenderLayer.of(
//                "end_portal_transparent",
//                RenderSetup.builder(RenderPipelines.END_PORTAL) // Uses the portal pipeline[cite: 2]
//                        .texture("Sampler0", AbstractEndPortalBlockEntityRenderer.SKY_TEXTURE)
//                        .texture("Sampler1", AbstractEndPortalBlockEntityRenderer.PORTAL_TEXTURE)
//                        .translucent() // Forces the alpha blending
//                        .build()
//        );
//    }
//}
