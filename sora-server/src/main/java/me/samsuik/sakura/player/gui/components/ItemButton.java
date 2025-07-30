package me.samsuik.sakura.player.gui.components;

import me.samsuik.sakura.player.gui.FeatureGuiInventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ItemButton implements GuiComponent {
    private final ItemStack bukkitItem;
    private final int slot;
    private final GuiClickEvent whenClicked;

    public ItemButton(ItemStack bukkitItem, int slot, GuiClickEvent whenClicked) {
        this.bukkitItem = bukkitItem;
        this.slot = slot;
        this.whenClicked = whenClicked;
    }

    @Override
    public boolean interaction(InventoryClickEvent event, FeatureGuiInventory featureInventory) {
        if (event.getSlot() == this.slot) {
            this.whenClicked.doSomething(event, featureInventory);
            return true;
        }
        return false;
    }

    @Override
    public void creation(Inventory inventory) {
        inventory.setItem(this.slot, this.bukkitItem);
    }
}
