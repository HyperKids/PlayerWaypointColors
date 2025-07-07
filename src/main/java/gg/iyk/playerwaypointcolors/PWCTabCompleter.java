package gg.iyk.playerwaypointcolors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PWCTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("set", "setother", "get", "reset", "help");
    private static final List<String> COLORS = Arrays.asList("RED", "BLUE", "GREEN", "YELLOW", "AQUA", "BLACK", "WHITE", "PURPLE", "hex");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, completions);
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "set":
                    if (sender.hasPermission("playerwaypointcolor.self")) {
                        StringUtil.copyPartialMatches(args[1], COLORS, completions);
                    }
                    break;
                case "setother":
                case "get":
                case "reset":
                    if (sender.hasPermission("playerwaypointcolor.others")) {
                        List<String> playerNames = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            playerNames.add(player.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], playerNames, completions);
                    }
                    break;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setother") && sender.hasPermission("playerwaypointcolor.others")) {
                StringUtil.copyPartialMatches(args[2], COLORS, completions);
            }
        }

        return completions;
    }
}

