package cn.foggyhillside.festival_delicacies.network.packet;

import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static cn.foggyhillside.festival_delicacies.blocks.StoveBlock.RED_STONE;

public class ServerRedstoneButtonPacket {

    private final BlockPos pos;

    public ServerRedstoneButtonPacket(BlockPos pos) {
        this.pos = pos;
    }

    public ServerRedstoneButtonPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            Player player = supplier.get().getSender();
            Level level = player.level();
            if (player instanceof ServerPlayer) {
                if (level.getBlockEntity(pos) instanceof StoveEntity stoveEntity) {
                    switch (stoveEntity.getBlockState().getValue(RED_STONE)){
                        case 0 -> level.setBlock(pos, stoveEntity.getBlockState().setValue(RED_STONE, 1), 2);
                        case 1 -> level.setBlock(pos, stoveEntity.getBlockState().setValue(RED_STONE, 0), 2);
                    }
                }
            }
        });

        supplier.get().setPacketHandled(true);
    }
}
