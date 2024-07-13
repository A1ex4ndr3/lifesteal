package fr.alex4ndr3.lifesteal.commands;

import fr.alex4ndr3.lifesteal.Main;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addLife implements CommandExecutor {

    private final Main plugin;

    public addLife(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(plugin.getMessage("usage-add"));
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
        double currentMaxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double newMaxHealth = currentMaxHealth + amount;
        double maxHealthPoints = plugin.getMaxHearts() * 2;
        if (newMaxHealth > maxHealthPoints) {
            newMaxHealth = maxHealthPoints;
        }

        int vie = (int) Math.round(amount);
        target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
        target.setHealth(newMaxHealth);

        String message = plugin.getMessage("health-increased", target);
        message = message.replace("{amount}", String.valueOf(vie / 2));
        message = message.replace("{player}", target.getName());
        sender.sendMessage(message);

        return true;
    }
}
