# PlayerWaypointColors

Easily customize player waypoint colors on your Minecraft server's map locator bar!

## Overview
PlayerWaypointColors lets players set their own waypoint colors, and lets admins set colors for other players. Supports automatic player color assignment using PlaceholderAPI.

## Features
- Assign custom hex colors to player waypoints (e.g., `#FF0000` or `FF0000` for red)
- Simple commands for players and admins
- Optional PlaceholderAPI integration for automatic color assignment on join
- Exposes the `%pwc_color_<format>%` placeholder so other plugins (chat, scoreboard, etc.) can read each player's current waypoint color in any format you configure
- Configurable default color applied to players on join

## Compatibility
- **Minecraft:** Spigot 1.21.11+ or Paper 1.21.11+
- **Optional:** [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

## Installation
1. Download the latest `PlayerWaypointColors.jar`.
2. Place it in your server's `plugins` folder.
3. (Optional) Install PlaceholderAPI if you want dynamic color assignment.
4. Start or reload your server.

## Commands & Permissions
| Command                                          | Description                                         | Permission                   | Default |
|--------------------------------------------------|-----------------------------------------------------|------------------------------|---------|
| `/pwc help`                                      | Show help message                                   | playerwaypointcolor.help      | true    |
| `/pwc set <color\|hex> [#hexcode]`               | Set your own waypoint color                         | playerwaypointcolor.self      | OP      |
| `/pwc setother <player> <color\|hex> [#hexcode]` | Set another player's waypoint color                 | playerwaypointcolor.others    | OP      |
| `/pwc get`                                       | Get your own waypoint color                         | playerwaypointcolor.self      | OP      |
| `/pwc get <player>`                              | Get another player's waypoint color                 | playerwaypointcolor.others    | OP      |
| `/pwc reset`                                     | Reset your own waypoint color                       | playerwaypointcolor.self      | OP      |
| `/pwc reset <player>`                            | Reset another player's waypoint color               | playerwaypointcolor.others    | OP      |

## Configuration
The plugin creates a `config.yml` file on first run. You can:
- Enable PlaceholderAPI integration
- Set a placeholder for automatic color assignment

Example config:
```yaml
# Schema version. Don't edit this manually — used to migrate older config.yml files when the plugin upgrades.
config-version: 2
placeholderapi:
  # Set to true to read a player's waypoint color from a PlaceholderAPI placeholder when they join.
  auto-apply-on-join: true
  # PAPI placeholder to read. Should return a 6-character hex code (e.g. "FF0000" or "#FF0000"). Leave empty to disable.
  variable: '%luckperms_meta_color%'
placeholders:
  # Templates for the placeholder this plugin exposes:
  #   %pwc_color_<format>%   where <format> is one of the keys below.
  # Variables you can use in templates:
  #   {HEX}  uppercase hex like FF0000
  #   {hex}  lowercase hex like ff0000
  #   {R1} {R2} {G1} {G2} {B1} {B2}  each channel split into two hex digits
  #                                  (used by the legacy spigot format below)
  # Add your own keys to support whatever your chat plugin needs.
  # If a player hasn't picked a color, the placeholder returns nothing.
  formats:
    minimessage: <#{HEX}>
    hex: '#{HEX}'
    raw: '{HEX}'
    legacy: '&x&{R1}&{R2}&{G1}&{G2}&{B1}&{B2}'
# Color applied to a player on join when PAPI auto-apply (above) is off
# or returns nothing. Set to a 6-character hex (e.g. "00FF00").
# To disable, leave as "".
default-color: ff0000
# When to apply default-color:
#   false: only if the player doesn't already have a color. Won't overwrite
#          a color they picked with /pwc set.
#   true:  overwrite the player's color on every join, including any color
#          they picked with /pwc set. The only color it won't overwrite is
#          one set by auto-apply-on-join above (that takes precedence).
default-color-force: false
# Print extra info to the console for troubleshooting. Leave off normally.
debug: false
```

## PlaceholderAPI Integration

PlaceholderAPI integration goes both directions: PWC can read a placeholder to assign a color on join (input), and PWC exposes its own placeholder so other plugins can read each player's current color (output).

### Reading colors from a placeholder (auto-apply on join)

If PlaceholderAPI is installed, you can use a placeholder to automatically assign hex colors when players join. Set the variable in config.yml to your desired placeholder (e.g., `%luckperms_meta_color%`), which must return hex colors in the format `#00FF00` or `00FF00`.

The easiest way to do this is by using LuckPerms meta functionality.
1. Set the permission `meta.color.#FF0000` on a group
2. Set the config.yml option `auto-apply-on-join` to `true`
3. Set the config.yml option `variable` to `%luckperms_meta_color%`
4. Save config.yml, restart your server

Alternatively, you can use the PlaceholderAPI String expansion (particularly `%string_replaceCharacters_<configuration>_<string>%`) to manipulate your placeholder to return a valid hex code.

### Exposing a player's current color to other plugins

If PlaceholderAPI is installed, PWC registers a placeholder of the form:

```
%pwc_color_<format>%
```

`<format>` is the name of any key under `placeholders.formats` in config.yml. Out of the box you get:

| Placeholder               | Returns                   | Example          |
|---------------------------|---------------------------|------------------|
| `%pwc_color_minimessage%` | MiniMessage hex tag       | `<#FF0000>`      |
| `%pwc_color_hex%`         | hex with leading `#`      | `#FF0000`        |
| `%pwc_color_raw%`         | bare 6-character hex      | `FF0000`         |
| `%pwc_color_legacy%`      | Spigot legacy color codes | `&x&F&F&0&0&0&0` |

Add your own format keys in config.yml to support whatever syntax your chat or scoreboard plugin needs. Templates can use these variables (case-sensitive):

- `{HEX}` — uppercase 6-char hex (e.g. `FF0000`)
- `{hex}` — lowercase 6-char hex (e.g. `ff0000`)
- `{R1}` `{R2}` `{G1}` `{G2}` `{B1}` `{B2}` — each channel split into its two hex digits

Example: a chat plugin that uses `<#hex>` MiniMessage syntax can format messages like `%pwc_color_minimessage%<player_name></...>` to color player names with each player's chosen waypoint color.

If a player hasn't explicitly picked or been assigned a color, the placeholder returns an empty string regardless of the format requested.

## FAQ
**Q:** The color isn't changing for a player!  
**A:** Make sure the color code is a valid hex code (e.g., `#00FF00` or `00FF00`). If using PlaceholderAPI, check that the placeholder returns a valid value.

**Q:** Does this work with Paper?
**A:** Yes, both Spigot and Paper are supported on 1.21.11 and newer. Earlier Paper versions (1.21.7 through 1.21.10) work for setting and resetting colors but do not support `/pwc get` or the `%pwc_color_<format>%` placeholder, since the underlying API for reading a player's waypoint color was added in 1.21.11.

## Support
If you need help, please provide:
- Your server version
- Plugin version
- Any error messages

Report bugs or request features on GitHub.

## Contributing
Suggestions and contributions are welcome! Feel free to open an issue or pull request.
