package ru.knifeswx.xdplanethelper;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.knifeswx.xdplanethelper.network.EntrypointsPacket;
import ru.knifeswx.xdplanethelper.network.ModlistPacket;
import ru.knifeswx.xdplanethelper.network.RequestEntrypointsPacket;
import ru.knifeswx.xdplanethelper.network.RequestModListPacket;

public class XDPlanetHelper implements ModInitializer {
    public static final String MOD_ID = "xdplanet_helper";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModlistPacket.register();
        RequestModListPacket.register();
        EntrypointsPacket.register();
        RequestEntrypointsPacket.register();
        LOGGER.info("Packets registered");
    }
}
