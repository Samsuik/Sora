package me.samsuik.sakura.command.subcommands;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import me.samsuik.sakura.command.BaseSubCommand;
import me.samsuik.sakura.tps.ServerTickInformation;
import me.samsuik.sakura.tps.graph.BuiltComponentCanvas;
import me.samsuik.sakura.tps.graph.DetailedTPSGraph;
import me.samsuik.sakura.tps.graph.GraphComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TPSCommand extends BaseSubCommand {
    private static final int GRAPH_WIDTH = 71;
    private static final int GRAPH_HEIGHT = 10;
    private static final Style GRAY_WITH_STRIKETHROUGH = Style.style(NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH);

    public TPSCommand(String name) {
        super(name);
        this.description = "Displays the current ticks per second";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ServerTickInformation tickInformation = MinecraftServer.getServer().latestTickInformation();
        long identifier = this.parseLong(args, 1).orElse(tickInformation.identifier());
        double scale = this.parseDouble(args, 0).orElse(-1.0);
        if (scale < 0.0) {
            scale = this.dynamicScale(identifier);
        }

        ImmutableList<ServerTickInformation> tickHistory = MinecraftServer.getServer().tickHistory(identifier - GRAPH_WIDTH, identifier);
        DetailedTPSGraph graph = new DetailedTPSGraph(GRAPH_WIDTH, GRAPH_HEIGHT, scale, tickHistory);
        BuiltComponentCanvas canvas = graph.plot();
        canvas.appendLeft(Component.text(":", NamedTextColor.BLACK));
        canvas.appendRight(Component.text(":", NamedTextColor.BLACK));
        canvas.header(this.createHeaderComponent(tickInformation, identifier));
        canvas.footer(Component.text("*", NamedTextColor.DARK_GRAY)
            .append(Component.text(Strings.repeat(" ", GRAPH_WIDTH - 1), GRAY_WITH_STRIKETHROUGH))
            .append(Component.text("*")));

        for (Component component : canvas.components()) {
            sender.sendMessage(component);
        }
    }

    private double dynamicScale(long identifier) {
        ImmutableList<ServerTickInformation> tickHistory = MinecraftServer.getServer().tickHistory(identifier - 5, identifier);
        double averageTps = tickHistory.stream()
            .mapToDouble(ServerTickInformation::tps)
            .average()
            .orElse(0.0);
        return 20 / averageTps;
    }

    private Component createHeaderComponent(ServerTickInformation tickInformation, long identifier) {
        int scrollAmount = GRAPH_WIDTH / 3 * 2;
        double memoryUsage = memoryUsage();
        TextComponent.Builder builder = Component.text();
        builder.color(NamedTextColor.DARK_GRAY);
        builder.append(Component.text("< ")
            .clickEvent(ClickEvent.runCommand("/tps -1 " + (identifier + scrollAmount))));
        builder.append(Component.text(Strings.repeat(" ", 19), GRAY_WITH_STRIKETHROUGH));
        builder.append(Component.text(" ( "));
        builder.append(Component.text("Now: ", NamedTextColor.WHITE)
            .append(Component.text("%.1f".formatted(tickInformation.tps()), tickInformation.colour())));
        builder.appendSpace();
        builder.append(Component.text("Mem: ", NamedTextColor.WHITE)
            .append(Component.text("%.1f".formatted(memoryUsage * 100), GraphComponents.colour(1 - (float) memoryUsage))));
        builder.append(Component.text("% ) "));
        builder.append(Component.text(Strings.repeat(" ", 18), GRAY_WITH_STRIKETHROUGH));
        builder.append(Component.text(" >")
            .clickEvent(ClickEvent.runCommand("/tps -1 " + (identifier - scrollAmount))));
        return builder.build();
    }

    private static double memoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        double free  = runtime.freeMemory();
        double max   = runtime.maxMemory();
        double alloc = runtime.totalMemory();
        return (alloc - free) / max;
    }
}
