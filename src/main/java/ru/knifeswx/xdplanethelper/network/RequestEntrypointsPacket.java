package ru.knifeswx.xdplanethelper.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestEntrypointsPacket() implements CustomPayload {
    public static final CustomPayload.Id<RequestEntrypointsPacket> ID = new CustomPayload.Id<>(Identifier.of("xdplanet_helper", "request_entrypoints"));
    public static final PacketCodec<RegistryByteBuf, RequestEntrypointsPacket> CODEC = PacketCodec.unit(new RequestEntrypointsPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void register() {
        PayloadTypeRegistry.playS2C().register(ID, CODEC);
    }
}
