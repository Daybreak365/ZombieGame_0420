package utils.minecraft;

import org.bukkit.Bukkit;

public class NMSFactory {

    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getName().replace('.', ',').split(",")[3];

    public static NMSHandler createNMSHandler() {
        try {
            int version = Integer.parseInt(Bukkit.getVersion().split(" ")[0].split("-")[0].split("\\.")[1]);
            if (version >= 21) {
                return (NMSHandler) Class.forName("utils.minecraft.handlers.NMS_v1_21_R7").getDeclaredConstructor().newInstance();
            }
            Class<?> clazz = Class.forName("utils.minecraft.handlers.NMS_" + SERVER_VERSION);
            return (NMSHandler) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return new UnsupportedNMSHandler();
        }
    }
}
