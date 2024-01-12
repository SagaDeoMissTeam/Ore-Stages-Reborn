package net.sixik.orestages;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.sixik.orestages.info.BlockStageInfo;

import java.util.ArrayList;
import java.util.List;

public class StageContainer extends SimplePreparableReloadListener<Void> {
    public static final StageContainer INSTANCE = new StageContainer();
    public static final List<BlockStageInfo> GLOBAL_BLOCK_INFO = new ArrayList<>();


    public static BlockStageInfo getOrCreateBlock(String stage, BlockState block, BlockState blockReplace, boolean explosion){
        final BlockStageInfo info = new BlockStageInfo(stage, block, blockReplace, explosion);
        if(GLOBAL_BLOCK_INFO.contains(info)) return info;
        GLOBAL_BLOCK_INFO.add(info);
        return info;
    }

    @Override
    protected Void prepare(ResourceManager p_10796_, ProfilerFiller p_10797_) {
        return null;
    }

    @Override
    protected void apply(Void p_10793_, ResourceManager p_10794_, ProfilerFiller p_10795_) {
        this.GLOBAL_BLOCK_INFO.clear();
    }
}
