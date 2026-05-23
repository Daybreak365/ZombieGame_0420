package utils.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class UnsupportedNMSHandler implements NMSHandler {
    private final Logger logger = Logger.getLogger("ZombieGame");

    public UnsupportedNMSHandler() {
        logger.warning("====================================================");
        logger.warning("Unsupported server version: " + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
        logger.warning("Some features may not work correctly.");
        logger.warning("====================================================");
    }

    @Override
    public void respawn(Player player) {
        // Not supported
    }
}