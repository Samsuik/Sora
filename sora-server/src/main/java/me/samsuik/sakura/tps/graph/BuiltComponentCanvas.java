package me.samsuik.sakura.tps.graph;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class BuiltComponentCanvas {
    private final List<Component> components;

    BuiltComponentCanvas(List<Component> components) {
        this.components = components;
    }

    public void appendLeft(Component component) {
        this.components.replaceAll(component::append);
    }

    public void appendRight(Component component) {
        this.components.replaceAll(row -> row.append(component));
    }

    public void header(Component component) {
        this.components.addFirst(component);
    }

    public void footer(Component component) {
        this.components.add(component);
    }

    public ImmutableList<Component> components() {
        return ImmutableList.copyOf(this.components);
    }
}
