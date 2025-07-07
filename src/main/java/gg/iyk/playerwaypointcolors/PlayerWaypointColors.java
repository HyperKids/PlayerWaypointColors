package gg.iyk.playerwaypointcolors;

import gg.iyk.playerwaypointcolors.utils.ConfigManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerWaypointColors extends JavaPlugin {

    private BukkitAudiences adventure;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        this.configManager = new ConfigManager(this, this.adventure);

        getCommand("playerwaypointcolor").setExecutor(new PWCCommand(this.configManager));
        getCommand("playerwaypointcolor").setTabCompleter(new PWCTabCompleter());

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
