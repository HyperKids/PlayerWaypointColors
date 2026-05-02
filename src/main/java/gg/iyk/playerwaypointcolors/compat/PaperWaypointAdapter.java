package gg.iyk.playerwaypointcolors.compat;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;

/**
 * Implementation of WaypointAdapter for Paper servers that captures console output
 * to parse waypoint colors from the actual in-game waypoint list command.
 */
public class PaperWaypointAdapter implements WaypointAdapter {

    // Pattern to match waypoint list output with colored player names
    private final gg.iyk.playerwaypointcolors.utils.ConfigManager configManager;

    public PaperWaypointAdapter(gg.iyk.playerwaypointcolors.utils.ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void setWaypointColor(Player player, Color color) {
        // Format the color into a 6-digit hex string (RRGGBB) without the '#'
        String hexColor = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        String command = "minecraft:waypoint modify " + player.getName() + " color hex " + hexColor;

        // Execute the command
        boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        // If command was successful, store the color in our cache
        if (result) {
            Bukkit.getLogger().info("[PWC] Set waypoint color for " + player.getName() + " to: " + hexColor);
        } else {
            Bukkit.getLogger().warning("[PWC] Failed to set waypoint color for " + player.getName());
        }
    }

    @Override
    public void resetWaypointColor(Player player) {
        String command = "minecraft:waypoint modify " + player.getName() + " color reset";

        // Execute the command
        boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        // If command was successful, remove the color from our cache
        if (result) {
            Bukkit.getLogger().info("[PWC] Reset waypoint color for " + player.getName());
        } else {
            Bukkit.getLogger().warning("[PWC] Failed to reset waypoint color for " + player.getName());
        }
    }

    @Override
    public Color getWaypointColor(Player player) {
        throw new gg.iyk.playerwaypointcolors.compat.WaypointColorNotSupportedException("Getting waypoint color is not supported on Paper.");
    }
}
