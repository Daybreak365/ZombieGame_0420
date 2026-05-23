package sinsa.zombie.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import sinsa.zombie.game.team.ZBTeam;

import java.util.EnumMap;
import java.util.Map;

public class FriendlyGlowSettings {

    private boolean enabled;
    private double range;
    private final Map<ZBTeam, ChatColor> glowColors = new EnumMap<>(ZBTeam.class);

    public void load(FileConfiguration config) {
        enabled = config.getBoolean("friendly-glow.enabled", true);
        range = config.getDouble("friendly-glow.range", 40.0);

        glowColors.put(ZBTeam.HUMAN, parseNamedColor(config.getString("friendly-glow.colors.HUMAN", "WHITE")));
        glowColors.put(ZBTeam.HERO, parseNamedColor(config.getString("friendly-glow.colors.HERO", "DARK_BLUE")));
        glowColors.put(ZBTeam.FIRST_ZOMBIE, parseNamedColor(config.getString("friendly-glow.colors.FIRST_ZOMBIE", "DARK_RED")));
        glowColors.put(ZBTeam.INFECTED, parseNamedColor(config.getString("friendly-glow.colors.INFECTED", "WHITE")));
    }

    private ChatColor parseNamedColor(String value) {
        try {
            return ChatColor.valueOf(value.toUpperCase());
        } catch (Exception ignored) {
            return ChatColor.WHITE;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public double getRange() {
        return range;
    }

    public ChatColor getGlowColor(ZBTeam team) {
        return glowColors.getOrDefault(team, ChatColor.WHITE);
    }
}