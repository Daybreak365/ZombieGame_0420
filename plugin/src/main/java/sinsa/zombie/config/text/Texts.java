package sinsa.zombie.config.text;

import org.bukkit.configuration.InvalidConfigurationException;
import sinsa.zombie.config.cached.CachedConfig;
import sinsa.zombie.utils.logging.Logger;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class Texts {

    private static final Logger logger = Logger.getLogger(Texts.class);

    private Texts() {}

    public static final CachedConfig<TextNodes> instance;

    static {
        CachedConfig<TextNodes> temp;
        try {
            temp = new CachedConfig<>(TextNodes.class, "texts.yml");
            for (TextNodes value : TextNodes.values()) {
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

    public static String text(TextNodes node) {
        final Object object = instance.get(node);
        if (object instanceof List) {
            final StringJoiner joiner = new StringJoiner("\n");
            for (Object o : ((List<?>) instance.get(node))) {
                joiner.add(o.toString());
            }
            return joiner.toString();
        } else {
            return (String) object;
        }
    }

}
