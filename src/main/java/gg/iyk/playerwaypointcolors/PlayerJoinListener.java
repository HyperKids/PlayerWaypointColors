package gg.iyk.playerwaypointcolors;

import gg.iyk.playerwaypointcolors.compat.WaypointAdapter;
import me.clip.placeholderapi.PlaceholderAPI;
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
        if (!plugin.getConfig().getBoolean("placeholderapi.auto-apply-on-join", false)) {
            return;
        }

        String placeholder = plugin.getConfig().getString("placeholderapi.variable", "");
        if (placeholder.isEmpty()) {
            return;
        }

        Player player = event.getPlayer();
        String hexColor = PlaceholderAPI.setPlaceholders(player, placeholder);

        // Accept 6-char hex or 7-char with leading '#'
        if (hexColor != null) {
            if (hexColor.matches("^[a-fA-F0-9]{6}$")) {
                // valid 6-char hex
            } else if (hexColor.matches("^#[a-fA-F0-9]{6}$")) {
                hexColor = hexColor.substring(1); // strip leading '#'
            } else {
                plugin.getLogger().warning("The placeholder '" + placeholder + "' did not return a valid 6-character hex code.");
                return;
            }
            try {
                Color color = Color.fromRGB(Integer.parseInt(hexColor, 16));
                waypointAdapter.setWaypointColor(player, color);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("The placeholder '" + placeholder + "' returned an invalid hex code: " + hexColor);
            }
        } else {
            plugin.getLogger().warning("The placeholder '" + placeholder + "' did not return a valid 6-character hex code.");
        }
    }
}
