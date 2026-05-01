package tsrh.xraying.client.mixins;

//import fr.atesab.xray.XrayMain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = Block.class)
public class DiscontinuedXray {
	@Inject(at = @At("RETURN"), method = "shouldDrawSide(" + "Lnet/minecraft/block/BlockState;" + // state
			"Lnet/minecraft/block/BlockState;" + // adjacentState
			"Lnet/minecraft/util/math/Direction;" + // side (unused)
			")Z", // ci
			cancellable = true)
	private static void shouldDrawSide(BlockState state, BlockState adjacentState, Direction side,
			CallbackInfoReturnable<Boolean> ci) {
		Identifier blockId = Registries.BLOCK.getId(state.getBlock());
		Identifier diamond_ore_id = Identifier.ofVanilla("diamond_ore");

		Random r = new Random();
//		if(blockId.equals(diamond_ore_id)) {
		if(r.nextBoolean()) {
			ci.setReturnValue(true);
		} else {
			ci.setReturnValue(false);
		}
	}

	private DiscontinuedXray() {
	}
}