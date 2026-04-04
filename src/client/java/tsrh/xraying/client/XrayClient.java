package tsrh.xraying.client;

import net.fabricmc.api.ClientModInitializer;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;

public class XrayClient implements ClientModInitializer, IInitializationHandler {
	@Override public void onInitializeClient() {
		// InitializationHandler.getInstance().registerInitializationHandler(this);
		// x.init();
	}

	@Override public void registerModHandlers() {
		// LPCTools.init();
	}
}
