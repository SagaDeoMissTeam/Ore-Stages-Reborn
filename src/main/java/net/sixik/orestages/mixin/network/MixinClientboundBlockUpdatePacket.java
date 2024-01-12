package net.sixik.orestages.mixin.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.sixik.orestages.InterceptedClientboundPacket;
import net.sixik.orestages.utils.BlockHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientboundBlockUpdatePacket.class)
public class MixinClientboundBlockUpdatePacket implements InterceptedClientboundPacket {

    @Shadow
    @Final
    @Mutable
    private BlockState blockState;

    @Shadow
    @Final
    private BlockPos pos;


    @Override
    public void interceptRestrictions(ServerPlayer player) {
        var newState = BlockHelper.getReplacement(player, blockState, pos);

        if (BlockHelper.isReplacedBlock(blockState, newState)) {
            blockState = newState;
        }
    }

}
