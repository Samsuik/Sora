package me.samsuik.sakura.tps.graph;

import com.google.common.base.Preconditions;
import me.samsuik.sakura.tps.ServerTickInformation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public abstract class TPSGraph {
    protected final List<ServerTickInformation> tickInformation;
    protected final int width;
    protected final int height;
    protected final double scale;

    public TPSGraph(int width, int height, double scale, List<ServerTickInformation> tickInformation) {
        Preconditions.checkArgument(tickInformation.size() == width);
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.tickInformation = tickInformation;
    }

    public abstract BuiltComponentCanvas plot();

    protected final int rowFromColumn(int x) {
        int clamped = Math.clamp(x, 0, this.width - 1);
        ServerTickInformation tickInformation = this.tickInformation.get(clamped);
        return this.rowFromTPS(tickInformation.tps());
    }

    protected final int rowFromTPS(double tps) {
        int row = Mth.floor((tps / 3) * this.scale);
        return Mth.clamp(row, 0, this.height - 1);
    }

    protected final void addColourAndHoverInformation(ComponentCanvas canvas) {
        for (int x = 0; x < this.width; ++x) {
            ServerTickInformation tickInformation = this.tickInformation.get(x);
            TextColor colourFromTPS = tickInformation.colour();
            Component hoverComponent = tickInformation.hoverComponent(colourFromTPS);
            HoverEvent<Component> hoverEvent = HoverEvent.showText(hoverComponent);

            for (int y = 0; y < this.height; ++y) {
                Component component = canvas.get(x, y);
                if (component == GraphComponents.BACKGROUND) {
                    component = component.color(NamedTextColor.BLACK);
                } else {
                    component = component.color(colourFromTPS);
                }
                canvas.set(x, y, component.hoverEvent(hoverEvent));
            }
        }
    }
}
