package me.newalvaro9.withdrawerpro.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import me.newalvaro9.withdrawerpro.WithdrawerPro;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnWithdraw implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("withdrawerpro.withdraw")) {
            player.sendMessage(ChatColor.RED + "You lack permission!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please enter an amount.");
            return true;
        }

        try {
            float dollarAmount = Float.parseFloat(args[0]);
            EconomyResponse response = WithdrawerPro.getEco().withdrawPlayer((OfflinePlayer) player, dollarAmount);
            ItemStack paper = createPaper(dollarAmount);

            if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
                player.getInventory().addItem(paper);
            } else {
                if(Objects.equals(response.errorMessage, "Loan was not permitted!")) {
                    player.sendMessage(ChatColor.RED + "Loan was not permitted!"); // translation thing
                } else {
                    player.sendMessage(ChatColor.RED + response.errorMessage);
                }
            }

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount! Please enter a valid number.");
            return true;
        }

        return true;
    }

    private ItemStack createPaper(float dollarAmount) {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(WithdrawerPro.noteName);
            meta.setLore(Arrays.asList(new String[] { ChatColor.AQUA + "$" + dollarAmount, ChatColor.BLUE + "(Right click to redeem)" }));
            paper.setItemMeta(meta);
        }

        return paper;
    }
}