package sinsa.zombie.stat;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.entity.Player;
import sinsa.zombie.utils.io.Files;
import sinsa.zombie.utils.logging.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatView {

    private static final Logger logger = Logger.getLogger(StatView.class);
    private static final Gson gson = new Gson();
    private static final Map<UUID, StatView> instances = new HashMap<>();

    public static void saveAll() {
        for (StatView value : instances.values()) {
            value.save();
        }
    }

    static {
        Files.getDirectory("stats", true);
    }

    private static EnumMap<Stats, Integer> newMap() {
        final EnumMap<Stats, Integer> map = new EnumMap<>(Stats.class);
        for (Stats value : Stats.values()) {
            map.put(value, 0);
        }
        return map;
    }

    public static StatView get(Player player) {
        if (instances.containsKey(player.getUniqueId())) return instances.get(player.getUniqueId());
        final StatView view = new StatView(player.getUniqueId());
        instances.put(player.getUniqueId(), view);
        return view;
    }

    private final UUID uniqueId;
    private final EnumMap<Stats, Integer> values;

    private StatView(UUID uniqueId) {
        EnumMap<Stats, Integer> lazyValues;
        this.uniqueId = uniqueId;
        if (Files.getFile("stats/" + uniqueId + ".json", false).exists()) {
            try (final FileReader reader = new FileReader(Files.getFile("stats/" + uniqueId + ".json", true))) {
                lazyValues = gson.fromJson(reader, new TypeToken<EnumMap<Stats, Integer>>() {}.getType());
            } catch (IOException e) {
                logger.error("통계를 불러오는 중 오류가 발생했습니다.");
                e.printStackTrace();
                lazyValues = newMap();
            }
        } else lazyValues = newMap();
        this.values = lazyValues;
    }

    public int getValue(Stats stats) {
        if (!values.containsKey(stats)) values.put(stats, 0);
        return values.get(stats);
    }

    public void setValue(Stats stats, int value) {
        values.put(stats, value);
    }

    public void addValue(Stats stats, int value) {
        if (!values.containsKey(stats)) values.put(stats, 0);
        values.put(stats, values.get(stats) + value);
    }

    public void save() {
        try (FileWriter writer = new FileWriter(Files.getFile("stats/" + uniqueId + ".json", true))) {
            gson.toJson(values, writer);
        } catch (IOException e) {
            logger.error("통계 저장 중 오류가 발생했습니다.");
        }
    }

}
