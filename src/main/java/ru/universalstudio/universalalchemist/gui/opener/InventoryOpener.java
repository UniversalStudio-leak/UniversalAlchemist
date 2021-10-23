package ru.universalstudio.universalalchemist.gui.opener;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;
import ru.universalstudio.universalalchemist.gui.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author original code this class: SmartInvs
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public interface InventoryOpener {

    Inventory open(SmartInventory inv, Player player);
    boolean supports(InventoryType type);

    default void fill(Inventory inventory, Map map) { // паравозик мастерили, мастерили
        for (Object entry : map.entrySet()) {
            Map.Entry entryMap = (Map.Entry) entry;
            if (entryMap.getValue() != null) {
                inventory.setItem(Integer.parseInt(entryMap.getKey().toString()), ((ClickableItem) entryMap.getValue()).getItem());
            }
        }
    }

}
