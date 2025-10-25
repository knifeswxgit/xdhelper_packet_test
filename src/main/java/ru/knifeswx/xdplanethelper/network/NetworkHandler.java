package ru.knifeswx.xdplanethelper.network;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import ru.knifeswx.xdplanethelper.crypto.CryptoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NetworkHandler {
    
    private static final Gson GSON = new Gson();
    private static Map<String, CachedModData> modCache = new HashMap<>();
    
    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(RequestModListPacket.ID, (payload, context) -> {
            context.client().execute(() -> {
                List<ModInfo> modList = collectModInfo();
                String json = GSON.toJson(modList);
                byte[] jsonBytes = json.getBytes();
                byte[] encrypted = CryptoUtil.encrypt(jsonBytes);
                ClientPlayNetworking.send(new ModlistPacket(encrypted));
            });
        });
        
        ClientPlayNetworking.registerGlobalReceiver(RequestEntrypointsPacket.ID, (payload, context) -> {
            context.client().execute(() -> {
                List<EntrypointInfo> entrypointsList = collectEntrypoints();
                String json = GSON.toJson(entrypointsList);
                byte[] jsonBytes = json.getBytes();
                byte[] encrypted = CryptoUtil.encrypt(jsonBytes);
                ClientPlayNetworking.send(new EntrypointsPacket(encrypted));
            });
        });
    }

    private static List<ModInfo> collectModInfo() {
        List<ModInfo> mods = new ArrayList<>();
        
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            String modId = mod.getMetadata().getId();
            String version = mod.getMetadata().getVersion().getFriendlyString();
            
            CachedModData cached = getCachedModData(mod);
            
            mods.add(new ModInfo(modId, version, cached.size));
        }
        
        return mods;
    }
    
    private static List<EntrypointInfo> collectEntrypoints() {
        List<EntrypointInfo> entrypoints = new ArrayList<>();
        
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            String modId = mod.getMetadata().getId();
            CachedModData cached = getCachedModData(mod);
            
            entrypoints.add(new EntrypointInfo(modId, cached.entrypoints));
        }
        
        return entrypoints;
    }

    private static CachedModData getCachedModData(ModContainer mod) {
        Path modPath = mod.getRootPaths().get(0);
        
        try {
            long lastModified = Files.getLastModifiedTime(modPath).toMillis();
            String cacheKey = modPath.toString();
            
            CachedModData cached = modCache.get(cacheKey);
            if (cached != null && cached.lastModified == lastModified) {
                return cached;
            }
            
            long size = calculateSize(modPath);
            List<String> entrypoints = extractEntrypoints(modPath);
            
            CachedModData newCache = new CachedModData(size, entrypoints, lastModified);
            modCache.put(cacheKey, newCache);
            
            return newCache;
        } catch (IOException e) {
            return new CachedModData(0, List.of(), 0);
        }
    }

    private static long calculateSize(Path modPath) throws IOException {
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
        return 0;
    }

    private static List<String> extractEntrypoints(Path modPath) {
        List<String> entrypoints = new ArrayList<>();
        
        try {
            if (Files.isRegularFile(modPath)) {
                try (InputStream is = Files.newInputStream(modPath);
                     ZipInputStream zis = new ZipInputStream(is)) {
                    
                    ZipEntry entry;
                    while ((entry = zis.getNextEntry()) != null) {
                        if (entry.getName().equals("fabric.mod.json")) {
                            String content = new String(zis.readAllBytes());
                            entrypoints = parseFabricModJson(content);
                            break;
                        }
                    }
                }
            } else if (Files.isDirectory(modPath)) {
                Path fabricModJson = modPath.resolve("fabric.mod.json");
                if (Files.exists(fabricModJson)) {
                    String content = Files.readString(fabricModJson);
                    entrypoints = parseFabricModJson(content);
                }
            }
        } catch (IOException e) {
            return List.of();
        }
        
        return entrypoints;
    }

    @SuppressWarnings("unchecked")
    private static List<String> parseFabricModJson(String json) {
        List<String> result = new ArrayList<>();
        
        try {
            Map<String, Object> root = GSON.fromJson(json, Map.class);
            Map<String, Object> entrypoints = (Map<String, Object>) root.get("entrypoints");
            
            if (entrypoints != null) {
                for (Map.Entry<String, Object> entry : entrypoints.entrySet()) {
                    String type = entry.getKey();
                    Object value = entry.getValue();
                    
                    if (value instanceof List) {
                        List<Object> list = (List<Object>) value;
                        for (Object item : list) {
                            result.add(type + ": " + item.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            return List.of();
        }
        
        return result;
    }

    private static class CachedModData {
        final long size;
        final List<String> entrypoints;
        final long lastModified;
        
        CachedModData(long size, List<String> entrypoints, long lastModified) {
            this.size = size;
            this.entrypoints = entrypoints;
            this.lastModified = lastModified;
        }
    }
}
