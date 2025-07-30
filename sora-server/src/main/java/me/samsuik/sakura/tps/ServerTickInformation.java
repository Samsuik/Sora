package me.samsuik.sakura.tps;

import me.samsuik.sakura.configuration.GlobalConfiguration;
import me.samsuik.sakura.tps.graph.GraphComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ServerTickInformation(long identifier, double tps, double averageTick, long longestTick, float targetTickRate, int chunks, int entities) {
    public static final ServerTickInformation FILLER = new ServerTickInformation(0, 0.0, 0.0, 0, 0.0f, 0, 0);

    public TextColor colour() {
        float lag = (float) this.tps / this.targetTickRate;
        return GraphComponents.colour(lag);
    }

    public Component hoverComponent(TextColor colour) {
        TextComponent.Builder builder = Component.text();
        builder.append(Component.text("TPS: ")
            .append(Component.text("%.1f".formatted(this.tps), colour)));
        builder.appendNewline();
        builder.append(Component.text("MSPT: ")
            .append(Component.text("%.1f".formatted(this.averageTick), colour))
            .append(Component.text("/"))
            .append(Component.text(this.longestTick, colour)));
        if (GlobalConfiguration.get().messages.tpsShowEntityAndChunkCount) {
            builder.appendNewline();
            builder.append(Component.text("Entities: " + this.entities));
            builder.appendNewline();
            builder.append(Component.text("Chunks: " + this.chunks));
        }
        return builder.build();
    }
}
