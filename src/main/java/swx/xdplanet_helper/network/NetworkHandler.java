package swx.xdplanet_helper.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NetworkHandler {
    
    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(RequestModListPacket.ID, (payload, context) -> {
            context.client().execute(() -> {
                List<ModInfo> modList = collectModInfo();
                ClientPlayNetworking.send(new ModlistPacket(modList));
            });
        });
    }

    private static List<ModInfo> collectModInfo() {
        List<ModInfo> mods = new ArrayList<>();
        
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            String modId = mod.getMetadata().getId();
            String version = mod.getMetadata().getVersion().getFriendlyString();
            long size = getModSize(mod);
            
            mods.add(new ModInfo(modId, version, size));
        }
        
        return mods;
    }

    private static long getModSize(ModContainer mod) {
        try {
            Path modPath = mod.getRootPaths().get(0);
            if (Files.isRegularFile(modPath)) {
                return Files.size(modPath);
            } else if (Files.isDirectory(modPath)) {
                return Files.walk(modPath)
                        .filter(Files::isRegularFile)
                        .mapToLong(p -> {
                            try {
                                return Files.size(p);
                            } catch (IOException e) {
                                return 0;
                            }
                        })
                        .sum();
            }
        } catch (IOException e) {
            return 0;
        }
        return 0;
    }
}
