package me.newalvaro9.withdrawerpro.services;

import me.newalvaro9.withdrawerpro.WithdrawerPro;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InteractListener implements Listener {
    private Economy eco = WithdrawerPro.getEco();

    @EventHandler
    private void noteRedeem(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getMaterial() == null)
            return;
        if (!e.getMaterial().equals(Material.PAPER))
            return;
        ItemStack note = e.getItem();
        if (note.getItemMeta().getDisplayName().equals(WithdrawerPro.noteName)) {
            ItemMeta im = note.getItemMeta();
            if (!im.hasLore())
                return;
            float amount = Float.parseFloat(ChatColor.stripColor(note.getItemMeta().getLore().get(0)).replace('$', ' '));
            this.eco.depositPlayer((OfflinePlayer)p, amount);
            p.sendMessage(ChatColor.GREEN + "You have redeemed $" + amount);
            note.setAmount(note.getAmount() - 1);
        }
    }
}