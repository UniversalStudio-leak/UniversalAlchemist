package ru.universalstudio.universalalchemist.gui.content;

import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import ru.universalstudio.universalalchemist.gui.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author original code this class: SmartInvs
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public interface InventoryContents {

    SmartInventory inventory();
    Pagination pagination();

    Optional<SlotIterator> iterator(String id);

    SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn);
    SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn);

    SlotIterator newIterator(String id, SlotIterator.Type type, SlotPos startPos);
    SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos);

    Collection contents();
    Map contentsAll();

    Optional<ClickableItem> get(ItemStack itemStack);
    Optional<ClickableItem> get(int row, int column);

    InventoryContents set(int row, int column, ClickableItem item);
    InventoryContents set(SlotPos slotPos, ClickableItem item);

    InventoryContents add(ClickableItem item);

    InventoryContents fillBorders(ClickableItem item);
    InventoryContents fillRect(int fromRow, int fromColumn,
                               int toRow, int toColumn, ClickableItem item);
    InventoryContents fillRect(SlotPos fromPos, SlotPos toPos, ClickableItem item);

    <T> T property(String name);
    <T> T property(String name, T def);

    InventoryContents setTitle(String name);
    InventoryContents setProperty(String name, Object value);

    InventoryContents propertyClear();

    InventoryContents updateItem(int row, int column);
    InventoryContents updateItem(int row, int column, ItemStack item);

    int getSlot(int row, int column);

    class Impl implements InventoryContents {

        private SmartInventory inv;
        private Player player;

        private Map contents = new HashMap();

        private Pagination pagination = new Pagination.Impl();
        private Map<String, SlotIterator> iterators = new HashMap<>();
        private Map<String, Object> properties = new HashMap<>();

        public Impl(SmartInventory inv, Player player) {
            this.inv = inv;
            this.player = player;
        }

        @Override
        public SmartInventory inventory() { return inv; }

        @Override
        public Pagination pagination() { return pagination; }

        @Override
        public Optional<SlotIterator> iterator(String id) {
            return Optional.ofNullable(this.iterators.get(id));
        }

        @Override
        public SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn) {
            SlotIterator iterator = new SlotIterator.Impl(this, inv,
                    type, startRow, startColumn);

            this.iterators.put(id, iterator);
            return iterator;
        }

        @Override
        public SlotIterator newIterator(String id, SlotIterator.Type type, SlotPos startPos) {
            return newIterator(id, type, startPos.getRow(), startPos.getColumn());
        }

        @Override
        public SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn) {
            return new SlotIterator.Impl(this, inv, type, startRow, startColumn);
        }

        @Override
        public SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos) {
            return newIterator(type, startPos.getRow(), startPos.getColumn());
        }

        @Override
        public Collection contents() {
            return this.contents.values();
        }

        @Override
        public Map contentsAll() {
            return this.contents;
        }

        @Override
        public Optional<ClickableItem> get(ItemStack itemStack) {
            return Optional.ofNullable((ClickableItem)this.contents().stream().filter(item -> item.equals(itemStack)).findAny().orElse(null));
        }

        @Override
        public Optional<ClickableItem> get(int row, int column) {
            return Optional.ofNullable((ClickableItem)this.contents.get(this.getSlot(row, column)));
        }

        @Override
        public InventoryContents set(int row, int column, ClickableItem item) {
            update(row, column, item != null ? item.getItem() : null);
            return this;
        }

        @Override
        public InventoryContents set(SlotPos slotPos, ClickableItem item) {
            return set(slotPos.getRow(), slotPos.getColumn(), item);
        }

        @Override
        public InventoryContents add(ClickableItem item) {
            for (int i = 0; i < this.contents.size(); ++i) {
                if (this.contents.get(i) != null) continue;
                this.contents.put(i, item);
                return this;
            }
            this.contents.put((this.contents.size() + 1), item);
            return this;
        }

        @Override
        public InventoryContents fillBorders(ClickableItem item) {
            fillRect(0, 0, inv.getRows() - 1, inv.getColumns() - 1, item);
            return this;
        }

        @Override
        public InventoryContents fillRect(int fromRow, int fromColumn, int toRow, int toColumn, ClickableItem item) {
            for(int row = fromRow; row <= toRow; row++) {
                for(int column = fromColumn; column <= toColumn; column++) {
                    if(row != fromRow && row != toRow && column != fromColumn && column != toColumn)
                        continue;

                    set(row, column, item);
                }
            }

            return this;
        }

        @Override
        public InventoryContents fillRect(SlotPos fromPos, SlotPos toPos, ClickableItem item) {
            return fillRect(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T property(String name) {
            return (T) properties.get(name);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T property(String name, T def) {
            return properties.containsKey(name) ? (T) properties.get(name) : def;
        }

        @Override
        public InventoryContents propertyClear() {
            this.properties.clear();
            return this;
        }

        @Override
        public InventoryContents setProperty(String name, Object value) {
            properties.put(name, value);
            return this;
        }

        @Override
        public InventoryContents setTitle(String name) {
            this.inv.builder().title(name);
            return null;
        }

        @Override
        public InventoryContents updateItem(int row, int column) {
            this.updateItem(row, column, ((ClickableItem)this.contents.get(this.getSlot(row, column))).getItem());
            return this;
        }

        @Override
        public InventoryContents updateItem(int row, int column, ItemStack itemStack) {
            if (!this.inv.getManager().getOpenedPlayers(this.inv).contains(this.player)) {
                return this;
            }
            Inventory inventory = this.player.getOpenInventory().getTopInventory();
            inventory.setItem(this.inv.getColumns() * row + column, itemStack);
            return this;
        }

        @Override
        public int getSlot(int row, int column) {
            return row * 9 + column;
        }

        private void update(int row, int column, ItemStack item) {
            Player currentPlayer = Bukkit.getPlayer(player.getUniqueId());
            if(!inv.getManager().getOpenedPlayers(inv).contains(currentPlayer))
                return;

            Inventory topInventory = currentPlayer.getOpenInventory().getTopInventory();
            topInventory.setItem(inv.getColumns() * row + column, item);
        }

    }

}