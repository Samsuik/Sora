package me.samsuik.sakura.configuration.transformation.global;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

public final class V2_ConvertIconToMaterial implements TransformAction {
    private static final int VERSION = 3; // targeted version is always ahead by one
    private static final NodePath PATH = NodePath.path("fps", "material");
    private static final V2_ConvertIconToMaterial INSTANCE = new V2_ConvertIconToMaterial();

    private V2_ConvertIconToMaterial() {}

    public static void apply(ConfigurationTransformation.VersionedBuilder builder) {
        builder.addVersion(VERSION, ConfigurationTransformation.builder().addAction(PATH, INSTANCE).build());
    }

    @Override
    public Object @Nullable [] visitPath(NodePath path, ConfigurationNode value) throws ConfigurateException {
        if (value.raw() instanceof String stringValue) {
            value.raw(stringValue.toUpperCase());
        }
        return null;
    }
}
