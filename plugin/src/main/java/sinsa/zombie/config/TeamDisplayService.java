package sinsa.zombie.game.team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sinsa.zombie.api.ZombieGameApi;
import sinsa.zombie.config.TeamDisplaySettings;

public class TeamDisplayService {

    private final ZombieGameApi api;
    private final TeamDisplaySettings settings;

    public TeamDisplayService(ZombieGameApi api, TeamDisplaySettings settings) {
        this.api = api;
        this.settings = settings;
    }

    public void updatePlayer(Player player) {
        if (!settings.isEnabled()) {
            player.setPlayerListName(player.getName());
            return;
        }

        ZBTeam team = api.zb_team(player);
        if (team == null) {
            player.setPlayerListName(player.getName());
            return;
        }

        player.setPlayerListName(settings.format(team, player.getName()));
    }

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayer(player);
        }
    }

    public void resetAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setPlayerListName(player.getName());
        }
    }
}