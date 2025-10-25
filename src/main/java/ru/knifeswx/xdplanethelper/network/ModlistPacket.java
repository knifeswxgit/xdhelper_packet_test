package ru.knifeswx.xdplanethelper.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ModlistPacket(byte[] encryptedData) implements CustomPayload {
    public static final CustomPayload.Id<ModlistPacket> ID = new CustomPayload.Id<>(Identifier.of("xdplanet_helper", "modlist"));
    
    public static final PacketCodec<RegistryByteBuf, ModlistPacket> CODEC = new PacketCodec<>() {
        @Override
        public ModlistPacket decode(RegistryByteBuf buf) {
            int length = buf.readVarInt();
            byte[] data = new byte[length];
            buf.readBytes(data);
            return new ModlistPacket(data);
        }

        @Override
        public void encode(RegistryByteBuf buf, ModlistPacket packet) {
            buf.writeVarInt(packet.encryptedData.length);
            buf.writeBytes(packet.encryptedData);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(ID, CODEC);
    }
}
