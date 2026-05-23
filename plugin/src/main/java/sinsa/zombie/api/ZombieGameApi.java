package sinsa.zombie.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sinsa.zombie.game.PlayerRole;
import sinsa.zombie.game.team.ZBTeam;

public interface ZombieGameApi {

    @Nullable
    ZBTeam zb_team(@NotNull Player player);

    void zb_setTeam(@NotNull Player player, @NotNull ZBTeam team);

    boolean zb_isSameSide(@NotNull Player a, @NotNull Player b);

    int zb_humanCount();

    int zb_zombieCount();

    PlayerRole zb_role(@NotNull Player player);

    int zb_killHuman(@NotNull Player player);

    int zb_killZombie(@NotNull Player player);

    int zb_timeAll();

    int zb_timeNow();

    int zb_timeRemaining();
}
