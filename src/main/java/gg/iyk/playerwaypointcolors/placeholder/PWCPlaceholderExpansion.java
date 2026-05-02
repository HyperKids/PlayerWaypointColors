package gg.iyk.playerwaypointcolors.placeholder;

import gg.iyk.playerwaypointcolors.PlayerWaypointColors;
import gg.iyk.playerwaypointcolors.compat.WaypointAdapter;
import gg.iyk.playerwaypointcolors.compat.WaypointColorNotSupportedException;
import gg.iyk.playerwaypointcolors.config.PWCConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PWCPlaceholderExpansion extends PlaceholderExpansion {

    private static final String COLOR_PREFIX = "color_";

    private final PlayerWaypointColors plugin;
    private final WaypointAdapter adapter;

    public PWCPlaceholderExpansion(PlayerWaypointColors plugin, WaypointAdapter adapter) {
        this.plugin = plugin;
        this.adapter = adapter;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pwc";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";
        if (!params.startsWith(COLOR_PREFIX)) return null;

        PWCConfig cfg = plugin.configManager().config();
        String formatKey = params.substring(COLOR_PREFIX.length());
        String template = cfg.placeholders.formats.get(formatKey);
        if (template == null) {
            if (cfg.debug) plugin.getLogger().info("[PWC debug] unknown format key: " + formatKey);
            return "";
        }

        Color color;
        try {
            color = adapter.getWaypointColor(player);
        } catch (WaypointColorNotSupportedException e) {
            if (cfg.debug) plugin.getLogger().info("[PWC debug] getWaypointColor not supported on this server (pre-1.21.11 Paper?)");
            return "";
        }

        if (color == null) {
            if (cfg.debug) plugin.getLogger().info("[PWC debug] " + player.getName() + " has null waypoint color (no explicit color set)");
            return "";
        }

        return expandTemplate(template, color);
    }

    private static String expandTemplate(String template, Color c) {
        String hex = String.format("%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
        return template
            .replace("{HEX}", hex)
            .replace("{hex}", hex.toLowerCase())
            .replace("{R1}", hex.substring(0, 1))
            .replace("{R2}", hex.substring(1, 2))
            .replace("{G1}", hex.substring(2, 3))
            .replace("{G2}", hex.substring(3, 4))
            .replace("{B1}", hex.substring(4, 5))
            .replace("{B2}", hex.substring(5, 6));
    }
}
