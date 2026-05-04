package tsrh.xraying.client.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.*;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.data.json.JsonUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.google.common.collect.ImmutableList;
import tsrh.xraying.client.Reference;
import tsrh.xraying.client.Xray;
import tsrh.xraying.client.XrayETL;
import tsrh.xraying.client.gui.GuiConfigs;

import static tsrh.xraying.client.Xray.mc;

public class Configs implements IConfigHandler {
    @Override
    public void load() {
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(Reference.MOD_ID + ".json");

        if (Files.exists(configFile) && Files.isReadable(configFile)) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
            } else {
                Xray.LOGGER.error("load(): Failed to parse config file '{}'", configFile.toAbsolutePath());
            }
        }
    }

    @Override
    public void save() {
        Path dir = FileUtils.getConfigDirectoryAsPath();

        if (!Files.exists(dir)) {
            FileUtils.createDirectoriesIfMissing(dir);
        }

        if (Files.isDirectory(dir)) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);

            JsonUtils.writeJsonToFile(root, dir.resolve(Reference.MOD_ID + ".json"));
        }
    }

    static private void reloadWR(){
        if (!Generic.MANUAL_RELOAD.getBooleanValue() && mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    static private void reloadWROnXray() { if(XrayETL.isXrayActive) reloadWR(); }

    /**
     * In {@link Generic}, Mixins read from {@link tsrh.xraying.client.XrayETL},
     */
    private static final String GENERIC_KEY = Reference.MOD_ID + ".config.generic";
    public static class Generic {
        public static final ConfigBooleanHotkeyed   XRAY            = new ConfigBooleanHotkeyed("xray", false, "COMMA", KeybindSettings.RELEASE_ALLOW_EXTRA).apply(GENERIC_KEY);
        public static final ConfigBoolean           MANUAL_RELOAD   = new ConfigBoolean("manualReload", false).apply(GENERIC_KEY);
        public static final ConfigHotkey            OPEN_CONFIG_GUI = new ConfigHotkey("openConfigGui", "W,C").apply(GENERIC_KEY);
        public static final ConfigInteger           XRAY_ALPHA      = new ConfigInteger("xrayAlpha", 255, 0, 255).apply(GENERIC_KEY);
        public static final ConfigInteger           OTHER_ALPHA     = new ConfigInteger("otherAlpha", 64, 0, 255).apply(GENERIC_KEY);

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                XRAY,
                MANUAL_RELOAD,
                OPEN_CONFIG_GUI,
                XRAY_ALPHA,
                OTHER_ALPHA
        );

        public static final List<IHotkey> HOTKEY_LIST = ImmutableList.of(
                XRAY,
                OPEN_CONFIG_GUI
        );

        static {
            XRAY.setValueChangeCallback((config) -> {
                XRAY.getKeybind().setCallback((action, key) -> {
                    XRAY.toggleBooleanValue();
                    return true;
                });
                XrayETL.isXrayActive = XRAY.getBooleanValue();
                reloadWR();
            });
            OPEN_CONFIG_GUI.getKeybind().setCallback((action, key) -> {
                GuiBase.openGui(new GuiConfigs());
                return true;
            });
            XRAY_ALPHA.setValueChangeCallback((config) -> {
                XrayETL.alphaWhitelist = config.getIntegerValue();
                reloadWROnXray();
            });
            OTHER_ALPHA.setValueChangeCallback((config) -> {
                XrayETL.alphaBlacklist = OTHER_ALPHA.getIntegerValue();
                reloadWROnXray();
            });
        }
    }

    /**
     * While in {@link BlockEntities}, Mixins read directly from this config
     */
    private static final String BLOCK_ENTITIES_KEY = Reference.MOD_ID + ".config.blockEntities";
    public static class BlockEntities {
        public static final ConfigInteger BANNER_ALPHA          = new ConfigInteger("bannerAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger BED_ALPHA             = new ConfigInteger("bedAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger BELL_ALPHA            = new ConfigInteger("bellAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger CHEST_ALPHA           = new ConfigInteger("chestAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger CONDUIT_ALPHA         = new ConfigInteger("conduitAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger DECORATE_POT_ALPHA    = new ConfigInteger("decpotAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger ENCHANTING_TABLE_ALPHA= new ConfigInteger("enctabAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger SIGN_ALPHA            = new ConfigInteger("signAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger HSIGN_ALPHA           = new ConfigInteger("hsignAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger SKULL_ALPHA           = new ConfigInteger("skullAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger STATUE_ALPHA          = new ConfigInteger("statueAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);
        public static final ConfigInteger SHULKER_ALPHA         = new ConfigInteger("shulkerAlpha", 64, 0, 255).apply(BLOCK_ENTITIES_KEY);

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                Generic.MANUAL_RELOAD,
                BANNER_ALPHA,
                BED_ALPHA,
                BELL_ALPHA,
                CHEST_ALPHA,
                CONDUIT_ALPHA,
                DECORATE_POT_ALPHA,
                ENCHANTING_TABLE_ALPHA,
                SIGN_ALPHA,
                HSIGN_ALPHA,
                SKULL_ALPHA,
                STATUE_ALPHA,
                SHULKER_ALPHA
        );

        public static final List<IHotkey> HOTKEY_LIST = ImmutableList.of(
        );

        static IValueChangeCallback<ConfigInteger> callback = config -> reloadWROnXray();

        static {
            BANNER_ALPHA.setValueChangeCallback(callback);
            BED_ALPHA.setValueChangeCallback(callback);
            BELL_ALPHA.setValueChangeCallback(callback);
            CHEST_ALPHA.setValueChangeCallback(callback);
            CONDUIT_ALPHA.setValueChangeCallback(callback);
            DECORATE_POT_ALPHA.setValueChangeCallback(callback);
        }
    }

    private static final String PROFILES_KEY = Reference.MOD_ID + ".config.profiles";
    public static class Profiles {
        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of();
    }
}