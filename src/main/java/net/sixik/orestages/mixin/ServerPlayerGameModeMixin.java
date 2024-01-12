package net.sixik.orestages.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sixik.orestages.utils.BlockHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Shadow
    @Final
    protected ServerPlayer player;

    private BlockState replaceBlock(BlockState original, BlockPos blockPos, String logAction) {
        var replacement = BlockHelper.getReplacement(player, original, blockPos);

        return replacement;
    }
    @Redirect(method = "handleBlockBreakAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState onBlockBreak(ServerLevel instance, BlockPos blockPos) {
        return replaceBlock(instance.getBlockState(blockPos), blockPos, "break");
    }
    @Redirect(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState onDestroyBlock(ServerLevel instance, BlockPos blockPos) {
        return replaceBlock(instance.getBlockState(blockPos), blockPos, "destroying");
    }

    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState onUseItemOn(Level instance, BlockPos blockPos) {
        return replaceBlock(instance.getBlockState(blockPos), blockPos, "right clicking");
    }
}
