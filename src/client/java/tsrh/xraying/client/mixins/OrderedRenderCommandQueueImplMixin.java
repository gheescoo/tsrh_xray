package tsrh.xraying.client.mixins;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(OrderedRenderCommandQueueImpl.class)
public class OrderedRenderCommandQueueImplMixin {
    @ModifyVariable(method = "submitModel", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private RenderLayer modifyRenderLayer(RenderLayer original) {
        // This is a bit of a hack, but it allows us to change the render layer for all model rendering commands
        // You can add more conditions here to only apply this to certain models or under certain circumstances
//        return RenderLayer.getTranslucent();
        return original;
    }
}