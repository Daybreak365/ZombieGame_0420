package sinsa.zombie.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import sinsa.zombie.game.team.ZBTeam;

import java.util.EnumMap;
import java.util.Map;

public class TeamDisplaySettings {

    private boolean enabled;
    private final Map<ZBTeam, String> colorMap = new EnumMap<>(ZBTeam.class);
    private final Map<ZBTeam, String> labelMap = new EnumMap<>(ZBTeam.class);

    public void load(FileConfiguration config) {
        enabled = config.getBoolean("team-display.enabled", true);

        colorMap.put(ZBTeam.HUMAN, config.getString("team-display.colors.HUMAN", "&3"));
        colorMap.put(ZBTeam.HERO, config.getString("team-display.colors.HERO", "&1"));
        colorMap.put(ZBTeam.FIRST_ZOMBIE, config.getString("team-display.colors.FIRST_ZOMBIE", "&4"));
        colorMap.put(ZBTeam.INFECTED, config.getString("team-display.colors.INFECTED", "&c"));
        colorMap.put(ZBTeam.SPECTATOR, config.getString("team-display.colors.SPECTATOR", "&8"));

        labelMap.put(ZBTeam.HUMAN, config.getString("team-display.labels.HUMAN", "생존자"));
        labelMap.put(ZBTeam.HERO, config.getString("team-display.labels.HERO", "영웅"));
        labelMap.put(ZBTeam.FIRST_ZOMBIE, config.getString("team-display.labels.FIRST_ZOMBIE", "최초좀비"));
        labelMap.put(ZBTeam.INFECTED, config.getString("team-display.labels.INFECTED", "감염자"));
        labelMap.put(ZBTeam.SPECTATOR, config.getString("team-display.labels.SPECTATOR", "관전자"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String format(ZBTeam team, String playerName) {
        String color = colorMap.getOrDefault(team, "&7");
        String label = labelMap.getOrDefault(team, team.name());
        return ChatColor.translateAlternateColorCodes('&',
                color + "[ " + label + " ] " + playerName);
    }
}