package me.newalvaro9.withdrawerpro.services;

import me.newalvaro9.withdrawerpro.WithdrawerPro;
import me.newalvaro9.withdrawerpro.utils.NumberExtractor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InteractListener implements Listener {
    private final Economy eco = WithdrawerPro.getEco();
    private final WithdrawerPro plugin = WithdrawerPro.getPlugin();

    @EventHandler @SuppressWarnings("ConstantConditions")
    private void noteRedeem(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if (e.getMaterial() == null)
            return;
        if (!e.getMaterial().equals(Material.PAPER))
            return;
        ItemStack note = e.getItem();

        if (note.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item.display_name")))) {
            ItemMeta im = note.getItemMeta();
            if (!im.hasLore())
                return;
            long amount = NumberExtractor.extractNumber(ChatColor.stripColor(note.getItemMeta().getLore().get(0)).replace('$', ' ').trim());
            Player player = e.getPlayer();

            EconomyResponse response = this.eco.depositPlayer((OfflinePlayer)player, amount);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("on_redeem.message")
                            .replace("%received-amount%", String.valueOf(amount))
                            .replace("%balance%", String.valueOf((long) response.balance))
            ));

            if(plugin.getConfig().getBoolean("on_redeem.sound.sound_enabled")) {
                player.playSound(
                        player.getLocation(),
                        Sound.valueOf(plugin.getConfig().getString("on_redeem.sound.sound").toUpperCase()),
                        (float) plugin.getConfig().getDouble("on_redeem.sound.volume"),
                        (float) plugin.getConfig().getDouble("on_redeem.sound.pitch")
                );
            }

            note.setAmount(note.getAmount() - 1);
        }
    }
}