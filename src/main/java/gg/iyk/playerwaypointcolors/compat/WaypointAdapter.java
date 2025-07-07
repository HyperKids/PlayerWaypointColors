package gg.iyk.playerwaypointcolors.compat;

import org.bukkit.Color;
import org.bukkit.entity.Player;

public interface WaypointAdapter {
    void setWaypointColor(Player player, Color color);
    Color getWaypointColor(Player player);
    void resetWaypointColor(Player player);
}

