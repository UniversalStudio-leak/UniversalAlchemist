package ru.universalstudio.universalalchemist.gui;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author default source code: WinLocker02 (Thank pasting wAxes -> UniversalAxe)
 */

public class ItemNBTWrapper {

    private String version;

    private Object nbtTagCompound;
    private Object itemNMS;
    private Class<?> craftItemStack;

    private ItemStack item;

    public ItemNBTWrapper(ItemStack item) {
        this.item = item;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

            craftItemStack = getCraftItemStack();
            itemNMS = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);

            boolean hasTag = (boolean) itemNMS.getClass().getMethod("hasTag").invoke(itemNMS);
            Object tag = itemNMS.getClass().getMethod("getTag").invoke(itemNMS);

            nbtTagCompound = hasTag ? tag : getNBTTagCompound().newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNBT(String key, String value) {
        try {
            nbtTagCompound.getClass().getMethod("setString", String.class, String.class).invoke(nbtTagCompound, key, value);
            itemNMS.getClass().getMethod("setTag", getNBTTagCompound()).invoke(itemNMS, nbtTagCompound);

            Object itemStack = craftItemStack.getMethod("asBukkitCopy", Class.forName("net.minecraft.server." + version + ".ItemStack")).invoke(craftItemStack, itemNMS);

            item.setItemMeta((ItemMeta) itemStack.getClass().getMethod("getItemMeta").invoke(itemStack));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasKey(String key) {
        try {
            String value = (String) nbtTagCompound.getClass().getMethod("getString", String.class).invoke(nbtTagCompound, key);

            return value != null && !value.isEmpty();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getValue(String key) {
        try {
            return (String) nbtTagCompound.getClass().getMethod("getString", String.class).invoke(nbtTagCompound, key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<?> getCraftItemStack() {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<?> getNBTTagCompound() {
        try {
            return Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemStack getItem() {
        return item;
    }
}
