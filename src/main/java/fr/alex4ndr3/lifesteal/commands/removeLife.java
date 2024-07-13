package fr.alex4ndr3.lifesteal.commands;

import fr.alex4ndr3.lifesteal.Main;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class removeLife implements CommandExecutor {

    private final Main plugin;

    public removeLife(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(plugin.getMessage("usage-remove"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(plugin.getMessage("player-not-found"));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessage("invalid-number"));
            return true;
        }

        if (amount <= 0) {
            sender.sendMessage(plugin.getMessage("positive-number"));
            return true;
        }

        amount = amount * 2;
        double newMaxHealth = Math.max(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - amount, 2.0);
        if (newMaxHealth < 2.0) {
            newMaxHealth = 2.0;
        }
        int vie = (int) Math.round(amount);
        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);

        String message = plugin.getMessage("health-decreased", target);
        message = message.replace("{amount}", String.valueOf(vie /2));
        message = message.replace("{player}", target.getName());
        sender.sendMessage(message);

        return true;
    }
}
