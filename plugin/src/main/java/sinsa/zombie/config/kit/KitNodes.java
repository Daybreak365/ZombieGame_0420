package sinsa.zombie.config.kit;

import org.bukkit.inventory.ItemStack;
import sinsa.zombie.config.cached.Cacher;
import sinsa.zombie.config.cached.Node;

import java.util.ArrayList;

public enum KitNodes implements Node {

    SURVIVOR(
            "survivor",
            new ArrayList<ItemStack>()
    ),
    HERO(
            "hero",
            new ArrayList<ItemStack>()
    ),
    INITIAL_ZOMBIE(
            "initial_zombie",
            new ArrayList<ItemStack>()
    ),
    INFECTEE(
            "infectee",
            new ArrayList<ItemStack>()
    ),
    SURVIVOR_ARMOR(
            "survivor_armor",
            new ArrayList<ItemStack>()
    ),
    HERO_ARMOR(
            "hero_armor",
            new ArrayList<ItemStack>()
    ),
    INITIAL_ZOMBIE_ARMOR(
            "initial_zombie_armor",
            new ArrayList<ItemStack>()
    ),
    INFECTEE_ARMOR(
            "infectee_armor",
            new ArrayList<ItemStack>()
    );

    private final String path;
    private final Object defaultValue;
    private final Cacher nodeHandler;
    private final String[] comments;

    KitNodes(String path, Object defaultValue, Cacher nodeHandler, String... comments) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.nodeHandler = nodeHandler;
        this.comments = comments;
    }

    KitNodes(String path, Object defaultValue, String... comments) {
        this(path, defaultValue, null, comments);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Object getDefault() {
        return defaultValue;
    }

    @Override
    public boolean hasCacher() {
        return nodeHandler != null;
    }

    @Override
    public Cacher getCacher() {
        return nodeHandler;
    }

    @Override
    public String[] getComments() {
        return comments;
    }

}
