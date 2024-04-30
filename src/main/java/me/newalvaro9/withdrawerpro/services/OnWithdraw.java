package me.newalvaro9.withdrawerpro.services;

import java.util.Objects;

import me.newalvaro9.withdrawerpro.WithdrawerPro;
import me.newalvaro9.withdrawerpro.utils.ItemManager;
import me.newalvaro9.withdrawerpro.utils.NumberExtractor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OnWithdraw implements CommandExecutor {
    private final Economy eco = WithdrawerPro.getEco();
    private final WithdrawerPro plugin = WithdrawerPro.getPlugin();

    @Override @SuppressWarnings("ConstantConditions")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error_messages.player_only_command")));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("withdrawerpro.withdraw")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error_messages.permission_lack")));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error_messages.enter_amount")));
            return true;
        }

        if (args[0].contains(".")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error_messages.decimal_not_allowed")));
            return true;
        }

        try {
            long dollarAmount = Long.parseLong(args[0]);
            plugin.getLogger().info(String.valueOf(dollarAmount));
            EconomyResponse response = this.eco.withdrawPlayer((OfflinePlayer) player, dollarAmount);
            ItemStack paper = ItemManager.createPaper(dollarAmount, player);

            if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
                player.getInventory().addItem(paper);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("on_withdraw.message")
                                .replace("%taken-amount%", String.valueOf(dollarAmount))
                                .replace("%balance%", String.valueOf((long) response.balance))
                ));
            } else {
                if(Objects.equals(response.errorMessage, "Loan was not permitted!")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error_messages.loan_not_permitted")));
                } else {
                    player.sendMessage(ChatColor.RED + response.errorMessage);
                }
            }

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error_messages.invalid_amount")));
            return true;
        }

        return true;
    }
}