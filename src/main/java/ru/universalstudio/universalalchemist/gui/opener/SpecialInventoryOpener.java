package ru.universalstudio.universalalchemist.gui.opener;

import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;
import com.google.common.collect.*;
import ru.universalstudio.universalalchemist.gui.*;
import ru.universalstudio.universalalchemist.gui.content.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author original code this class: SmartInvs
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public class SpecialInventoryOpener implements InventoryOpener {

    private static final List<InventoryType> SUPPORTED = ImmutableList.of(
            InventoryType.FURNACE,
            InventoryType.WORKBENCH,
            InventoryType.DISPENSER,
            InventoryType.DROPPER,
            InventoryType.ENCHANTING,
            InventoryType.BREWING,
            InventoryType.ANVIL,
            InventoryType.BEACON,
            InventoryType.HOPPER
    );

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        InventoryManager manager = inv.getManager();
        Inventory handle = Bukkit.createInventory(player, inv.getType(), inv.getTitle());
        this.fill(handle, ((InventoryContents)manager.getContents(player).get()).contentsAll());
        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return SUPPORTED.contains(type);
    }

}
