package net.sixik.orestages.mixin;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.sixik.orestages.StageContainer;
import net.sixik.orestages.info.BlockStageInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ExplosionDamageCalculator.class)
public class ExplosionDamageGeneratorMixin {

    @Inject(method = "getBlockExplosionResistance", at = @At("HEAD"), cancellable = true)
    public void getBlockExplosionResistance(Explosion p_46099_, BlockGetter p_46100_, BlockPos pos, BlockState blockState, FluidState p_46103_, CallbackInfoReturnable<Optional<Float>> cir){
        cir.cancel();
        if(blockState.isAir() && p_46103_.isEmpty()) cir.setReturnValue(Optional.empty());

        if(!StageContainer.GLOBAL_BLOCK_INFO.isEmpty()){
            for(BlockStageInfo info : StageContainer.GLOBAL_BLOCK_INFO){
                if(info.getBlock().equals(blockState) || info.getBlockReplace().equals(blockState)) {
                    if (info.isExplosion()) {
                        ServerPlayer playerSelected = null;
                        ServerPlayer serverPlayerS = null;
                        for (int i = 0; i < ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerCount(); i++) {
                            playerSelected = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().get(i);
                            for (int i1 = 0; i1 < ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerCount(); i1++) {
                                if (ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().get(i1) != playerSelected) {
                                    if (playerSelected.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().get(i1).distanceToSqr(pos.getX(), pos.getY(), pos.getZ())) {
                                        serverPlayerS = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().get(i1);
                                    }
                                }
                            }
                        }
                        if(serverPlayerS != null){
                            playerSelected = serverPlayerS;
                        }
                        if (playerSelected != null) {
                            if (!GameStageHelper.hasStage(playerSelected, info.getStage())) {
                                cir.setReturnValue(Optional.of(99999f));
                                return;
                            }
                        }
                    }
                }
            }
        }

        cir.setReturnValue(Optional.of(Math.max(blockState.getExplosionResistance(p_46100_, pos, p_46099_), p_46103_.getExplosionResistance(p_46100_, pos, p_46099_))));
    }
}
