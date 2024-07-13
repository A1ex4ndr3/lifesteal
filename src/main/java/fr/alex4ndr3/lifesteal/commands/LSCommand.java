package fr.alex4ndr3.lifesteal.commands;

import fr.alex4ndr3.lifesteal.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LSCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public LSCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Utiliser: /ls <addlife|removelife|resetlife|broadcast> <player> <amount>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "broadcast":
            case "bc":
                if (sender.hasPermission("lifesteal.broadcast")) {
                    return new CommandBroadcast().onCommand(sender, command, label, args);
                } else {
                    sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
                }
                break;

            case "addlife":
                if (sender.hasPermission("lifesteal.addlife")) {
                    return new addLife(plugin).onCommand(sender, command, label, args);
                } else {
                    sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
                }
                break;

            case "reload":
            case "rl":
                if (sender.hasPermission("lifesteal.reload")) {
                    return new ReloadCommand(plugin).onCommand(sender, command, label, args);
                } else {
                    sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
                }
                break;

            case "removelife":
            case "rmvlife":
                if (sender.hasPermission("lifesteal.removelife")) {
                    return new removeLife(plugin).onCommand(sender, command, label, args);
                } else {
                    sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
                }
                break;

            case "resetlife":
            case "rslife":
                if (sender.hasPermission("lifesteal.resetlife")) {
                    return new resetLife(plugin).onCommand(sender, command, label, args);
                } else {
                    sender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
                }
                break;

            default:
                sender.sendMessage("Sous-commande inconnue. Utiliser: /ls <addlife|removelife|resetlife|broadcast> <player> <amount>");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // Suggest subcommands
            if ("broadcast".startsWith(args[0].toLowerCase())) completions.add("broadcast");
            if ("bc".startsWith(args[0].toLowerCase())) completions.add("bc");
            if ("addlife".startsWith(args[0].toLowerCase())) completions.add("addlife");
            if ("removelife".startsWith(args[0].toLowerCase())) completions.add("removelife");
            if ("resetlife".startsWith(args[0].toLowerCase())) completions.add("resetlife");
            if("rmvlife".startsWith(args[0].toLowerCase())) completions.add("rmvlife");
            if("rslife".startsWith(args[0].toLowerCase())) completions.add("rslife");
            if ("reload".startsWith(args[0].toLowerCase())) completions.add("reload");
            if ("rl".startsWith(args[0].toLowerCase())) completions.add("rl");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("resetlife")) {
                completions.add("@a");
                completions.add("@s");
            }
            if (args[0].equalsIgnoreCase("rslife")) {
                completions.add("@a");
                completions.add("@s");
            }
            if (!args[0].equalsIgnoreCase("broadcast") && !args[0].equalsIgnoreCase("bc")) {
                // Suggest player names
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(player.getName());
                    }
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
