package tsrh.xraying.client;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static tsrh.xraying.client.Reference.MOD_ID;

public class Xray implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient() {
		InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
//		GuiContext.getGuiRegistry().registerGuiProvider(MOD_ID, () -> new GuiConfigs());
	}
}