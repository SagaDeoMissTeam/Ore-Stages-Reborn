package net.sixik.orestages.CrT.action;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import net.minecraft.world.level.block.state.BlockState;
import net.sixik.orestages.StageContainer;

public class AddStageBlockInfo implements IRuntimeAction {
    private final String stage;
    private final BlockState block;
    private final BlockState blockReplace;
    private final boolean explosion;

    public AddStageBlockInfo(String stage, BlockState block, BlockState blockReplace, boolean explosion) {
        this.stage = stage;
        this.block = block;
        this.blockReplace = blockReplace;
        this.explosion = explosion;
    }

    @Override
    public void apply() {
        StageContainer.getOrCreateBlock(this.stage, this.block, this.blockReplace, this.explosion);
    }

    @Override
    public String describe() {
        return null;
    }
}
