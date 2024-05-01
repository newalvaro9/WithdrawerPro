package me.newalvaro9.withdrawerpro.utils;

import me.newalvaro9.withdrawerpro.WithdrawerPro;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class ItemManager {
    public static ItemStack createItem(long dollarAmount, Player player) {
        final WithdrawerPro plugin = WithdrawerPro.getPlugin();
        ItemStack item = new ItemStack(Material.valueOf(plugin.getConfig().getString("item.item")));
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String displayName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item.display_name"));

            meta.setDisplayName(displayName);
            ArrayList<String> arrayList = new ArrayList<>();
            for (String str : plugin.getConfig().getStringList("item.lore")) {
                str = ChatColor.translateAlternateColorCodes('&', str);
                str = str.replace("%player%", player.getName());
                str = str.replace("%amount%", NumberManager.formatNumber(dollarAmount));
                arrayList.add(str);
            }
            meta.setLore(arrayList);
            if(plugin.getConfig().getBoolean("item.glow")) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setItemMeta(meta);
        }

        return item;
    }
}
