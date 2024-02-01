package net.sixik.orestages;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sixik.orestages.info.BlockStageInfo;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Orestages.MODID)
public class Orestages {

    public static final String MODID = "orestages";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Orestages() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onReload);

    }

    private void onReload(AddReloadListenerEvent event){
        event.addListener(StageContainer.INSTANCE);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
//        SDMNetwork.register();
    }

    @Mod.EventBusSubscriber(modid = Orestages.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Event {

        @SubscribeEvent
        public static void onGameStageAdded(GameStageEvent.Added event) {
            for (BlockStageInfo info : StageContainer.GLOBAL_BLOCK_INFO) {
                if (info.getStage().equals(event.getStageName()) && event.getEntity() instanceof ServerPlayer player) {
                    reloadVisibleChunksForPlayer(player, (ServerLevel) player.level);
                }
            }
        }

        @SubscribeEvent
        public static void onGameStageRemoved(GameStageEvent.Remove event) {
            if (event.getEntity() instanceof ServerPlayer player)
                reloadVisibleChunksForPlayer(player, (ServerLevel) player.level);
        }

        @SubscribeEvent
        public static void onGameStageCleared(GameStageEvent.Cleared event) {
            if (event.getEntity() instanceof ServerPlayer player)
                reloadVisibleChunksForPlayer(player, (ServerLevel) player.level);
        }

        private static void reloadVisibleChunksForPlayer(ServerPlayer player, ServerLevel serverLevel) {
            // Obtain the player's current chunk coordinates
            int chunkX = player.chunkPosition().x;
            int chunkZ = player.chunkPosition().z;
            // Use the server's view distance for the range
            int viewDistance = serverLevel.getServer().getPlayerList().getViewDistance();
            LOGGER.info(String.valueOf(viewDistance));

            // Define the range of chunks around the player to reload. This is a 3x3 area centered on the player's current chunk.
            int range = viewDistance; // Adjust this value to change the number of chunks reloaded around the player.

            for (int dx = -range; dx <= range; dx++) {
                for (int dz = -range; dz <= range; dz++) {
                    // Calculate the coordinates of the chunk to reload
                    int reloadChunkX = chunkX + dx;
                    int reloadChunkZ = chunkZ + dz;

                    // Ensure the chunk is loaded before attempting to reload it
                    if (serverLevel.hasChunk(reloadChunkX, reloadChunkZ)) {
                        LevelChunk chunk = serverLevel.getChunk(reloadChunkX, reloadChunkZ);

                        // This packet includes both chunk data and light data
                        ClientboundLevelChunkWithLightPacket packet = new ClientboundLevelChunkWithLightPacket(chunk, player.getLevel().getLightEngine(), null, null, true);

                        player.trackChunk(chunk.getPos(),packet);
                        DebugPackets.sendPoiPacketsForChunk(serverLevel, chunk.getPos());
                        player.connection.send(packet);
                    }
                }
            }
        }
    }
}
