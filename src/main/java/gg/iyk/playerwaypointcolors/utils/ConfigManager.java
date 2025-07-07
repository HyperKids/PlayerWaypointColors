package gg.iyk.playerwaypointcolors.utils;

import gg.iyk.playerwaypointcolors.PlayerWaypointColors;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ConfigManager {

    private final PlayerWaypointColors plugin;
    private final BukkitAudiences adventure;
    private final MiniMessage miniMessage;

    private FileConfiguration messagesConfig;

    public ConfigManager(PlayerWaypointColors plugin, BukkitAudiences adventure) {
        this.plugin = plugin;
        this.adventure = adventure;
        this.miniMessage = MiniMessage.miniMessage();
        loadConfigs();
    }

    public void loadConfigs() {
        // Load config.yml
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();

        // Load messages.yml
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void sendMessage(CommandSender sender, String key, TagResolver... placeholders) {
        String message = messagesConfig.getString(key);
        if (message == null || message.isEmpty()) {
            adventure.sender(sender).sendMessage(Component.text("Missing message for key: " + key).color(net.kyori.adventure.text.format.NamedTextColor.RED));
            return;
        }
        adventure.sender(sender).sendMessage(miniMessage.deserialize(message, placeholders));
    }

    public void sendHelpMessage(CommandSender sender) {
        List<String> helpLines = messagesConfig.getStringList("help-message");
        for (String line : helpLines) {
            adventure.sender(sender).sendMessage(miniMessage.deserialize(line));
        }
    }
}

