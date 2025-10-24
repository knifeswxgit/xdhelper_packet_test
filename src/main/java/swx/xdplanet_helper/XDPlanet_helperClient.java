package swx.xdplanet_helper;

import net.fabricmc.api.ClientModInitializer;
import swx.xdplanet_helper.network.NetworkHandler;

public class XDPlanet_helperClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetworkHandler.registerClientReceivers();
        XDPlanet_helper.LOGGER.info("так 1-2-3 работаем");
    }
}
