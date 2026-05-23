package sinsa.zombie.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import sinsa.zombie.ZombieGame;
import sinsa.zombie.game.GameManager;

public class ZombiePlaceholderExpansion extends PlaceholderExpansion {

    private final ZombieGame plugin;

    public ZombiePlaceholderExpansion(ZombieGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "zb";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        GameManager.Snapshot snapshot = GameManager.getSnapshot();
        return switch (params.toLowerCase()) {
            case "is_in_game" -> Boolean.toString(GameManager.isRunning());
            case "human_count" -> Integer.toString(snapshot.getHumanCount());
            case "zombie_count" -> Integer.toString(snapshot.getZombieCount());
            case "role" -> player == null ? "0" : Integer.toString(snapshot.getRole(player.getUniqueId()).getApiCode());
            case "human_kills" -> player == null ? "unknown" : Integer.toString(snapshot.getHumanKills(player.getUniqueId()));
            case "zombie_kills" ->
                    player == null ? "unknown" : Integer.toString(snapshot.getZombieKills(player.getUniqueId()));
            case "time_all" -> Integer.toString(snapshot.getTimeAll());
            case "time_now" -> Integer.toString(snapshot.getTimeNow());
            case "time_remaining" -> Integer.toString(snapshot.getTimeRemaining());
            default -> null;
        };
    }
}
