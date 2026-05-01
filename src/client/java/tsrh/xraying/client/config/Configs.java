package tsrh.xraying.client.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.MinecraftClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.google.common.collect.ImmutableList;
import tsrh.xraying.client.Reference;
import tsrh.xraying.client.Xray;
import tsrh.xraying.client.XrayETL;

import static tsrh.xraying.client.Reference.MOD_ID;
import static tsrh.xraying.client.Xray.mc;

public class Configs implements IConfigHandler {
    @Override
    public void load() {
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(MOD_ID + ".json");

        if (Files.exists(configFile) && Files.isReadable(configFile)) {
            JsonElement element = JsonUtils.parseJsonFileAsPath(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                // "Visuals" acts as the category header in the JSON file
                ConfigUtils.readConfigBase(root, "Generic", java.util.List.of(Generic.XRAY_ALPHA));
                ConfigUtils.readConfigBase(root, "Generic", java.util.List.of(Generic.XRAY));
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
            // Write your options under the "Visuals" category
            ConfigUtils.writeConfigBase(root, "Generic", java.util.List.of(Generic.XRAY_ALPHA));
            ConfigUtils.writeConfigBase(root, "Generic", java.util.List.of(Generic.XRAY));

            JsonUtils.writeJsonToFileAsPath(root, dir.resolve(MOD_ID + ".json"));
        }
    }

    private static final String GENERIC_KEY = Reference.MOD_ID+".config.generic";
    public static class Generic {
        public static final ConfigBoolean XRAY          = new ConfigBoolean("xray", true).apply(GENERIC_KEY);
        public static final ConfigInteger XRAY_ALPHA    = new ConfigInteger("xrayAlpha", 64, 0, 255).apply(GENERIC_KEY);

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                XRAY,
                XRAY_ALPHA
        );

        static {
            XRAY.setValueChangeCallback((config) -> {;
                if (MinecraftClient.getInstance().worldRenderer != null) {
                    XrayETL.isXrayActive = XRAY.getBooleanValue();
                    mc.worldRenderer.reload();
                }
            });
            // Callback to reload chunks immediately when the slider is moved
            XRAY_ALPHA.setValueChangeCallback((config) -> {
                if (MinecraftClient.getInstance().worldRenderer != null) {
                    XrayETL.alphaXray = XRAY_ALPHA.getIntegerValue();
                    mc.worldRenderer.reload();
                }
            });
        }
    }
}