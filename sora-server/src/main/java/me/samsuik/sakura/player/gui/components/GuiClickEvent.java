package me.samsuik.sakura.player.gui.components;

import me.samsuik.sakura.player.gui.FeatureGuiInventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GuiClickEvent {
    void doSomething(InventoryClickEvent event, FeatureGuiInventory inventory);
}
