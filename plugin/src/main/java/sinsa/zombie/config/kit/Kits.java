package sinsa.zombie.config.kit;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import sinsa.zombie.config.cached.CachedConfig;
import sinsa.zombie.utils.logging.Logger;

import java.io.IOException;
import java.util.List;

public class Kits {

    private static final Logger logger = Logger.getLogger(Kits.class);

    private Kits() {}

    public static final CachedConfig<KitNodes> instance;

    static {
        CachedConfig<KitNodes> temp;
        try {
            temp = new CachedConfig<>(KitNodes.class, "kits.yml");
            for (KitNodes value : KitNodes.values()) {
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

    public static void setKit(KitNodes node, List<ItemStack> items) {
        instance.setProperty(node, items);
    }

    public static List<ItemStack> getKit(KitNodes node) {
        return instance.getList(node, ItemStack.class);
    }

    public static KitNodes getArmorNode(KitNodes node) {
        switch (node) {
            case SURVIVOR:
                return KitNodes.SURVIVOR_ARMOR;
            case HERO:
                return KitNodes.HERO_ARMOR;
            case INITIAL_ZOMBIE:
                return KitNodes.INITIAL_ZOMBIE_ARMOR;
            case INFECTEE:
                return KitNodes.INFECTEE_ARMOR;
            default:
                return node;
        }
    }

    public static List<ItemStack> getArmorKit(KitNodes node) {
        return getKit(getArmorNode(node));
    }

    public static void setArmorKit(KitNodes node, List<ItemStack> items) {
        setKit(getArmorNode(node), items);
    }

}
