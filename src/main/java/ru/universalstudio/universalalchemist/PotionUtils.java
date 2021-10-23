package ru.universalstudio.universalalchemist;

import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import com.google.common.collect.*;
import net.minecraft.server.v1_12_R1.MobEffect; // всё паламався или ты будешь лично указывать урл класса т.к их дохера в либах...)
import net.minecraft.server.v1_12_R1.PotionRegistry;
import org.bukkit.craftbukkit.v1_12_R1.potion.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author default source code: WinLocker02
 */

public class PotionUtils {

    public static Collection<PotionEffect> getEffects(PotionType potionType, boolean upgraded, boolean extended) { // мне кажется я тут побыл мамомнтом и не понял прикола в изменение булеанов местами
        List<MobEffect> list = PotionRegistry.a(CraftPotionUtil.fromBukkit(new PotionData(potionType, extended, upgraded))).a();
        ImmutableList.Builder builder = new ImmutableList.Builder();
        for (MobEffect mobEffect : list) {
            builder.add(CraftPotionUtil.toBukkit(mobEffect));
        }
        return builder.build();
    }

    public static Collection<PotionEffect> getEffects(ItemStack item) {
        PotionMeta potionMeta = (PotionMeta)item.getItemMeta();
        PotionData potionData = potionMeta.getBasePotionData();
        ArrayList arrayList = new ArrayList(PotionUtils.getEffects(potionData.getType(), potionData.isUpgraded(), potionData.isExtended()));
        arrayList.addAll(potionMeta.getCustomEffects());
        return arrayList;
    }

    public static List<ItemStack> getPotions(Player player) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack == null || !itemStack.getType().name().endsWith("POTION")) continue;
            arrayList.add(itemStack);
        }
        return arrayList;
    }

    public static ItemStack mixPotions(ItemStack item, ItemStack item2) {
        return mixPotions(item, item2, Collections.singletonList(Optional.ofNullable(mixColors(item, item2))));
    }

    public static ItemStack mixPotions(ItemStack item, ItemStack item2, List<Optional<Color>> list) {
        item = item.clone(); // баккит творит удивительные вещи
        PotionMeta potionMeta = (PotionMeta)item.getItemMeta();
        getEffects(item2).forEach(potionEffect -> {potionMeta.addCustomEffect(potionEffect, true);});
        list.forEach(optional -> { // здесь я мастерил паравозик
            optional.ifPresent(color -> {
                if (potionMeta.hasColor()) {
                    potionMeta.setColor(potionMeta.getColor().mixColors(color));
                } else {
                    potionMeta.setColor(color);
                }
            });
        });
        item.setItemMeta(potionMeta);
        return item;
    }

    public static Color mixColors(ItemStack item, ItemStack item2) {
        PotionMeta potionMeta = (PotionMeta)item.getItemMeta();
        PotionMeta potionMeta2 = (PotionMeta)item2.getItemMeta();
        PotionData potionData = potionMeta.getBasePotionData();
        PotionData potionData2 = potionMeta2.getBasePotionData();
        Color color = potionData.getType().getEffectType().getColor();
        Color color2 = potionData2.getType().getEffectType().getColor();
        return (color != null && color2 != null) ? color.mixColors(color2) : null; // ммм... я бы переписал это говнецо
    }

    public static Color colorValueOf(String colorString) {
        try {
            Color color = (Color)Color.class.getField(colorString.toUpperCase()).get(null);
            return color;
        } catch (Exception ex) { // Тимур Фиксов: *МатьException*
            return null;
        }
    }

}
