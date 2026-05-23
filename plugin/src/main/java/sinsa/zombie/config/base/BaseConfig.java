package sinsa.zombie.config.base;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.bukkit.configuration.InvalidConfigurationException;
import sinsa.zombie.config.base.serializable.SpawnLocation;
import sinsa.zombie.config.cached.CachedConfig;
import sinsa.zombie.game.GamePhase;
import sinsa.zombie.game.PlayerRole;
import sinsa.zombie.utils.io.Files;
import sinsa.zombie.utils.logging.Logger;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseConfig {

    private static final Logger logger = Logger.getLogger(BaseConfig.class);
    private static final Gson gson = new Gson();

    private BaseConfig() {}

    public static final CachedConfig<BaseNodes> instance;

    static {
        CachedConfig<BaseNodes> temp;
        try {
            temp = new CachedConfig<>(BaseNodes.class, "config.yml");
            for (BaseNodes value : BaseNodes.values()) {
                temp.get(value);
            }
        } catch (IOException | InvalidConfigurationException e) {
            logger.error(e.toString());
            temp = null;
        }
        instance = temp;
    }

    public static boolean isLoaded() {
        return instance != null;
    }

    public static int getPhaseLength(GamePhase gamePhase) {
        return instance.get(BaseNodes.valueOf("PHASE_LENGTH_" + gamePhase.name()));
    }

    public static int getRoleHealth(PlayerRole playerRole) {
        return instance.get(BaseNodes.valueOf("HEALTH_" + playerRole.name()));
    }

    public static int getInt(BaseNodes node) {
        return instance.get(node);
    }

    public static boolean getBoolean(BaseNodes node) {
        return instance.get(node);
    }

    public static class SpawnLocations {

        private static Map<String, SpawnLocation> spawnLocations;

        static {
            try {
                spawnLocations = gson.fromJson(new FileReader(Files.getFile("spawns.json", true)), new TypeToken<Map<String, SpawnLocation>>(){}.getType());
                if (spawnLocations == null) spawnLocations = new HashMap<>();
            } catch (FileNotFoundException | JsonIOException e) {
                e.printStackTrace();
                logger.error("스폰 목록을 불러오는 중 오류가 발생했습니다.");
                spawnLocations = new HashMap<>();
            }
        }

        private SpawnLocations() {}

        public static Collection<SpawnLocation> getLocations() {
            return Collections.unmodifiableCollection(spawnLocations.values());
        }

        public static Set<Map.Entry<String, SpawnLocation>> getEntries() {
            return Collections.unmodifiableSet(spawnLocations.entrySet());
        }

        @Nullable
        public static SpawnLocation get(String name) {
            return spawnLocations.get(name);
        }

        public static boolean contains(String name) {
            return spawnLocations.containsKey(name);
        }

        public static boolean isAbsent(String name) {
            return !spawnLocations.containsKey(name);
        }

        public static void add(String name, SpawnLocation location) {
            spawnLocations.put(name, location);
            save();
        }

        public static void remove(String name) {
            spawnLocations.remove(name);
            save();
        }

        public static void setNextSpawn(String name) {
            if (name == null || "random".equalsIgnoreCase(name)) {
                BaseConfig.instance.setProperty(BaseNodes.NEXT_SPAWN, "random");
            } else {
                BaseConfig.instance.setProperty(BaseNodes.NEXT_SPAWN, name);
            }
        }

        @Nullable
        public static SpawnLocation getNextSpawn() {
            final String name = BaseConfig.instance.get(BaseNodes.NEXT_SPAWN);
            if ("random".equalsIgnoreCase(name)) return null;
            else return get(name);
        }

        private static void save() {
            try (FileWriter writer = new FileWriter(Files.getFile("spawns.json", true))) {
                gson.toJson(new HashMap<>(spawnLocations), writer);
            } catch (IOException e) {
                logger.error("스폰 목록 저장 중 오류가 발생했습니다.");
            }
        }

    }

}
