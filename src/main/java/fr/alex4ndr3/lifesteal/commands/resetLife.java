package fr.alex4ndr3.lifesteal.commands;

import fr.alex4ndr3.lifesteal.Main;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class resetLife implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public resetLife(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(plugin.getMessage("usage-reset"));
            return true;
        }

        if (args[1].equalsIgnoreCase("@a")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                resetPlayerLife(player);
            }
            sender.sendMessage(plugin.getMessage("health-reset-all"));
            return true;
        }

        if (args[1].equalsIgnoreCase("@s")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                resetPlayerLife(player);
                sender.sendMessage(plugin.getMessage("health-reset-self"));
            } else {
                sender.sendMessage("La commande @s ne peut être utilisée que par un joueur.");
            }
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getMessage("player-not-found"));
            return true;
        }

        resetPlayerLife(target);
        String message = plugin.getMessage("health-reset", target);
        message = message.replace("{player}", target.getName());
        sender.sendMessage(message);
        return true;
    }

    private void resetPlayerLife(Player player) {
        double newMaxHealth = 20;
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
        player.setHealth(newMaxHealth);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            List<String> completions = new ArrayList<>();
            completions.add("@a");
            completions.add("@s");
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
            return completions;
        }
        return Collections.emptyList();
    }
}
