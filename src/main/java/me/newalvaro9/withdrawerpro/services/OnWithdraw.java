package me.newalvaro9.withdrawerpro.services;

import java.util.Arrays;
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
            float dollarAmount = Float.parseFloat(args[0]);
            EconomyResponse response = WithdrawerPro.getEco().withdrawPlayer((OfflinePlayer) player, dollarAmount);
            ItemStack paper = createPaper(dollarAmount);

            if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
                player.getInventory().addItem(paper);
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

    @SuppressWarnings("ConstantConditions")
    private ItemStack createPaper(float dollarAmount) {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();

        if (meta != null) {
            String displayName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item.display_name"));
            String loreLine1 = ChatColor.AQUA + "$" + dollarAmount;
            String loreLine2 = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("item.lore.line2"));

            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(loreLine1, loreLine2));
            paper.setItemMeta(meta);
        }

        return paper;
    }
}