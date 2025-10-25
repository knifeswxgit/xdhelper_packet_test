package ru.knifeswx.xdplanethelper;

import net.fabricmc.api.ClientModInitializer;
import ru.knifeswx.xdplanethelper.network.NetworkHandler;

public class XDPlanetHelperClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetworkHandler.registerClientReceivers();
        XDPlanetHelper.LOGGER.info("Client receivers initialized");
    }
}
