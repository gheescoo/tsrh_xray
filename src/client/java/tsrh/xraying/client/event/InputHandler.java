package tsrh.xraying.client.event;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IMouseInputHandler;
import tsrh.xraying.client.Reference;
import tsrh.xraying.client.config.Configs;

public class InputHandler implements IKeybindProvider, IMouseInputHandler
{
    private static final InputHandler INSTANCE = new InputHandler();

    private InputHandler()
    {
        super();
    }

    public static InputHandler getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager)
    {
        for (IHotkey hotkey : Configs.Generic.HOTKEY_LIST)
        {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
        for (IHotkey hotkey : Configs.Profiles.HOTKEY_LIST)
        {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager)
    {
        manager.addHotkeysForCategory(Reference.MOD_NAME, "minihud.hotkeys.category.generic_hotkeys", Configs.Generic.HOTKEY_LIST);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "minihud.hotkeys.category.profile_hotkeys", Configs.Profiles.HOTKEY_LIST);
    }
}
