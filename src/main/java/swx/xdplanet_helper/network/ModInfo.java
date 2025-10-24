package swx.xdplanet_helper.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ModInfo(String modId, String version, long size) {
    public static final PacketCodec<RegistryByteBuf, ModInfo> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ModInfo::modId,
            PacketCodecs.STRING, ModInfo::version,
            PacketCodecs.VAR_LONG, ModInfo::size,
            ModInfo::new
    );
}
