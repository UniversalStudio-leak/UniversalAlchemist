package ru.universalstudio.universalalchemist.gui.content;

import org.bukkit.entity.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author original code this class: SmartInvs
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public interface InventoryProvider {

    void init(Player player, InventoryContents contents);
    default boolean refresh(Player player, InventoryContents contents) {
        return false;
    }
    @Deprecated
    default void update(Player player, InventoryContents contents) {}

}
