package net.sixik.orestages.CrT;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sixik.orestages.CrT.action.AddStageBlockInfo;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@Document("mods/orestages/OreStages")
@ZenCodeType.Name("mods.orestages.OreStages")
public class CraftTweaker {


    @ZenCodeType.Method
    public static void addOreStage(String stage, BlockState block){
        CraftTweakerAPI.apply(new AddStageBlockInfo(stage, block, Blocks.STONE.defaultBlockState(), false));
    }
    @ZenCodeType.Method
    public static void addOreStage(String stage, BlockState block, boolean explosion){
        CraftTweakerAPI.apply(new AddStageBlockInfo(stage, block, Blocks.STONE.defaultBlockState(), explosion));
    }

    /**
     * @param stage - The Block Stage
     * @param blockState - Block need replace
     * @param replaceBlock - Replaced Block
     */
    @ZenCodeType.Method
    public static void addOreStage(String stage, BlockState blockState, BlockState replaceBlock){
        CraftTweakerAPI.apply(new AddStageBlockInfo(stage,blockState,replaceBlock, false));
    }
    @ZenCodeType.Method
    public static void addOreStage(String stage, BlockState blockState, BlockState replaceBlock, boolean explosion){
        CraftTweakerAPI.apply(new AddStageBlockInfo(stage,blockState,replaceBlock, explosion));
    }
}
