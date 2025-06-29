package cn.foggyhillside.festival_delicacies.network;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.network.packet.ServerCookingTypeButtonPacket;
import cn.foggyhillside.festival_delicacies.network.packet.ServerRedstoneButtonPacket;
import cn.foggyhillside.festival_delicacies.network.packet.ServerStartButtonPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {

    private static SimpleChannel NETWORK;

    private static int packetID;
    private static int id(){
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(FestivalDelicacies.MODID, "network"))
                .networkProtocolVersion(() -> new ResourceLocation(FestivalDelicacies.MODID, "1").toString())
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        NETWORK = net;

        net.messageBuilder(ServerCookingTypeButtonPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerCookingTypeButtonPacket::new)
                .encoder(ServerCookingTypeButtonPacket::encode)
                .consumerMainThread(ServerCookingTypeButtonPacket::handle)
                .add();

        net.messageBuilder(ServerStartButtonPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerStartButtonPacket::new)
                .encoder(ServerStartButtonPacket::encode)
                .consumerMainThread(ServerStartButtonPacket::handle)
                .add();

        net.messageBuilder(ServerRedstoneButtonPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerRedstoneButtonPacket::new)
                .encoder(ServerRedstoneButtonPacket::encode)
                .consumerMainThread(ServerRedstoneButtonPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        NETWORK.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        NETWORK.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
