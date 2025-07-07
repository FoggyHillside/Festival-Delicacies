package cn.foggyhillside.festival_delicacies.network.packet;

import cn.foggyhillside.festival_delicacies.blocks.entities.FermentationJarEntity;
import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import cn.foggyhillside.festival_delicacies.registry.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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
                if (level.getBlockEntity(pos) instanceof StoveEntity entity) {
                    switch (entity.getBlockState().getValue(ModBlockStateProperties.RED_STONE)) {
                        case 0 ->
                                level.setBlock(pos, entity.getBlockState().setValue(ModBlockStateProperties.RED_STONE, 1), 2);
                        case 1 ->
                                level.setBlock(pos, entity.getBlockState().setValue(ModBlockStateProperties.RED_STONE, 0), 2);
                    }
                } else if (level.getBlockEntity(pos) instanceof FermentationJarEntity entity) {
                    switch (entity.getBlockState().getValue(ModBlockStateProperties.RED_STONE)) {
                        case 0 ->
                                level.setBlock(pos, entity.getBlockState().setValue(ModBlockStateProperties.RED_STONE, 1), 2);
                        case 1 ->
                                level.setBlock(pos, entity.getBlockState().setValue(ModBlockStateProperties.RED_STONE, 0), 2);
                    }
                }
            }
        });

        supplier.get().setPacketHandled(true);
    }
}
