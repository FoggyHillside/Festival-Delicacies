package cn.foggyhillside.festival_delicacies.network.packet;

import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static cn.foggyhillside.festival_delicacies.blocks.StoveBlock.COOKING_TYPE;

public class ServerCookingTypeButtonPacket {

    private final BlockPos pos;

    public ServerCookingTypeButtonPacket(BlockPos pos) {
        this.pos = pos;
    }

    public ServerCookingTypeButtonPacket(FriendlyByteBuf buffer) {
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
                    int value = stoveEntity.getBlockState().getValue(COOKING_TYPE);
                    //7 is the maximum of COOKING_TYPE
                    if (value >= 7) {
                        level.setBlock(pos, stoveEntity.getBlockState().setValue(COOKING_TYPE, 1), 2);
                    } else {
                        level.setBlock(pos, stoveEntity.getBlockState().setValue(COOKING_TYPE, value + 1), 2);
                    }
                }
            }
        });

        supplier.get().setPacketHandled(true);
    }
}
