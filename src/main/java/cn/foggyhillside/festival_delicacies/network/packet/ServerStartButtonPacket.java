package cn.foggyhillside.festival_delicacies.network.packet;

import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static cn.foggyhillside.festival_delicacies.blocks.StoveBlock.START_BUTTON;

public class ServerStartButtonPacket {

    private final BlockPos pos;

    public ServerStartButtonPacket(BlockPos pos) {
        this.pos = pos;
    }

    public ServerStartButtonPacket(FriendlyByteBuf buffer) {
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
                    switch (stoveEntity.getBlockState().getValue(START_BUTTON)){
                        case 0 -> level.setBlock(pos, stoveEntity.getBlockState().setValue(START_BUTTON, 1), 2);
                        case 1 -> level.setBlock(pos, stoveEntity.getBlockState().setValue(START_BUTTON, 0), 2);
                    }
                }
            }
        });

        supplier.get().setPacketHandled(true);
    }
}
