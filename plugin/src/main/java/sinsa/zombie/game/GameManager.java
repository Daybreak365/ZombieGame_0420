package sinsa.zombie.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GameManager {

    private GameManager() {}

    static Game currentGame;
    private static Snapshot snapshot = new Snapshot();
    private static final Set<UUID> pendingResetPlayers = new HashSet<>();

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static boolean isRunning() {
        return currentGame != null;
    }

    public static boolean start() {
        if (isRunning()) return false;
        snapshot = new Snapshot();
        return new Game().start();
    }

    public static boolean stop() {
        if (!isRunning()) return false;
        return currentGame.stop();
    }

    static void updateSnapshot(int humanCount, int zombieCount, Map<UUID, PlayerRole> roles, Map<UUID, Integer> humanKills, Map<UUID, Integer> zombieKills, int timeAll, int timeNow, int timeRemaining) {
        snapshot = new Snapshot(humanCount, zombieCount, roles, humanKills, zombieKills, timeAll, timeNow, timeRemaining);
    }

    public static Snapshot getSnapshot() {
        return snapshot;
    }

    public static void addPendingReset(UUID uuid) {
        pendingResetPlayers.add(uuid);
    }

    public static boolean isPendingReset(UUID uuid) {
        return pendingResetPlayers.contains(uuid);
    }

    public static void clearPendingReset(UUID uuid) {
        pendingResetPlayers.remove(uuid);
    }

    public static class Snapshot {
        private final int humanCount;
        private final int zombieCount;
        private final Map<UUID, PlayerRole> roles;
        private final Map<UUID, Integer> humanKills;
        private final Map<UUID, Integer> zombieKills;
        private final int timeAll;
        private final int timeNow;
        private final int timeRemaining;

        private Snapshot() {
            this(0, 0, Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap(), 0, 0, 0);
        }

        private Snapshot(int humanCount, int zombieCount, Map<UUID, PlayerRole> roles, Map<UUID, Integer> humanKills, Map<UUID, Integer> zombieKills, int timeAll, int timeNow, int timeRemaining) {
            this.humanCount = humanCount;
            this.zombieCount = zombieCount;
            this.roles = Collections.unmodifiableMap(new HashMap<>(roles));
            this.humanKills = Collections.unmodifiableMap(new HashMap<>(humanKills));
            this.zombieKills = Collections.unmodifiableMap(new HashMap<>(zombieKills));
            this.timeAll = timeAll;
            this.timeNow = timeNow;
            this.timeRemaining = timeRemaining;
        }

        public int getHumanCount() {
            return humanCount;
        }

        public int getZombieCount() {
            return zombieCount;
        }

        public PlayerRole getRole(UUID uuid) {
            return roles.getOrDefault(uuid, null);
        }

        public int getHumanKills(UUID uuid) {
            return humanKills.getOrDefault(uuid, 0);
        }

        public int getZombieKills(UUID uuid) {
            return zombieKills.getOrDefault(uuid, 0);
        }

        public int getTimeAll() {
            return timeAll;
        }

        public int getTimeNow() {
            return timeNow;
        }

        public int getTimeRemaining() {
            return timeRemaining;
        }
    }
}
