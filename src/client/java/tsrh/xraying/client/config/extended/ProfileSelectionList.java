package tsrh.xraying.client.config.extended;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.StringIdentifiable;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum ProfileSelectionList implements IConfigOptionListEntry, StringIdentifiable
{
    P0  ("profile0",   "tsrheang_xray.label.name.profile_of_ore"),
    P1  ("profile1",   "tsrheang_xray.label.name.profile1"),
    P2  ("profile2",   "tsrheang_xray.label.name.profile2"),
    P3  ("profile2",   "tsrheang_xray.label.name.profile3"),
    P4  ("profile2",   "tsrheang_xray.label.name.profile4"),
    P5  ("profile3",   "tsrheang_xray.label.name.profile5");

//    public static final ImmutableList<@NotNull ProfileSelectionList> VALUES = ImmutableList.copyOf(values());

    private final String configString;
    private final String unlocName;

    ProfileSelectionList(String configString, String unlocName)
    {
        this.configString = configString;
        this.unlocName = unlocName;
    }

    @Override
    public String getStringValue()
    {
        return this.configString;
    }

    @Override
    public String getDisplayName()
    {
        return StringUtils.translate(this.unlocName);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward)
    {
        int id = this.ordinal();

        if (forward)
        {
            if (++id >= values().length)
            {
                id = 0;
            }
        }
        else
        {
            if (--id < 0)
            {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public ProfileSelectionList fromString(String name)
    {
        return fromStringStatic(name);
    }

    public static ProfileSelectionList fromStringStatic(String name)
    {
        for (ProfileSelectionList mode : ProfileSelectionList.values())
        {
            if (mode.configString.equalsIgnoreCase(name))
            {
                return mode;
            }
        }

        return ProfileSelectionList.P1;
    }

    @Override
    public String asString() {
        return this.name();
    }
}
