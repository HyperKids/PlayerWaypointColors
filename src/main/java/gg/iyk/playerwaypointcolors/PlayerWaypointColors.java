package gg.iyk.playerwaypointcolors;

import gg.iyk.playerwaypointcolors.compat.PaperWaypointAdapter;
import gg.iyk.playerwaypointcolors.compat.SpigotWaypointAdapter;
import gg.iyk.playerwaypointcolors.compat.WaypointAdapter;
import gg.iyk.playerwaypointcolors.placeholder.PWCPlaceholderExpansion;
import gg.iyk.playerwaypointcolors.utils.ConfigManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerWaypointColors extends JavaPlugin {

    private BukkitAudiences adventure;
    private ConfigManager configManager;
    private WaypointAdapter waypointAdapter;

    public ConfigManager configManager() {
        return this.configManager;
    }

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        // ConfigManager owns config.yml via ConfigLib — no need for saveDefaultConfig()
        // (Bukkit's mechanism, which would write a bundled config.yml that no longer exists).
        this.configManager = new ConfigManager(this, this.adventure);

        // Detect Spigot vs Paper by checking for Player#setWaypointColor
        boolean isSpigot = false;
        try {
            Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
            playerClass.getMethod("setWaypointColor", org.bukkit.Color.class);
            isSpigot = true;
        } catch (Exception ignored) {}

        if (isSpigot) {
            this.waypointAdapter = new SpigotWaypointAdapter();
            getLogger().info("Using SpigotWaypointAdapter (native API)");
        } else {
            this.waypointAdapter = new PaperWaypointAdapter(this.configManager);
            getLogger().info("Using PaperWaypointAdapter (command parsing)");
        }

        getCommand("playerwaypointcolor").setExecutor(new PWCCommand(this.configManager, this.waypointAdapter));
        getCommand("playerwaypointcolor").setTabCompleter(new PWCTabCompleter());

        // Always register the join listener — it handles both PAPI auto-apply and the default-color fallback.
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, waypointAdapter), this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PWCPlaceholderExpansion(this, this.waypointAdapter).register();
            getLogger().info("PlaceholderAPI found! Placeholder expansion registered.");
        } else {
            getLogger().info("PlaceholderAPI not found; %pwc_color_<format>% placeholder unavailable.");
        }

        getLogger().info("PlayerWaypointColors has been enabled!");
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        getLogger().info("PlayerWaypointColors has been disabled.");
    }
}
