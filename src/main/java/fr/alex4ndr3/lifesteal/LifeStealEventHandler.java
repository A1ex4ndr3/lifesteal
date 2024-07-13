package fr.alex4ndr3.lifesteal;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LifeStealEventHandler implements Listener {

    private final Main plugin;

    public LifeStealEventHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deceasedPlayer = event.getEntity();
        Player killer = deceasedPlayer.getKiller();

        if (killer != null && deceasedPlayer != null) {
            if (plugin.canChangeLife(deceasedPlayer) && plugin.canChangeLife(killer)) {
                if (!deceasedPlayer.hasPermission("lifesteal.nolifechange")) {
                    // Reduce the maximum health of the deceased player by 1 heart (2 health points)
                    double newMaxHealth = Math.max(deceasedPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 2.0, 2.0);
                    deceasedPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
                }

                if (!killer.hasPermission("lifesteal.nolifechange")) {
                    // Increase the maximum health of the killer by 1 heart (2 health points)
                    double currentMaxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    double maxConfiguredHealth = plugin.getMaxHearts();
                    double newKillerMaxHealth = Math.min(currentMaxHealth + 2.0, maxConfiguredHealth * 2);
                    killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newKillerMaxHealth);

                    if (killer.hasPermission("lifesteal.cleanup")) {
                        killer.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 1));
                    }
                }
            }
        }
    }
}
