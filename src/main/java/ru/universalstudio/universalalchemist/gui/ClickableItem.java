package ru.universalstudio.universalalchemist.gui;

import java.util.*;
import java.util.function.*;
import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author original code this class: SmartInvs
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public class ClickableItem {

    private ItemStack item;
    private Consumer<InventoryClickEvent> action;
    private boolean visible = true;
    private UUID uuid = UUID.randomUUID(); // рандом клоун 228

    private ClickableItem(ItemStack item, Consumer<InventoryClickEvent> consumer) {
        this.item = item;
        this.action = consumer;
    }

    public static ClickableItem empty(ItemStack item) {
        return of(item, e -> {});
    }

    public static ClickableItem of(ItemStack item, Consumer<InventoryClickEvent> consumer) {
        return new ClickableItem(item, consumer);
    }

    public void run(InventoryClickEvent e) { action.accept(e); }

    public ItemStack getItem() { return item; }

    public void setAction(Consumer consumer) {
        this.action = consumer;
    }

    public Consumer getAction() {
        return this.action;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public boolean equalsItemStack(ItemStack itemStack) {
        ItemNBTWrapper itemNBTWrapper = new ItemNBTWrapper(itemStack);
        if (!itemNBTWrapper.hasKey("clickableitem")) {
            return false;
        }
        UUID uUID = UUID.fromString(itemNBTWrapper.getValue("clickableitem"));
        return uUID.equals(this.uuid);
    }

}
