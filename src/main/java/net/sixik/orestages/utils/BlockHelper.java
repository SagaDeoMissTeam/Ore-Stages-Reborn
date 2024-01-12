package net.sixik.orestages.utils;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sixik.orestages.StageContainer;
import net.sixik.orestages.info.BlockStageInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockHelper {
    private static Player currentPlayer;

    public static void setPlayer(@Nullable Player player) {
        currentPlayer = player;
    }

    public static @Nullable Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static BlockState getBlockState(BlockPos blockPos, Level level) {
        return level.getBlockState(blockPos);
    }

    public static Block getBlock(BlockState blockState) {
        return blockState.getBlock();
    }

    public static Block getBlock(ResourceLocation block) {
        return Registry.BLOCK.get(block);
    }

    public static boolean isEmptyBlock(Block block) {
        var defaultBlock = Registry.BLOCK.get((ResourceLocation) null);

        return !isBlock(block) || defaultBlock.defaultBlockState().is(block);
    }

    public static boolean isBlock(Block block) {
        return block != null;
    }

    public static boolean isBlock(BlockState blockState) {
        return blockState != null;
    }

    public static boolean isReplacedBlock(BlockState a, Block b) {
        return isBlock(a) && isBlock(b) && !a.is(b);
    }

    public static boolean isReplacedBlock(BlockState a, BlockState b) {
        return isBlock(a) && isBlock(b) && !b.is(a.getBlock());
    }

    public static ResourceLocation getBlockName(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    public static ResourceLocation getBlockName(BlockState blockState) {
        return getBlockName(getBlock(blockState));
    }


    public static BlockState getReplacement(Player player, BlockState original, BlockPos pos) {

        BlockState replace = null;
        for(BlockStageInfo info : StageContainer.GLOBAL_BLOCK_INFO){
            if(info.getBlock().equals(original)){
                if(!GameStageHelper.hasStage(player, info.getStage())){
                    replace = info.getBlockReplace();
                    return replace;
                }
            }
        }

        return original;
    }

    public static int getReplacementId(BlockState original, @Nullable BlockPos pos) {
        BlockState replacement = original;

        // Inject replacement block
        if (currentPlayer != null) {
            // Slightly hacky here but we don't know the blockPos when this method is called in Palettes so we're assuming the player's current location for biome matches
            var actualPos = pos == null ? currentPlayer.blockPosition() : pos;

            var maybeReplacement = getReplacement(currentPlayer, original, actualPos);

            if (isReplacedBlock(original, maybeReplacement)) {
                replacement = maybeReplacement;
            }
        }

        return Block.BLOCK_STATE_REGISTRY.getId(replacement);
    }

    private static final List<ItemStack> EMPTY_DROPS = new ArrayList<>();

    public static List<ItemStack> getDrops(Player player, BlockState original, ServerLevel serverLevel, BlockPos blockPos, @Nullable BlockEntity blockEntity, ItemStack tool) {
        var replacement = getReplacement(player, original, blockPos);

        // Determine drops from replacement block
        if (isReplacedBlock(original, replacement)) {
            var drops = Block.getDrops(replacement, serverLevel, blockPos, blockEntity, player, tool);

            return drops;
        }

        return EMPTY_DROPS;
    }
}
