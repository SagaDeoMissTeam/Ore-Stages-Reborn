//package net.sixik.orestages;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraftforge.network.NetworkDirection;
//import net.minecraftforge.network.NetworkRegistry;
//import net.minecraftforge.network.PacketDistributor;
//import net.minecraftforge.network.simple.SimpleChannel;
//
//public class SDMNetwork {
//
//    private static SimpleChannel INSTANCE;
//
//    private static int packetID = 0;
//    private static int id(){
//        return packetID++;
//    }
//
//    public static void register(){
//        SimpleChannel net = NetworkRegistry.ChannelBuilder
//                .named(new ResourceLocation(Orestages.MODID, "message"))
//                .networkProtocolVersion(() -> "1.0")
//                .clientAcceptedVersions(s -> true)
//                .serverAcceptedVersions(s -> true)
//                .simpleChannel();
//
//        INSTANCE = net;
//    }
//
//    public static <MSG> void sendToServer(MSG message){
//        INSTANCE.sendToServer(message);
//    }
//
//    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
//        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
//    }
//}
