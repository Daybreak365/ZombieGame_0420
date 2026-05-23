package utils.minecraft;

import org.bukkit.entity.Player;

public class NMS {

    private static final NMSHandler handler = NMSFactory.createNMSHandler();

    private NMS() {}

    public static void respawn(Player player) {
        handler.respawn(player);
    }

    public static boolean isSupported() {
        return !(handler instanceof UnsupportedNMSHandler);
    }
}