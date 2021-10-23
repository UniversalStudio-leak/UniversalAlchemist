package ru.universalstudio.universalalchemist;

import java.util.*;
import org.bukkit.*;
import java.util.stream.*;
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.configuration.file.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public class ItemBuilder {

    public static ItemBuilder loadItemBuilder(FileConfiguration config, String path)
    {
        Material material = Material.valueOf(config.getString(path + ".type").toUpperCase());
        ItemBuilder builder = new ItemBuilder(material);

        String displayName = config.getString(path + ".title");
        List<String> lore = config.getStringList(path + ".lore");

        builder.setDurability((short) config.getInt(path + ".data"));

        if(displayName != null)
            builder.setDisplayName(displayName);

        if(lore != null)
            builder.setLore(lore);

        builder.setAmount(config.getInt(path + ".amount") > 0 ? config.getInt(path + ".amount") : 1);

        if(config.getString(path + ".enchants") != null) {
            for (String enchants : config.getStringList(path + ".enchants")) {
                String[] args = enchants.split(":");
                builder.enchant(Enchantment.getByName(args[0].toUpperCase()), Integer.parseInt(args[1]));
            }
        }


        for(String flags : config.getStringList(path + ".flags"))
            builder.flag(ItemFlag.valueOf(flags.toUpperCase()));

        if(config.getString(path + ".potion-color") != null) {
            try {
                Color color = (Color) Color.class.getField(config.getString(path + ".potion-color").toUpperCase()).get(null);

                builder.setPotionColor(color);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(config.getStringList(path + ".potion-effects") != null) {

            for(String s : config.getStringList(path + ".potion-effects")) {

                PotionEffectType type = PotionEffectType.getByName(s.split(":")[0].toUpperCase());
                int level = Integer.parseInt(s.split(":")[1]) - 1;
                int duration = Integer.parseInt(s.split(":")[2]) * 20;

                builder.addPotionEffect(new PotionEffect(type, duration, level));
            }
        }

        return builder;
    }

    private ItemStack item;

    public static ItemBuilder of(ItemStack item) {
        return new ItemBuilder(item);
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchantall(int level) {
        for (Enchantment enchantment : Enchantment.values()) {
            this.enchant(enchantment, level);
        }
        return this;
    }

    public ItemBuilder flags(List<String> flags) {
        if(flags == null) return this;

        for(String flag : flags) {
            flag(ItemFlag.valueOf(flag.toUpperCase()));
        }
        return this;
    }

    public ItemBuilder flag(ItemFlag flag) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(flag);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder flagall() {
        for (ItemFlag flag : ItemFlag.values()) {
            this.flag(flag);
        }
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        if(name == null) return this;

        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(color(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder replaceDisplayName(String replace, String to)
    {
        ItemMeta meta = this.item.getItemMeta();
        if(!meta.hasDisplayName()) return this;

        meta.setDisplayName(meta.getDisplayName().replace(replace, to));
        this.item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if(lore == null) return this;

        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(color(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder replaceLore(String replace, String to)
    {
        ItemMeta meta = this.item.getItemMeta();
        if(!meta.hasLore()) return this;

        List<String> lore = meta.getLore()
                .stream()
                .map(x -> x.replace(replace, to))
                .collect(Collectors.toList());

        meta.setLore(lore);
        this.item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder addLore(String line) {
        ItemMeta meta = this.item.getItemMeta();

        List<String> list;

        if (meta.hasLore())
            list = meta.getLore();
        else
            list = new ArrayList<String>();

        list.add(color(line));
        meta.setLore(list);
        this.item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder removeLore(int page) {
        ItemMeta meta = this.item.getItemMeta();

        List<String> list;

        if (!meta.hasLore())
            return this;
        list = meta.getLore();

        if (page > list.size())
            return this;

        list.remove(page);
        meta.setLore(color(list));
        this.item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setPotionColor(Color color) {
        if(!(this.item.getItemMeta() instanceof PotionMeta)) return this;

        PotionMeta meta = (PotionMeta) this.item.getItemMeta();
        meta.setColor(color);
        this.item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffect effect) {
        if(!(this.item.getItemMeta() instanceof PotionMeta)) return this;

        PotionMeta meta = (PotionMeta) this.item.getItemMeta();
        meta.addCustomEffect(effect, true);
        this.item.setItemMeta(meta);

        return this;
    }

    public ItemStack build() {
        return item;
    }

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private List<String> color(List<String> list) {
        return list.stream().map(x -> color(x)).collect(Collectors.toList());
    }

}
