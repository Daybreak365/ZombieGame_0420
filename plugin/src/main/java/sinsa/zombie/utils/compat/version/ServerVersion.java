package sinsa.zombie.utils.compat.version;

import com.google.common.base.Enums;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerVersion {
    private static final Logger logger = Logger.getLogger(ServerVersion.class.getName());
    public static final NMSVersion INSTANCE;

    static {
        String className = Bukkit.getServer().getClass().getName();
        String[] parts = className.replace('.', ',').split(",");
        NMSVersion temp = NMSVersion.UNSUPPORTED;
        if (parts.length >= 4) {
            temp = Enums.getIfPresent(NMSVersion.class, parts[3]).or(NMSVersion.UNSUPPORTED);
        }
        INSTANCE = temp;
    }

    public static boolean compatVersion(Plugin plugin) {
        if (INSTANCE != NMSVersion.UNSUPPORTED) {
            if (INSTANCE.getVersion() >= 13) {
                try {
                    PluginDescriptionFile desc = plugin.getDescription();
                    java.lang.reflect.Field field = PluginDescriptionFile.class.getDeclaredField("apiVersion");
                    field.setAccessible(true);
                    field.set(desc, "1." + INSTANCE.getVersion());
                    field.setAccessible(false);
                } catch (ReflectiveOperationException ignored) {
                }
            }
            return true;
        } else {
            logger.log(Level.SEVERE, "플러그인이 지원하지 않는 버전을 이용하고 있습니다.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }
}
