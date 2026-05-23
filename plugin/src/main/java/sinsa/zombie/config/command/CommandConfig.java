package sinsa.zombie.config.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import sinsa.zombie.config.cached.CachedConfig;
import sinsa.zombie.utils.logging.Logger;

import java.io.IOException;
import java.util.List;

public class CommandConfig {

    private static final Logger logger = Logger.getLogger(CommandConfig.class);

    private CommandConfig() {}

    public static final CachedConfig<CommandNodes> instance;

    static {
        CachedConfig<CommandNodes> temp;
        try {
            temp = new CachedConfig<>(CommandNodes.class, "commands.yml");
            for (CommandNodes value : CommandNodes.values()) {
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

    public static List<String> getCommands(CommandNodes node) {
        return instance.getList(node, String.class);
    }

    public static void dispatch(CommandNodes node) {
        dispatch(node, null);
    }

    public static void dispatch(CommandNodes node, OfflinePlayer player) {
        for (String command : getCommands(node)) {
            if (command == null || command.trim().isEmpty()) continue;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacePlayer(command, player));
        }
    }

    private static String replacePlayer(String command, OfflinePlayer player) {
        if (player == null) return command;
        String name = player.getName() == null ? player.getUniqueId().toString() : player.getName();
        return command
                .replace("%player%", name)
                .replace("%p", name)
                .replace("%uuid%", player.getUniqueId().toString());
    }
}
