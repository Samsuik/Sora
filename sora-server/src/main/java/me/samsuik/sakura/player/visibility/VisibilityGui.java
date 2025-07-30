package me.samsuik.sakura.player.visibility;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import me.samsuik.sakura.configuration.GlobalConfiguration;
import me.samsuik.sakura.player.gui.FeatureGui;
import me.samsuik.sakura.player.gui.FeatureGuiInventory;
import me.samsuik.sakura.player.gui.components.ItemButton;
import me.samsuik.sakura.player.gui.components.ItemSwitch;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;

import static me.samsuik.sakura.player.gui.ItemStackUtil.itemWithBlankName;

@NullMarked
public final class VisibilityGui extends FeatureGui {
    private static final NamespacedKey TOGGLE_BUTTON_KEY = new NamespacedKey("sakura", "toggle_button");
    private static final NamespacedKey MENU_ITEMS_KEY = new NamespacedKey("sakura", "menu_items");

    public VisibilityGui() {
        super(45, Component.text("FPS Settings"));
    }

    @Override
    protected void fillInventory(Inventory inventory) {
        for (int slot = 0; slot < inventory.getSize(); ++slot) {
            // x, y from top left of the inventory
            int x = slot % 9;
            int y = slot / 9;
            // from center
            int rx = x - 4;
            int ry = y - 2;
            double d = Math.sqrt(rx * rx + ry * ry);
            if (d <= 3.25) {
                inventory.setItem(slot, itemWithBlankName(GlobalConfiguration.get().fps.material));
            } else if (x % 8 == 0) {
                inventory.setItem(slot, itemWithBlankName(Material.BLACK_STAINED_GLASS_PANE));
            } else {
                inventory.setItem(slot, itemWithBlankName(Material.WHITE_STAINED_GLASS_PANE));
            }
        }
    }

    @Override
    protected void afterFill(Player player, FeatureGuiInventory inventory) {
        VisibilitySettings settings = player.getVisibility();
        IntArrayFIFOQueue slots = this.availableSlots();
        this.updateToggleButton(settings, player, inventory);
        for (VisibilityType type : VisibilityTypes.types()) {
            VisibilityState state = settings.get(type);
            int index = type.states().indexOf(state);
            int slot = slots.dequeueInt();

            ItemSwitch itemSwitch = new ItemSwitch(
                VisibilityGuiItems.GUI_ITEMS.get(type),
                slot, index,
                (e, inv) -> {
                    settings.cycle(type);
                    this.updateToggleButton(settings, player, inv);
                }
            );

            inventory.addComponent(itemSwitch, MENU_ITEMS_KEY);
        }
    }

    private void updateToggleButton(VisibilitySettings settings, Player player, FeatureGuiInventory inventory) {
        inventory.removeComponents(TOGGLE_BUTTON_KEY);
        VisibilityState settingsState = settings.currentState();
        ItemButton button = new ItemButton(
            VisibilityGuiItems.TOGGLE_BUTTON_ITEMS.get(settingsState),
            (2 * 9) + 8,
            (e, inv) -> {
                settings.toggleAll();
                inventory.removeAllComponents();
                this.afterFill(player, inv);
            }
        );
        inventory.addComponent(button, TOGGLE_BUTTON_KEY);
    }

    private IntArrayFIFOQueue availableSlots() {
        IntArrayFIFOQueue slots = new IntArrayFIFOQueue();
        for (int row = 1; row < 4; ++row) {
            for (int column = 3; column < 6; ++column) {
                if ((column + row) % 2 == 0) {
                    slots.enqueue((row * 9) + column);
                }
            }
        }
        return slots;
    }
}
