# PlayerWaypointColors

Easily customize player waypoint colors on your Minecraft server's map locator bar!

---

## Overview
PlayerWaypointColors lets players set their own waypoint colors, and lets admins set colors for other players. Supports automatic player color assignment using PlaceholderAPI.

---

## Features
- Assign custom hex colors to player waypoints (e.g., `#FF0000` or `FF0000` for red)
- Simple commands for players and admins
- Optional PlaceholderAPI integration for automatic color assignment on join

---

## Compatibility
- **Minecraft:** Spigot 1.21.7, Paper 1.21.7
- **Optional:** [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

---

## Installation
1. Download the latest `PlayerWaypointColors.jar`.
2. Place it in your server's `plugins` folder.
3. (Optional) Install PlaceholderAPI if you want dynamic color assignment.
4. Start or reload your server.

---

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

`/pwc get` is only supported on Spigot (not on Paper) due to API limitations.

---

## Configuration
The plugin creates a `config.yml` file on first run. You can:
- Enable PlaceholderAPI integration
- Set a placeholder for automatic color assignment

Example config:
```yaml
placeholderapi:
  auto-apply-on-join: false
  variable: ""
```

---

## PlaceholderAPI Integration
If PlaceholderAPI is installed, you can use a placeholder to automatically assign hex colors when players join. Set the variable in config.yml to your desired placeholder (e.g., `%luckperms_meta_color%`), which must return hex colors in the format `#00FF00` or `00FF00`.

The easiest way to do this is by using LuckPerms meta functionality.  
1. Set the permission `meta.color.#FF0000` on a group
2. Set the config.yml option `auto-apply-on-join` to `true`
3. Set the config.yml option `variable` to `%luckperms_meta_color%`
4. Save config.yml, restart your server 

Alternatively, you can use the PlaceholderAPI String expansion (particularly %string_replaceCharacters_\<configuration\>_\<string\>%) to manipulate your placeholder to return a valid hex code.

---

## FAQ
**Q:** The color isn't changing for a player!  
**A:** Make sure the color code is a valid hex code (e.g., `#00FF00` or `00FF00`). If using PlaceholderAPI, check that the placeholder returns a valid value.

**Q:** Does this work with Paper?  
**A:** Yes, both Spigot and Paper are supported.

---

## Support
If you need help, please provide:
- Your server version
- Plugin version
- Any error messages

Report bugs or request features on GitHub.

---

## Contributing
Suggestions and contributions are welcome! Feel free to open an issue or pull request.
