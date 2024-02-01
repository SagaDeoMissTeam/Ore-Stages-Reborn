package net.sixik.orestages.mixin.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.sixik.orestages.InterceptedClientboundPacket;
import net.sixik.orestages.utils.BlockHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientboundLevelChunkWithLightPacket.class)
public abstract class ClientboundLevelChunkWithLightPacketMixin implements InterceptedClientboundPacket {


    @Shadow public abstract int getX();

    @Shadow public abstract int getZ();

    @Shadow public abstract ClientboundLevelChunkPacketData getChunkData();

    @Override
    public void interceptRestrictions(ServerPlayer player) {
        //var newState = BlockHelper.getReplacement(player, blockState, pos);
        ChunkPos chunkPos = new ChunkPos(getX(),getZ());
        BlockPos pos = chunkPos.getWorldPosition();

        int x = chunkPos.getMinBlockX();
        int y = player.level.getMinBuildHeight();
        int z = chunkPos.getMinBlockZ();



//        while (x <= chunkPos.getMaxBlockX()){
//            while (y <= player.level.getMaxBuildHeight()){
//                while (z <= chunkPos.getMaxBlockZ()){
//                    var newState = BlockHelper.getReplacement(player, player.level.getBlockState(new BlockPos(x,y,z)), new BlockPos(x,y,z));
//                    if(BlockHelper.isReplacedBlock(player.level.getBlockState(new BlockPos(x,y,z)), newState)){
//
//                    }
//                    z++;
//                }
//                y++;
//                z = chunkPos.getMinBlockZ();
//            }
//            x++;
//            y = player.level.getMinBuildHeight();
//        }
//        for(int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++){
//            for(int y = player.level.getMinBuildHeight(); y <= player.level.getMaxBuildHeight(); y++){
//                for(int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++){
//
//                }
//            }
//
//
//        }


    }
}
