package swx.xdplanet_helper.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record ModlistPacket(List<ModInfo> mods) implements CustomPayload {
    public static final CustomPayload.Id<ModlistPacket> ID = new CustomPayload.Id<>(Identifier.of("xdplanet_helper", "modlist"));
    public static final PacketCodec<RegistryByteBuf, ModlistPacket> CODEC = PacketCodec.tuple(
            ModInfo.CODEC.collect(PacketCodecs.toList()), ModlistPacket::mods,
            ModlistPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(ID, CODEC);
    }
}
