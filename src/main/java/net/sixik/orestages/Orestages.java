package net.sixik.orestages;

import com.mojang.logging.LogUtils;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
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

    }


    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = Orestages.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Event{

        @SubscribeEvent
        public void onGameStageAdd(GameStageEvent.Add event){
            if(event.getEntity() == Minecraft.getInstance().player) {


            }
        }

        @SubscribeEvent
        public void onGameStageRemove(GameStageEvent.Remove event){
            if(event.getEntity() == Minecraft.getInstance().player) {
                Minecraft.getInstance().levelRenderer.allChanged();
            }
        }
    }
}
