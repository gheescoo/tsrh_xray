/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package tsrh.xraying.client;

import net.fabricmc.loader.api.FabricLoader;
import org.jspecify.annotations.NonNull;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    private static final String mixinPackage = "tsrh.xraying.client.mixins";

    private static boolean loaded;

    static boolean isIndigoPresent;
    static boolean isSodiumPresent;
    static boolean isLithiumPresent;

    @Override
    public void onLoad(String mixinPackage) {
        if (loaded) return;

        try {
            Field mixinTransformerField = getField();
            mixinTransformerField.setAccessible(true);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            Xray.LOGGER.error("Error loading the mixin plugin", e);
        }

        isIndigoPresent = FabricLoader.getInstance().isModLoaded("fabric-renderer-indigo");
        isSodiumPresent = FabricLoader.getInstance().isModLoaded("sodium");
        isLithiumPresent = FabricLoader.getInstance().isModLoaded("lithium");

        loaded = true;
    }

    private static @NonNull Field getField() throws NoSuchFieldException, IllegalAccessException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> classLoaderClass = classLoader.getClass();

        // Get delegate
        Field delegateField = classLoaderClass.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        Object delegate = delegateField.get(classLoader);
        Class<?> delegateClass = delegate.getClass();

        // Get mixinTransformer field
        return delegateClass.getDeclaredField("mixinTransformer");
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith(mixinPackage)) {
            throw new RuntimeException("Mixin " + mixinClassName + " is not in the mixin package");
        }
        else if (mixinClassName.startsWith(mixinPackage + ".sodium")) {
            return isSodiumPresent;
        }
        else if (mixinClassName.startsWith(mixinPackage + ".indigo")) {
            return isIndigoPresent;
        }
        else if (mixinClassName.startsWith(mixinPackage + ".lithium")) {
            return isLithiumPresent;
        }


        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
