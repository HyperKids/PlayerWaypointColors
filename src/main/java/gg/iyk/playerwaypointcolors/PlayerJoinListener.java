package gg.iyk.playerwaypointcolors;

import gg.iyk.playerwaypointcolors.compat.WaypointAdapter;
import gg.iyk.playerwaypointcolors.compat.WaypointColorNotSupportedException;
import gg.iyk.playerwaypointcolors.config.PWCConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PlayerWaypointColors plugin;
    private final WaypointAdapter waypointAdapter;

    public PlayerJoinListener(PlayerWaypointColors plugin, WaypointAdapter waypointAdapter) {
        this.plugin = plugin;
        this.waypointAdapter = waypointAdapter;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PWCConfig cfg = plugin.configManager().config();

        if (tryApplyFromPlaceholder(player, cfg)) return;

        applyDefaultColorIfConfigured(player, cfg);
    }

    private boolean tryApplyFromPlaceholder(Player player, PWCConfig cfg) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return false;
        if (!cfg.placeholderapi.autoApplyOnJoin) return false;

        String placeholder = cfg.placeholderapi.variable;
        if (placeholder == null || placeholder.isEmpty()) return false;

        String result = PlaceholderAPI.setPlaceholders(player, placeholder);
        Color color = parseHex(result);
        if (color == null) {
            if (cfg.debug) plugin.getLogger().info("[PWC debug] PAPI placeholder '" + placeholder
                + "' returned no valid hex for " + player.getName() + " (got: " + result + ")");
            return false;
        }

        waypointAdapter.setWaypointColor(player, color);
        if (cfg.debug) plugin.getLogger().info("[PWC debug] applied PAPI color to " + player.getName());
        return true;
    }

    private void applyDefaultColorIfConfigured(Player player, PWCConfig cfg) {
        String defaultHex = cfg.defaultColor;
        if (defaultHex == null || defaultHex.isEmpty()) return;

        Color defaultColor = parseHex(defaultHex);
        if (defaultColor == null) {
            plugin.getLogger().warning("Invalid default-color in config.yml: " + defaultHex);
            return;
        }

        if (!cfg.defaultColorForce) {
            try {
                Color current = waypointAdapter.getWaypointColor(player);
                if (current != null) {
                    if (cfg.debug) plugin.getLogger().info("[PWC debug] " + player.getName()
                        + " already has a color set; skipping default (force=false).");
                    return;
                }
            } catch (WaypointColorNotSupportedException e) {
                if (cfg.debug) plugin.getLogger().info("[PWC debug] can't read current color"
                    + " (pre-1.21.11 Paper?); skipping default to be safe.");
                return;
            }
        }

        waypointAdapter.setWaypointColor(player, defaultColor);
        if (cfg.debug) plugin.getLogger().info("[PWC debug] applied default-color to " + player.getName());
    }

    private static Color parseHex(String raw) {
        if (raw == null) return null;
        String hex = raw.startsWith("#") ? raw.substring(1) : raw;
        if (!hex.matches("^[a-fA-F0-9]{6}$")) return null;
        try {
            return Color.fromRGB(Integer.parseInt(hex, 16));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
