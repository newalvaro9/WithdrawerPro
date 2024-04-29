package me.newalvaro9.withdrawerpro.services;

import me.newalvaro9.withdrawerpro.WithdrawerPro;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    private final WithdrawerPro plugin = WithdrawerPro.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("withdrawerpro")) {
            /* HELP COMMAND */
            if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                if (!sender.hasPermission("permission.admin")) {
                    return false;
                }

                sender.sendMessage("/withdrawerpro reload - Reloads the plugin's configuration");

                return true;
            }

            /* RELOAD COMMAND */
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

                if (!sender.hasPermission("withdrawerpro.admin")) {
                    return false;
                }

                plugin.reloadConfig();

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully reloaded WithdrawerPro!"));
                return true;
            }
        }
        return false;
    }
}