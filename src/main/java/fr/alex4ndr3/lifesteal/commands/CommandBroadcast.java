package fr.alex4ndr3.lifesteal.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBroadcast implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (args.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "Utiliser: /ls broadcast <message> ou /ls bc <message>");
            return true;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }
        String message = messageBuilder.toString().trim();
        message = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.broadcastMessage(ChatColor.RED + "[Lifesteal] " + ChatColor.GOLD + "[Annonce] " + ChatColor.WHITE + message);
        return true;
    }
}
