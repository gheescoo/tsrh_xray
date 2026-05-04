package tsrh.xraying.client;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.*;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import tsrh.xraying.client.config.Configs;
//import tsrh.xraying.client.data.DebugDataManager;
//import tsrh.xraying.client.data.EntitiesDataManager;
//import tsrh.xraying.client.data.HudDataManager;
//import tsrh.xraying.client.event.*;
import tsrh.xraying.client.event.InputHandler;
import tsrh.xraying.client.gui.GuiConfigs;
//import tsrh.xraying.client.hotkeys.KeyCallbacks;
//import tsrh.xraying.client.renderer.OverlayRendererVillagerInfo;
//import tsrh.xraying.client.renderer.worker.WorkerDaemonHandler;
//import tsrh.xraying.client.util.DataStorage;

public class InitHandler implements IInitializationHandler
{
	@Override
	public void registerModHandlers()
	{
		ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());
		Registry.CONFIG_SCREEN.registerConfigScreenFactory(
				new ModInfo(Reference.MOD_ID, Reference.MOD_NAME, GuiConfigs::new)
		);

//		DataStorage.getInstance().onGameInit();
//		HudDataManager.getInstance().onGameInit();
//		EntitiesDataManager.getInstance().onGameInit();
//		DebugDataManager.getInstance().onGameInit();
//
		InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
		InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());
//
//		RenderHandler renderer = RenderHandler.getInstance();
//		RenderEventHandler.getInstance().registerInGameGuiRenderer(renderer);
//		RenderEventHandler.getInstance().registerTooltipLastRenderer(renderer);
////        RenderEventHandler.getInstance().registerWorldPreWeatherRenderer(renderer);
//		RenderEventHandler.getInstance().registerWorldLastRenderer(renderer);
//
//		WorldLoadListener listener = new WorldLoadListener();
//		WorldLoadHandler.getInstance().registerWorldLoadPreHandler(listener);
//		WorldLoadHandler.getInstance().registerWorldLoadPostHandler(listener);
//
//		ServerListener serverListener = new ServerListener();
//		ServerHandler.getInstance().registerServerHandler(serverListener);
//
//		TickHandler.getInstance().registerClientTickHandler(new ClientTickHandler());
//		TickHandler.getInstance().registerClientTickHandler(EntitiesDataManager.getInstance());
//		TickHandler.getInstance().registerClientTickHandler(OverlayRendererVillagerInfo.INSTANCE);
//		TickHandler.getInstance().registerClientTickHandler(WorkerDaemonHandler.INSTANCE);
//
//		KeyCallbacks.init();
	}
}
