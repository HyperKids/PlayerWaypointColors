# PlayerWaypointColors

Easily customize player waypoint colors on your Minecraft server's map locator bar!

---

## Overview
PlayerWaypointColors is a simple plugin for Spigot and Paper servers that lets you assign unique colors to each player's map locator.

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

## FAQ
**Q:** The color isn't changing for a player!  
**A:** Make sure the color code is a valid hex code (e.g., `#00FF00` or `00FF00`). If using PlaceholderAPI, check that the placeholder returns a valid value.

**Q:** Does this work with Paper?  
**A:** Yes, both Spigot and Paper are supported.

**Q:** Can I use named colors or RGB?  
**A:** Only hex codes are supported (e.g., `#00FF00` or `00FF00`).

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
