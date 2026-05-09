package tsrh.xraying.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import tsrh.xraying.client.config.Configs;

import java.util.List;

import static tsrh.xraying.client.Xray.mc;


public class XrayETL {
    // Load Config as status
    public static boolean isXrayActive = Configs.Generic.XRAY.getBooleanValue();

    public static int alphaWhitelist = Configs.Generic.XRAY_ALPHA.getIntegerValue();
    public static int alphaBlacklist = Configs.Generic.OTHER_ALPHA.getIntegerValue();

    public static final List<Block> ORES = List.of(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.ANCIENT_DEBRIS);

    public static List<Block> WHITE_LIST = ORES;
//    public static List<Block> BLACK_LIST = null;

/**
    Block is blocked when it's not in the whitelist,
    and is not exposed
 */
    public static boolean notBlocked(Block block, BlockPos blockPos) {
        return WHITE_LIST.contains(block)
                && (blockPos == null || !isExposed(blockPos));
    }
    public static boolean isBlocked(Block block, BlockPos blockPos) {return !notBlocked(block, blockPos);}

    private static final ThreadLocal<BlockPos.Mutable> EXPOSED_POS = ThreadLocal.withInitial(BlockPos.Mutable::new);

    public static boolean isExposed(BlockPos blockPos) {
        if (mc.world != null) {
            for (Direction direction : Direction.values()) {
                if (!mc.world.getBlockState(EXPOSED_POS.get().set(blockPos, direction)).isOpaqueFullCube()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean shouldDrawSide(BlockState state, BlockView view, BlockPos pos, Direction facing, boolean returns) {
        if (!returns && notBlocked(state.getBlock(), pos)) {
            BlockPos adjPos = pos.offset(facing);
            BlockState adjState = view.getBlockState(adjPos);
            return adjState.getCullingFace(facing.getOpposite()) != VoxelShapes.fullCube() || adjState.getBlock() != state.getBlock() || !adjState.isOpaqueFullCube() || isBlocked(adjState.getBlock(), adjPos);
        }

        return returns;
    }

    public static int getAlpha(BlockState state, BlockPos pos) {
        if(!isXrayActive) return -1;

        if(WHITE_LIST.contains(state.getBlock())) return alphaWhitelist;

        return alphaBlacklist;
    }
}
