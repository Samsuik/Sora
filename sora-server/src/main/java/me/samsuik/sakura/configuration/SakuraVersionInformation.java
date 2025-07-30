package me.samsuik.sakura.configuration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public final class SakuraVersionInformation {
    private static final String VERSION_MESSAGE = """
                <dark_gray>.
                <dark_gray>| <white>This server is running <gradient:aqua:green>Sora</gradient>
                <dark_gray>| <white>Commit<dark_gray>: \\<<commit>> <gray>targeting </gray>(<green>MC</green>: <gray><version></gray>)
                <dark_gray>| <white>Github<dark_gray>: \\<<green><click:open_url:'https://github.com/Samsuik/Sora'>link</click></green>>
                <dark_gray>'""";

    public static void sendVersionToPlayer(final CommandSender sender) {
        sender.sendMessage(MiniMessage.miniMessage().deserialize(VERSION_MESSAGE,
            Placeholder.component("commit", gitCommit()),
            Placeholder.unparsed("version", Bukkit.getMinecraftVersion())
        ));
    }

    private static Component gitCommit() {
        return Component.text("hover", NamedTextColor.GREEN)
            .hoverEvent(HoverEvent.showText(Component.text(Bukkit.getGitInformation())));
    }
}
