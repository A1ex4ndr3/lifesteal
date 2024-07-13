package fr.alex4ndr3.lifesteal;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import fr.alex4ndr3.lifesteal.commands.LSCommand;
import fr.alex4ndr3.lifesteal.flag.CustomFlags;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Main extends JavaPlugin {

    private static int maxHearts;
    private static Main instance;
    private BukkitTask checkHealthTask;
    private FileConfiguration messagesConfig;

    @Override
    public void onLoad() {
        registerCustomFlags();
    }


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadMessages();
        maxHearts = getConfig().getInt("max-hearts", 20);

        getLogger().info("Le Plugin Lifesteal est on");

        LSCommand lsCommand = new LSCommand(this);
        if (getCommand("ls") != null) {
            getCommand("ls").setExecutor(lsCommand);
            getCommand("ls").setTabCompleter(lsCommand);
        } else {
            getLogger().severe("Commande 'ls' introuvable. Vérifiez votre plugin.yml.");
        }

        Bukkit.getPluginManager().registerEvents(new LifeStealEventHandler(this), this);

        startCheckHealthTask();
    }

    @Override
    public void onDisable() {
        getLogger().info("Le Plugin Lifesteal est off");
        if (checkHealthTask != null) {
            checkHealthTask.cancel();
        }
    }

    private void registerCustomFlags() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            registry.register(CustomFlags.NO_LIFE_CHANGE);
            getLogger().info("Flag personnalisé enregistré avec succès.");
        } catch (Exception e) {
            getLogger().severe("Erreur lors de l'enregistrement du flag personnalisé.");
            e.printStackTrace();
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    public boolean canChangeLife(Player player) {
        WorldGuardPlugin wg = getWorldGuard();
        if (wg == null) {
            return true; // WorldGuard non installé, autoriser par défaut
        }

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
        if (regionManager == null) {
            return true; // Pas de régions définies, autoriser par défaut
        }

        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));
        return !regionSet.testState(wg.wrapPlayer(player), CustomFlags.NO_LIFE_CHANGE);
    }

    public void loadMessages() {
        File messagesFile = new File(getDataFolder(), "message.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("message.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(String key, Player player) {
        String message = messagesConfig.getString(key);
        if (message != null && player != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }

    public String getMessage(String key) {
        return messagesConfig.getString(key);
    }

    public double getMaxHearts() {
        return maxHearts;
    }

    public static Main getInstance() {
        return instance;
    }

    private void startCheckHealthTask() {
        checkHealthTask = Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                if (maxHealth <= 2.0) { // 2.0 health points = 1 heart
                    List<String> commands = getConfig().getStringList("commands-malus");
                    for (String command : commands) {
                        command = command.replace("{player}", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }
            }
        }, 0L, 100L); // Exécute la tâche toutes les 5 secondes (100 ticks)
    }
}
