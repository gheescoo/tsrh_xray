package tsrh.xraying.client.config.extended;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.IConfigStringList;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.data.ImmutableCopy;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConfigBlockStringList extends ConfigBase<ConfigBlockStringList> implements IConfigStringList {
    public static final Codec<ConfigBlockStringList> CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
                    PrimitiveCodec.STRING.fieldOf("name").forGetter(ConfigBase::getName),
                    Codecs.listOrSingle(PrimitiveCodec.STRING).fieldOf("defaultValue").forGetter(get -> get.defaultValue.stream().toList()),
                    Codecs.listOrSingle(PrimitiveCodec.STRING).fieldOf("values").forGetter(get -> get.blockIDs),
                    PrimitiveCodec.STRING.fieldOf("comment").forGetter(get -> get.comment),
                    PrimitiveCodec.STRING.fieldOf("prettyName").forGetter(get -> get.prettyName),
                    PrimitiveCodec.STRING.fieldOf("translatedName").forGetter(get -> get.translatedName)
            ).apply(inst, ConfigBlockStringList::new)
    );
    private final ImmutableList<@NotNull String> defaultValue;
    private final List<String> blockIDs = new ArrayList<>();
    private final List<String> lastBlockIDs = new ArrayList<>();

    public ConfigBlockStringList(String name, ImmutableList<@NotNull String> defaultValue)
    {
        this(name, defaultValue, name+" Comment?", StringUtils.splitCamelCase(name), name);
    }

    public ConfigBlockStringList(String name, ImmutableList<@NotNull String> defaultValue, String comment)
    {
        this(name, defaultValue, comment, StringUtils.splitCamelCase(name), name);
    }

    public ConfigBlockStringList(String name, ImmutableList<@NotNull String> defaultValue, String comment, String prettyName)
    {
        this(name, defaultValue, comment, prettyName, name);
    }

    public ConfigBlockStringList(String name, ImmutableList<@NotNull String> defaultValue, String comment, String prettyName, String translatedName)
    {
        super(ConfigType.STRING_LIST, name, comment, prettyName, translatedName);

        this.defaultValue = defaultValue;
        this.blockIDs.addAll(defaultValue);
        this.updateLastStringListValue();
    }

    private ConfigBlockStringList(String name, List<String> defaultValue, List<String> values, String comment, String prettyName, String translatedName)
    {
        this(name, ImmutableList.copyOf(defaultValue), comment, prettyName, translatedName);
        this.blockIDs.addAll(values);
    }

    @Override
    public List<String> getStrings()
    {
        return this.blockIDs;
    }

    @Override
    public ImmutableList<@NotNull String> getDefaultStrings()
    {
        return this.defaultValue;
    }

    @Override
    public void setStrings(List<String> strings)
    {
        if (this.blockIDs.equals(strings) == false)
        {
            this.updateLastStringListValue();
            this.blockIDs.clear();
            this.blockIDs.addAll(strings);
            this.setModified();
        }
    }

    @Override
    public void setModified()
    {
        this.markClean();
        this.onValueChanged();
    }

    @Override
    public void resetToDefault()
    {
        this.setStrings(this.defaultValue);
    }

    @Override
    public boolean isModified()
    {
        return !this.blockIDs.equals(this.defaultValue);
    }

    @Override
    public List<String> getLastStringListValue()
    {
        return this.lastBlockIDs;
    }

    @Override
    public void updateLastStringListValue()
    {
        this.lastBlockIDs.clear();
        this.lastBlockIDs.addAll(ImmutableCopy.of(this.blockIDs).toList());
    }

    private void addString(String str)
    {
        this.blockIDs.add(str);
    }

    @Override
    public JsonElement getAsJsonElement()
    {
        JsonArray arr = new JsonArray();

        for (String str : this.blockIDs)
        {
            arr.add(new JsonPrimitive(str));
        }

        return arr;
    }

    @Override
    public void setValueFromJsonElement(JsonElement element)
    {
        ImmutableList<String> oldList = ImmutableCopy.of(this.blockIDs).toList();
        this.blockIDs.clear();

        try
        {
            if (element.isJsonArray())
            {
                JsonArray arr = element.getAsJsonArray();
                final int count = arr.size();

                for (int i = 0; i < count; ++i)
                {
                    String temp = arr.get(i).getAsString();

                    if (temp != null)
                    {
                        this.addString(temp);
                    }
                }

                if (!oldList.equals(this.blockIDs) || this.isDirty())
                {
                    this.markClean();

                    if (!this.getLastStringListValue().equals(this.getStrings()))
                    {
//                        MaLiLib.LOGGER.error("[STRING-LIST/{}]: setValueFromJsonElement(): LV: [{}], OV: [{}], NV: [{}]", this.getName(),
//                                             this.getLastStringListValue().size(),
//                                             oldList.size(),
//                                             this.getStrings().size()
//                        );

                        this.setModified();
                    }
                }
            }
            else
            {
                MaLiLib.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element);
            }
        }
        catch (Exception e)
        {
            MaLiLib.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }
    }
}
