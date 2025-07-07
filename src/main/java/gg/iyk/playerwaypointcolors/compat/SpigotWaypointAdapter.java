package gg.iyk.playerwaypointcolors.compat;

import org.bukkit.Color;
import org.bukkit.entity.Player;

/**
 * Implementation of WaypointAdapter for Spigot servers that have the native Player#setWaypointColor methods.
 */
public class SpigotWaypointAdapter implements WaypointAdapter {

    @Override
    public void setWaypointColor(Player player, Color color) {
        player.setWaypointColor(color);
    }

    @Override
    public Color getWaypointColor(Player player) {
        return player.getWaypointColor();
    }

    @Override
    public void resetWaypointColor(Player player) {
        // Setting the color to null resets it to the default.
        player.setWaypointColor(null);
    }
}

