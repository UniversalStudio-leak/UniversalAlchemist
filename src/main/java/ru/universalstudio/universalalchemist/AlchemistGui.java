package ru.universalstudio.universalalchemist;

import java.util.*;
import java.util.stream.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import ru.universalstudio.universalalchemist.gui.*;
import ru.universalstudio.universalalchemist.utils.*;
import ru.universalstudio.universalalchemist.gui.content.*;
import ru.universalstudio.universalalchemist.gui.content.SlotIterator.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author default source code: WinLocker02
 */

public class AlchemistGui implements InventoryProvider {

    private SmartInventory smartInventory;
    private ItemStack seleceted;
    private ItemStack toMix;

    public AlchemistGui() {
        this(null);
    }

    public AlchemistGui(ItemStack selected) {
        this(selected, null);
    }

    public AlchemistGui(ItemStack selected, ItemStack toMix) {
        this.smartInventory = SmartInventory.builder().provider(this).title(Utils.color(Utils.getString("gui.title"))).size(5, 9).build();
        this.seleceted = selected;
        this.toMix = toMix;
    }

    public void open(Player player) {
        this.smartInventory.open(player);
    }

    public void open(Player player, int page) {
        this.smartInventory.open(player, page);
    }

    public void init(Player player, InventoryContents contents) {
        // здесь я хорошо уебался под просмотром анимешки, сидишь такой пишешь код, а рядом в 4K full hd идёт анимешка (https://i.imgur.com/2rizwiA.jpeg), только прикол в том что телек к компу по HDMI привязан и бл*ть NVIDIA с lightshot не может другой экран юзать если не попросят))
        ClickableItem clickableItem = ClickableItem.empty(ItemBuilder.loadItemBuilder(Utils.getConfig(), "gui.window").build());
        contents.fillBorders(clickableItem);
        ClickableItem clickableItem2 = ClickableItem.of(ItemBuilder.loadItemBuilder(Utils.getConfig(), "gui.exit").build(), inventoryClickEvent -> player.closeInventory());
        contents.set(4, 0, clickableItem2);
        contents.set(4, 8, clickableItem2);
        if (this.seleceted != null && this.toMix != null) {
            List list = Utils.getBoolean("mix-colors") ? Collections.singletonList(Optional.ofNullable(PotionUtils.mixColors(this.seleceted, this.toMix))) : Utils.getMixedColors();
            ItemStack itemStack = ItemBuilder.of(PotionUtils.mixPotions(this.seleceted, this.toMix, list))
                    .setDisplayName(Utils.getString("mixed-potion.title"))
                    .setLore(Utils.getStringList("mixed-potion.lore"))
                    .flags(Utils.getStringList("mixed-potion.flags"))
                    .build();
            contents.set(2, 4, ClickableItem.empty(itemStack));
            contents.set(2, 3, ClickableItem.of(ItemBuilder.loadItemBuilder(Utils.getConfig(), "gui.accept").build(), inventoryClickEvent -> {
                if (!AlchemistPlugin.getInstance().withdraw(player, Utils.getDouble("price"))) {
                    Utils.sendMessage(player, Utils.getMessage("not-balance"));
                } else {
                    Utils.removeFirstItem(player, this.seleceted);
                    Utils.removeFirstItem(player, this.toMix);
                    player.getInventory().addItem(itemStack);
                    player.updateInventory();
                    Utils.sendMessage(player, Utils.getMessage("accepted"));
                }
                player.closeInventory();
            }));
            contents.set(2, 5, ClickableItem.of(ItemBuilder.loadItemBuilder(Utils.getConfig(), "gui.decline").build(), inventoryClickEvent -> {
                AlchemistGui alchemistGui = new AlchemistGui(this.seleceted);
                alchemistGui.open(player);
            }));
        } else {
            Pagination pagination = contents.pagination().setItemsPerPage(21);
            List list = new ArrayList<>();
            ClickableItem clickableItem3;
            if (this.seleceted == null && this.toMix == null) {
                list = (List)Utils.getPotions(player).stream().map(itemStack -> {
                    initItem(player,  itemStack);
                    return itemStack;
                }).collect(Collectors.toList());
            } else if (this.seleceted != null && this.toMix == null) {
                contents.set(4, 4, ClickableItem.empty(this.seleceted));
                clickableItem3 = ClickableItem.of(ItemBuilder.loadItemBuilder(Utils.getConfig(), "gui.cancel").build(), inventoryClickEvent -> {
                    AlchemistGui alchemistGui = new AlchemistGui();
                    alchemistGui.open(player);
                });
                contents.set(4, 1, clickableItem3);
                contents.set(4, 7, clickableItem3);
                list = (List)Utils.getPotionsIfNotSame(player, this.seleceted).stream().filter(itemStack ->
                        ((ItemStack)itemStack).getType().equals(this.seleceted.getType())
                ).map(itemStack -> ClickableItem.of((ItemStack)itemStack, inventoryClickEvent -> {
                    AlchemistGui alchemistGui = new AlchemistGui(this.seleceted, (ItemStack) itemStack);
                    alchemistGui.open(player);
                })).collect(Collectors.toList());
            }

            pagination.setItems((ClickableItem[])(list.toArray(new ClickableItem[0])));
            pagination.addToIterator(contents.newIterator(Type.HORIZONTAL, 0, 0).allowOverride(false));
            clickableItem3 = ClickableItem.empty(ItemBuilder.loadItemBuilder(Utils.getConfig(), "gui.next")
                    .replaceLore("%page%", Integer.toString(pagination.getPage()))
                    .replaceLore("%maxpage%", Integer.toString(pagination.getPages()))
                    .build());
            ClickableItem clickableItem4 = ClickableItem.empty(ItemBuilder.loadItemBuilder(Utils.getConfig(), "gui.previous")
                    .replaceLore("%page%", Integer.toString(pagination.getPage()))
                    .replaceLore("%maxpage%", Integer.toString(pagination.getPages()))
                    .build());
            clickableItem3.setAction(inventoryClickEvent -> this.open(player, pagination.next().getPage()));
            clickableItem4.setAction(inventoryClickEvent -> this.open(player, pagination.previous().getPage()));
            if (!pagination.isFirst() && pagination.isLast()) {
                clickableItem3.setVisible(false);
                clickableItem4.setVisible(true);
            } else if (pagination.isFirst() && pagination.getPages() > 0) {
                clickableItem3.setVisible(true);
                clickableItem4.setVisible(false);
            } else if (!pagination.isFirst() && !pagination.isLast()) {
                clickableItem3.setVisible(true);
                clickableItem4.setVisible(true);
            } else {
                clickableItem3.setVisible(false);
                clickableItem4.setVisible(false);
            }

            contents.set(4, 6, clickableItem3.isVisible() ? clickableItem3 : clickableItem);
            contents.set(4, 2, clickableItem4.isVisible() ? clickableItem4 : clickableItem);
        }
    }

    public void initItem(Player player, Object item) {
        ItemStack itemStack = (ItemStack)item;
        ClickableItem.of(itemStack, inventoryClickEvent -> {
            AlchemistGui alchemistGui = new AlchemistGui(itemStack);
            alchemistGui.open(player);
        });
    }
}
