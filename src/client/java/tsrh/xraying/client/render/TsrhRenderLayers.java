package tsrh.xraying.client.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.OutputTarget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Function;

import static net.minecraft.client.gl.RenderPipelines.*;

public class TsrhRenderLayers {
    public static final RenderPipeline PIPE_ENTITY_TRANSLUCENT = RenderPipelines.register(
            RenderPipeline.builder(ENTITY_SNIPPET)
                    .withLocation("pipeline/entity_translucent")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withShaderDefine("PER_FACE_LIGHTING")
                    .withSampler("Sampler1")
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .withDepthWrite(false)
                    .build()
    );
    private static final Function<Identifier, RenderLayer> ENTITY_TRANSLUCENT = Util.memoize(
            texture -> {
                RenderSetup renderSetup = RenderSetup.builder(PIPE_ENTITY_TRANSLUCENT)
                        .texture("Sampler0", texture)
                        .useLightmap()
                        .useOverlay()
                        .translucent()
//                        .crumbling()
                        .outlineMode(RenderSetup.OutlineMode.AFFECTS_OUTLINE)
                        .build();
                return RenderLayer.of("entity_cutout", renderSetup);
            }
    );
    private static final Function<Identifier, RenderLayer> ENTITY_XRAY = Util.memoize(texture -> RenderLayer.of(
            "entity_xray",
            RenderSetup.builder(ENTITY_TRANSLUCENT_EMISSIVE)
                    .texture("Sampler0", texture)
                    .useOverlay()
                    .crumbling()
                    .translucent()
                    .outputTarget(OutputTarget.MAIN_TARGET)
                    .outlineMode(false ? RenderSetup.OutlineMode.AFFECTS_OUTLINE : RenderSetup.OutlineMode.NONE)
                    .build()
    ));


    /**
     * Pure Vanilla
     * @param texture
     * @return Cutout RenderLayer
     */
    @Unique
    public static RenderLayer entityChest(Identifier texture) {
        return ENTITY_XRAY.apply(texture);
    }
}
