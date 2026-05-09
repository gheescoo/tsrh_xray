package tsrh.xraying.client.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            GuiConfigs guiConfigs = new GuiConfigs();
            guiConfigs.setParent(screen);
            return guiConfigs;
        };
    }
}
