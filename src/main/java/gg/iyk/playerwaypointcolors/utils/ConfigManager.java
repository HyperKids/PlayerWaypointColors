package gg.iyk.playerwaypointcolors.utils;

import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import gg.iyk.playerwaypointcolors.PlayerWaypointColors;
import gg.iyk.playerwaypointcolors.config.PWCConfig;
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
    private PWCConfig config;

    public ConfigManager(PlayerWaypointColors plugin, BukkitAudiences adventure) {
        this.plugin = plugin;
        this.adventure = adventure;
        this.miniMessage = MiniMessage.miniMessage();
        loadConfigs();
    }

    public PWCConfig config() {
        return this.config;
    }

    public void loadConfigs() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        configFile.getParentFile().mkdirs();

        // LOWER_KEBAB_CASE matches the existing on-disk schema (configVersion -> config-version,
        // autoApplyOnJoin -> auto-apply-on-join, etc.) so users upgrading don't see all their
        // keys renamed.
        YamlConfigurationProperties props = YamlConfigurationProperties.newBuilder()
            .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
            .build();

        try {
            // update() = load + populate-defaults-for-missing-fields + save. Creates the
            // file from defaults if it doesn't exist.
            this.config = YamlConfigurations.update(configFile.toPath(), PWCConfig.class, props);

            // Bump configVersion in the user's file to match the current schema's default.
            // This is the hook where future schema migrations (rename fields, transform
            // values, restructure) will run — branch on the LOADED configVersion before
            // syncing it forward. Today: schema is at v2, no migrations needed, just sync.
            int currentVersion = new PWCConfig().configVersion;
            if (this.config.configVersion != currentVersion) {
                plugin.getLogger().info("Updating config schema version from "
                    + this.config.configVersion + " to " + currentVersion);
                this.config.configVersion = currentVersion;
                YamlConfigurations.save(configFile.toPath(), PWCConfig.class, this.config, props);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load config.yml: " + e.getMessage());
            this.config = new PWCConfig();
        }

        // messages.yml is i18n strings, not config — ConfigLib is the wrong tool. Stays
        // on Bukkit's YamlConfiguration.
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
