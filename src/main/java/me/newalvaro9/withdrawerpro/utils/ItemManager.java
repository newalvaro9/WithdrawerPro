package me.newalvaro9.withdrawerpro.utils;

import me.newalvaro9.withdrawerpro.WithdrawerPro;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class ItemManager {
    public static ItemStack createPaper(long dollarAmount, Player player) {
        final WithdrawerPro plugin = WithdrawerPro.getPlugin();
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();

        if (meta != null) {
            String displayName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item.display_name"));

            meta.setDisplayName(displayName);
            ArrayList<String> arrayList = new ArrayList<>();
            for (String str : plugin.getConfig().getStringList("item.lore")) {
                str = ChatColor.translateAlternateColorCodes('&', str);
                str = str.replace("%player%", player.getDisplayName());
                str = str.replace("%amount%", String.valueOf(dollarAmount));
                arrayList.add(str);
            }
            meta.setLore(arrayList);
            paper.setItemMeta(meta);
        }

        return paper;
    }
}
