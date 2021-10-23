package ru.universalstudio.universalalchemist.gui.opener;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import com.google.common.base.*;
import org.bukkit.event.inventory.*;
import ru.universalstudio.universalalchemist.gui.*;
import ru.universalstudio.universalalchemist.gui.content.InventoryContents;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author original code this class: SmartInvs
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public class ChestInventoryOpener implements InventoryOpener {

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        Preconditions.checkArgument(inv.getColumns() == 9,
                "The column count for the chest inventory must be 9, found: %s.", inv.getColumns());
        Preconditions.checkArgument(inv.getRows() >= 1 && inv.getRows() <= 6,
                "The row count for the chest inventory must be between 1 and 6, found: %s", inv.getRows());

        InventoryManager manager = inv.getManager();
        Inventory handle = Bukkit.createInventory(player, inv.getRows() * inv.getColumns(), inv.getTitle());

        this.fill(handle, ((InventoryContents)manager.getContents(player).get()).contentsAll());

        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST;
    }

}
