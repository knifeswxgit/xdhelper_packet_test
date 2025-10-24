package swx.xdplanet_helper;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swx.xdplanet_helper.network.ModlistPacket;
import swx.xdplanet_helper.network.RequestModListPacket;

public class XDPlanet_helper implements ModInitializer {
	public static final String MOD_ID = "xdplanet_helper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModlistPacket.register();
		RequestModListPacket.register();
		LOGGER.info("так 1-2-3 работаем");
	}
}