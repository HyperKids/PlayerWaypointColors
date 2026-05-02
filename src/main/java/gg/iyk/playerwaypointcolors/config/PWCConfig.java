package gg.iyk.playerwaypointcolors.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public final class PWCConfig {

    @Comment("Schema version. Don't edit this manually — used to migrate older config.yml files when the plugin upgrades.")
    public int configVersion = 2;

    public PlaceholderApi placeholderapi = new PlaceholderApi();

    public Placeholders placeholders = new Placeholders();

    @Comment({
        "Color applied to a player on join when PAPI auto-apply (above) is off",
        "or returns nothing. Set to a 6-character hex (e.g. \"00FF00\").",
        "To disable, leave as \"\"."
    })
    public String defaultColor = "";

    @Comment({
        "When to apply default-color:",
        "  false: only if the player doesn't already have a color. Won't overwrite",
        "         a color they picked with /pwc set.",
        "  true:  overwrite the player's color on every join, including any color",
        "         they picked with /pwc set. The only color it won't overwrite is",
        "         one set by auto-apply-on-join above (that takes precedence)."
    })
    public boolean defaultColorForce = false;

    @Comment("Print extra info to the console for troubleshooting. Leave off normally.")
    public boolean debug = false;

    @Configuration
    public static final class PlaceholderApi {
        @Comment("Set to true to read a player's waypoint color from a PlaceholderAPI placeholder when they join.")
        public boolean autoApplyOnJoin = false;

        @Comment("PAPI placeholder to read. Should return a 6-character hex code (e.g. \"FF0000\" or \"#FF0000\"). Leave empty to disable.")
        public String variable = "";
    }

    @Configuration
    public static final class Placeholders {
        @Comment({
            "Templates for the placeholder this plugin exposes:",
            "  %pwc_color_<format>%   where <format> is one of the keys below.",
            "Variables you can use in templates:",
            "  {HEX}  uppercase hex like FF0000",
            "  {hex}  lowercase hex like ff0000",
            "  {R1} {R2} {G1} {G2} {B1} {B2}  each channel split into two hex digits",
            "                                 (used by the legacy spigot format below)",
            "Add your own keys to support whatever your chat plugin needs.",
            "If a player hasn't picked a color, the placeholder returns nothing."
        })
        public Map<String, String> formats = defaultFormats();

        private static Map<String, String> defaultFormats() {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("minimessage", "<#{HEX}>");
            m.put("hex", "#{HEX}");
            m.put("raw", "{HEX}");
            m.put("legacy", "&x&{R1}&{R2}&{G1}&{G2}&{B1}&{B2}");
            return m;
        }
    }
}
