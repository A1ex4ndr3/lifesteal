package fr.alex4ndr3.lifesteal.commands;

import fr.alex4ndr3.lifesteal.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    private final Main plugin;

    public ReloadCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("lifesteal.reload")) {
            plugin.reloadConfig();
            plugin.loadMessages();
            sender.sendMessage(ChatColor.DARK_GREEN + plugin.getMessage("plugin-reloaded", sender instanceof Player ? (Player) sender : null));
        } else {
            sender.sendMessage(ChatColor.RED + plugin.getMessage("no-permission", sender instanceof Player ? (Player) sender : null));
        }
        return true;
    }
}
