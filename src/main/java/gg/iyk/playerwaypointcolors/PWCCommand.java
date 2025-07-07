package gg.iyk.playerwaypointcolors;

import gg.iyk.playerwaypointcolors.compat.WaypointAdapter;
import gg.iyk.playerwaypointcolors.utils.ConfigManager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PWCCommand implements CommandExecutor {

    private final ConfigManager cfg;
    private final WaypointAdapter waypointAdapter;

    public PWCCommand(ConfigManager configManager, WaypointAdapter waypointAdapter) {
        this.cfg = configManager;
        this.waypointAdapter = waypointAdapter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            cfg.sendMessage(sender, "plugin-info");
            cfg.sendMessage(sender, "plugin-usage-prompt");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "help":
                if (!sender.hasPermission("playerwaypointcolor.help")) {
                    cfg.sendMessage(sender, "no-permission");
                    break;
                }
                cfg.sendHelpMessage(sender);
                break;
            case "set":
                handleSet(sender, args);
                break;
            case "setother":
                handleSetOther(sender, args);
                break;
            case "get":
                handleGet(sender, args);
                break;
            case "reset":
                handleReset(sender, args);
                break;
            default:
                cfg.sendMessage(sender, "unknown-command");
                break;
        }
        return true;
    }

    private void handleSet(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            cfg.sendMessage(sender, "player-only-command");
            return;
        }
        if (!sender.hasPermission("playerwaypointcolor.self")) {
            cfg.sendMessage(sender, "no-permission");
            return;
        }
        if (args.length < 2) {
            cfg.sendHelpMessage(sender); // Or a specific usage message
            return;
        }

        Player player = (Player) sender;
        Color color = parseColor(args[1], (args.length > 2) ? args[2] : null);
        if (color == null) {
            cfg.sendMessage(sender, "invalid-color", Placeholder.unparsed("color", args[1]));
            return;
        }
        waypointAdapter.setWaypointColor(player, color);
        cfg.sendMessage(sender, "set-color-success", Placeholder.unparsed("color", args[1].toUpperCase()));
    }

    private void handleSetOther(CommandSender sender, String[] args) {
        if (!sender.hasPermission("playerwaypointcolor.others")) {
            cfg.sendMessage(sender, "no-permission");
            return;
        }
        if (args.length < 3) {
            cfg.sendHelpMessage(sender);
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            cfg.sendMessage(sender, "player-not-found", Placeholder.unparsed("player", args[1]));
            return;
        }
        Color color = parseColor(args[2], (args.length > 3) ? args[3] : null);
        if (color == null) {
            cfg.sendMessage(sender, "invalid-color", Placeholder.unparsed("color", args[2]));
            return;
        }
        waypointAdapter.setWaypointColor(target, color);
        cfg.sendMessage(sender, "set-other-success",
            Placeholder.unparsed("player", target.getName()),
            Placeholder.unparsed("color", args[2].toUpperCase())
        );
    }

    private void handleGet(CommandSender sender, String[] args) {
        Player target;
        if (args.length == 1) { // /pwc get
            if (!(sender instanceof Player)) {
                cfg.sendMessage(sender, "must-specify-player");
                return;
            }
            if (!sender.hasPermission("playerwaypointcolor.self")) {
                cfg.sendMessage(sender, "no-permission");
                return;
            }
            target = (Player) sender;
        } else { // /pwc get <player>
            if (!sender.hasPermission("playerwaypointcolor.others")) {
                cfg.sendMessage(sender, "no-permission");
                return;
            }
            target = Bukkit.getPlayer(args[1]);
        }

        if (target == null) {
            cfg.sendMessage(sender, "player-not-found", Placeholder.unparsed("player", args[1]));
            return;
        }

        String targetName = target.getName();
        boolean isSelf = sender.getName().equals(targetName);

        Color color;
        try {
            color = waypointAdapter.getWaypointColor(target);
        } catch (gg.iyk.playerwaypointcolors.compat.WaypointColorNotSupportedException ex) {
            cfg.sendMessage(sender, "paper-not-supported");
            return;
        }

        if (color == null) {
            cfg.sendMessage(sender, "get-color-default", Placeholder.unparsed("player", targetName));
        } else {
            String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
            if (isSelf) {
                cfg.sendMessage(sender, "get-color-self", Placeholder.unparsed("color", hex));
            } else {
                cfg.sendMessage(sender, "get-color-other", Placeholder.unparsed("player", targetName), Placeholder.unparsed("color", hex));
            }
        }
    }

    private void handleReset(CommandSender sender, String[] args) {
        Player target;
        if (args.length == 1) { // /pwc reset
            if (!(sender instanceof Player)) {
                cfg.sendMessage(sender, "must-specify-player");
                return;
            }
            if (!sender.hasPermission("playerwaypointcolor.self")) {
                cfg.sendMessage(sender, "no-permission");
                return;
            }
            target = (Player) sender;
        } else { // /pwc reset <player>
            if (!sender.hasPermission("playerwaypointcolor.others")) {
                cfg.sendMessage(sender, "no-permission");
                return;
            }
            target = Bukkit.getPlayer(args[1]);
        }

        if (target == null) {
            cfg.sendMessage(sender, "player-not-found", Placeholder.unparsed("player", args[1]));
            return;
        }

        waypointAdapter.resetWaypointColor(target);
        boolean isSelf = sender.getName().equals(target.getName());

        if (isSelf) {
            cfg.sendMessage(sender, "reset-color-success");
        } else {
            cfg.sendMessage(sender, "reset-other-success", Placeholder.unparsed("player", target.getName()));
        }
    }

    private Color parseColor(String colorName, String hexValue) {
        if (colorName.equalsIgnoreCase("hex")) {
            if (hexValue == null) return null;
            try {
                return Color.fromRGB(Integer.parseInt(hexValue.replace("#", ""), 16));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        switch (colorName.toUpperCase()) {
            case "RED": return Color.RED;
            case "BLUE": return Color.BLUE;
            case "GREEN": return Color.GREEN;
            case "YELLOW": return Color.YELLOW;
            case "AQUA": return Color.AQUA;
            case "BLACK": return Color.BLACK;
            case "WHITE": return Color.WHITE;
            case "PURPLE": return Color.PURPLE;
            default: return null;
        }
    }
}
