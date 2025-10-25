package ru.knifeswx.xdplanethelper.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestModListPacket() implements CustomPayload {
    public static final CustomPayload.Id<RequestModListPacket> ID = new CustomPayload.Id<>(Identifier.of("xdplanet_helper", "request_modlist"));
    public static final PacketCodec<RegistryByteBuf, RequestModListPacket> CODEC = PacketCodec.unit(new RequestModListPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void register() {
        PayloadTypeRegistry.playS2C().register(ID, CODEC);
    }
}
