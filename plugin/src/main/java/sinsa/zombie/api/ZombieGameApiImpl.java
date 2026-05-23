package sinsa.zombie.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sinsa.zombie.game.GameManager;
import sinsa.zombie.game.PlayerRole;
import sinsa.zombie.game.team.ZBTeam;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ZombieGameApiImpl implements ZombieGameApi {

    private final Map<UUID, ZBTeam> teams = new ConcurrentHashMap<>();

    @Override
    public @Nullable ZBTeam zb_team(@NotNull Player player) {
        return teams.get(player.getUniqueId());
    }

    @Override
    public void zb_setTeam(@NotNull Player player, @NotNull ZBTeam team) {
        teams.put(player.getUniqueId(), team);
    }

    @Override
    public boolean zb_isSameSide(@NotNull Player a, @NotNull Player b) {
        ZBTeam ta = zb_team(a);
        ZBTeam tb = zb_team(b);
        if (ta == null || tb == null) return false;
        return ta.isSameSide(tb);
    }

    @Override
    public int zb_humanCount() {
        return GameManager.getSnapshot().getHumanCount();
    }

    @Override
    public int zb_zombieCount() {
        return GameManager.getSnapshot().getZombieCount();
    }

    @Override
    public PlayerRole zb_role(@NotNull Player player) {
        return GameManager.getSnapshot().getRole(player.getUniqueId());
    }

    @Override
    public int zb_killHuman(@NotNull Player player) {
        return GameManager.getSnapshot().getHumanKills(player.getUniqueId());
    }

    @Override
    public int zb_killZombie(@NotNull Player player) {
        return GameManager.getSnapshot().getZombieKills(player.getUniqueId());
    }

    @Override
    public int zb_timeAll() {
        return GameManager.getSnapshot().getTimeAll();
    }

    @Override
    public int zb_timeNow() {
        return GameManager.getSnapshot().getTimeNow();
    }

    @Override
    public int zb_timeRemaining() {
        return GameManager.getSnapshot().getTimeRemaining();
    }

    public void clear() {
        teams.clear();
    }
}