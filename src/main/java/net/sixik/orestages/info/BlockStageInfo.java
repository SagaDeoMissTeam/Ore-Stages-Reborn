package net.sixik.orestages.info;

import net.minecraft.world.level.block.state.BlockState;

public class BlockStageInfo {
    private String stage;
    private BlockState block;
    private BlockState blockReplace;

    private boolean explosion;
    public BlockStageInfo(String stage, BlockState block, BlockState blockReplace, boolean explosion) {
        this.stage = stage;
        this.block = block;
        this.blockReplace = blockReplace;
        this.explosion = explosion;
    }

    public BlockState getBlock() {
        return block;
    }

    public BlockState getBlockReplace() {
        return blockReplace;
    }

    public String getStage() {
        return stage;
    }

    public boolean isExplosion() {
        return explosion;
    }
}
