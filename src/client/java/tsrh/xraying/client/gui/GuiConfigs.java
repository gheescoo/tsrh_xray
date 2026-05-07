package tsrh.xraying.client.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.IConfigGuiAllTab;
import fi.dy.masa.malilib.util.StringUtils;
import tsrh.xraying.client.Reference;
import tsrh.xraying.client.config.Configs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static tsrh.xraying.client.Reference.MOD_ID;

public class GuiConfigs extends GuiConfigsBase implements IConfigGuiAllTab {
    public GuiConfigs() {
        super(10, 50, MOD_ID, null, "xraying.gui.title.configs", String.format("%s", Reference.MOD_VERSION));
    }

    public static ConfigGuiTab tab = ConfigGuiTab.GENERIC;

    @Override
    public void initGui()
    {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;
        int rows = 1;

        for (ConfigGuiTab tab : ConfigGuiTab.values())
        {
            if (!this.useAllTab() && tab == ConfigGuiTab.ALL) continue;
            int width = this.getStringWidth(tab.getDisplayName()) + 10;

            if (x >= this.getScreenWidth() - width - 10)
            {
                x = 10;
                y += 22;
                rows++;
            }

            x += this.createButton(x, y, width, tab);
        }

        if (rows > 1)
        {
            int scrollbarPosition = Objects.requireNonNull(this.getListWidget()).getScrollbar().getValue();
            this.setListPosition(this.getListX(), 50 + (rows - 1) * 22);
            this.reCreateListWidget();
            this.getListWidget().getScrollbar().setValue(scrollbarPosition);
            this.getListWidget().refreshEntries();
        }
    }

    private int createButton(int x, int y, int width, ConfigGuiTab tab)
    {
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
        button.setEnabled(GuiConfigs.tab != tab);
        this.addButton(button, new ButtonListenerConfigTabs(tab, this));

        return button.getWidth() + 2;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs()
    {
        ConfigGuiTab tab = GuiConfigs.tab;

        if (tab == ConfigGuiTab.ALL && this.useAllTab())
        {
            return this.getAllConfigs();
        }
        else if (tab == ConfigGuiTab.GENERIC)
        {
            return ConfigOptionWrapper.createFor(Configs.Generic.OPTIONS);
        }
        else if (tab == ConfigGuiTab.BLOCK_ENTITIES)
        {
            return ConfigOptionWrapper.createFor(Configs.BlockEntities.OPTIONS);
        }
        else if (tab == ConfigGuiTab.HOTKEYS)
        {
            List<IConfigBase> list = new ArrayList<>();

            list.addAll(Configs.Generic.HOTKEY_LIST);
            list.addAll(Configs.Profiles.HOTKEY_LIST);

            return ConfigOptionWrapper.createFor(list);
        }
        else if (tab == ConfigGuiTab.PROFILES)
        {
            return ConfigOptionWrapper.createFor(Configs.Profiles.OPTIONS);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean useAllTab() {
        return true;
    }

    @Override
    public List<ConfigOptionWrapper> getAllConfigs()
    {

        List<ConfigOptionWrapper> configs = new ArrayList<>(ConfigOptionWrapper.createFor(Configs.Generic.OPTIONS));

        List<IConfigBase> list = new ArrayList<>();

        list.addAll(Configs.Generic.HOTKEY_LIST);
        list.addAll(Configs.Profiles.HOTKEY_LIST);
//        list.addAll(Configs.BlockEntities.HOTKEY_LIST);
//        list.addAll(Configs.Profiles.OPTIONS);

        // Info Lines
//        list.addAll(INFO_LINE_LIST.stream().map(this::wrapConfig).toList());
//        list.addAll(ConfigUtils.createConfigWrapperForType(ConfigType.INTEGER, INFO_LINE_LIST));
        configs.addAll(ConfigOptionWrapper.createFor(list));

        return configs;
    }

//    protected BooleanHotkeyGuiWrapper wrapConfig(InfoToggle config)
//    {
//        return new BooleanHotkeyGuiWrapper(config.getName(), config, config.getKeybind());
//    }
//
    private record ButtonListenerConfigTabs(ConfigGuiTab tab, GuiConfigs parent) implements IButtonActionListener
    {
        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            GuiConfigs.tab = this.tab;

            this.parent.reCreateListWidget(); // apply the new config width
            if (this.parent.getListWidget() != null)
            {
                this.parent.getListWidget().resetScrollbarPosition();
            }
            this.parent.initGui();
        }
    }
    public enum ConfigGuiTab
    {
        ALL                 (IConfigGuiAllTab.getTranslationKey()),
        GENERIC             ("xraying.gui.button.config_gui.generic"),
        BLOCK_ENTITIES      ("xraying.gui.button.config_gui.blockEntities"),
        HOTKEYS             ("xraying.gui.button.config_gui.hotkeys"),
        PROFILES            ("xraying.gui.button.config_gui.profiles");

        private final String translationKey;

        ConfigGuiTab(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public String getDisplayName()
        {
            return StringUtils.translate(this.translationKey);
        }
    }
}