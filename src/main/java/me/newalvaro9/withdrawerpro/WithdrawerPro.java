package me.newalvaro9.withdrawerpro;

import java.util.logging.Level;

import me.newalvaro9.withdrawerpro.services.Commands;
import me.newalvaro9.withdrawerpro.services.InteractListener;
import me.newalvaro9.withdrawerpro.services.OnWithdraw;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class WithdrawerPro extends JavaPlugin implements Listener {
    private static boolean vault;

    private static Economy econ = null;

    private static WithdrawerPro plugin;

    public static WithdrawerPro getPlugin() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        vault = setupEco();
        if (!vault) {
            getLogger().log(Level.SEVERE, ChatColor.RED + "Please download Vault to use this plugin!");
        }

        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        getCommand("withdraw").setExecutor(new OnWithdraw());
        getCommand("withdrawerpro").setExecutor(new Commands());
        saveDefaultConfig();
    }

    public static boolean getVault() {
        return vault;
    }

    private boolean setupEco() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = (Economy)rsp.getProvider();
        return (econ != null);
    }

    public static Economy getEco() {
        return econ;
    }
}