package sinsa.zombie.game.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import sinsa.zombie.api.ZombieGameApi;
import sinsa.zombie.config.FriendlyGlowSettings;

public class FriendlyGlowService implements Runnable {

    private final JavaPlugin plugin;
    private final ZombieGameApi api;
    private final FriendlyGlowSettings settings;

    public FriendlyGlowService(JavaPlugin plugin, ZombieGameApi api, FriendlyGlowSettings settings) {
        this.plugin = plugin;
        this.api = api;
        this.settings = settings;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 20L, 10L);
    }

    @Override
    public void run() {
        if (!settings.isEnabled()) return;

        for (Player viewer : Bukkit.getOnlinePlayers()) {
            applyForViewer(viewer);
        }
    }

    private void applyForViewer(Player viewer) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (viewer.equals(target)) continue;

            ZBTeam viewerTeam = api.zb_team(viewer);
            ZBTeam targetTeam = api.zb_team(target);

            if (viewerTeam == null || targetTeam == null) {
                target.setGlowing(false);
                continue;
            }

            boolean sameSide = viewerTeam.isSameSide(targetTeam);
            boolean inRange = viewer.getWorld().equals(target.getWorld())
                    && viewer.getLocation().distanceSquared(target.getLocation()) <= (settings.getRange() * settings.getRange());

            if (sameSide && inRange) {
                Team team = getOrCreateTeam(board, targetTeam);
                team.addEntry(target.getName());
                target.setGlowing(true);
            }
        }

        viewer.setScoreboard(board);
    }

    private Team getOrCreateTeam(Scoreboard board, ZBTeam targetTeam) {
        String name = "zb_" + targetTeam.name().toLowerCase();
        Team team = board.getTeam(name);
        if (team == null) {
            team = board.registerNewTeam(name);
        }

        ChatColor color = settings.getGlowColor(targetTeam);
        team.setColor(color);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        return team;
    }
}