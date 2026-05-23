package sinsa.zombie;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import sinsa.zombie.api.ZombiePlaceholderExpansion;
import sinsa.zombie.command.Commands;
import sinsa.zombie.config.base.BaseConfig;
import sinsa.zombie.config.command.CommandConfig;
import sinsa.zombie.config.text.TextNodes;
import sinsa.zombie.config.text.Texts;
import sinsa.zombie.game.Game;
import sinsa.zombie.game.GameManager;
import sinsa.zombie.stat.StatView;
import sinsa.zombie.utils.logging.Logger;
import utils.minecraft.NMS;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;

public class ZombieGame extends JavaPlugin implements Listener {

    private static boolean compatVersion() {
        final ZombieGame plugin = getInstance();
        try {
            String serverVersion = Bukkit.getBukkitVersion().split("-")[0];
            if (Integer.parseInt(serverVersion.split("\\.")[1]) >= 13) {
                Field apiVersionField = PluginDescriptionFile.class.getDeclaredField("apiVersion");
                apiVersionField.setAccessible(true);
                apiVersionField.set(plugin.getDescription(), serverVersion);
                apiVersionField.setAccessible(false);

            }
            if (!NMS.isSupported()) {
                logger.log(Level.SEVERE, "지원되지 않는 버전입니다.");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "플러그인 버전 호환성 설정 중 오류가 발생했습니다.", e);
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }

    private static final Logger logger = Logger.getLogger(ZombieGame.class);
    private static ZombieGame instance;

    @NotNull
    public static ZombieGame getInstance() {
        if (instance == null) throw new RuntimeException("plugin is not initialized");
        return instance;
    }

    private final Commands commands = new Commands(this);

    public ZombieGame() {
        if (ZombieGame.instance != null) throw new RuntimeException("plugin is already initialized");
        ZombieGame.instance = this;
    }

    public Commands getCommands() {
        return commands;
    }

    @Override
    public void onEnable() {
        if (!compatVersion()) return;
        if (BaseConfig.isLoaded()) logger.debug("콘피그를 정상적으로 불러왔습니다.");
        else logger.error("콘피그를 불러오는 중 오류가 발생했습니다.");

        if (Texts.isLoaded()) logger.debug("텍스트 목록을 정상적으로 불러왔습니다.");
        else logger.error("텍스트 목록을 불러오는 중 오류가 발생했습니다.");

        if (CommandConfig.isLoaded()) logger.debug("명령어 설정을 정상적으로 불러왔습니다.");
        else logger.error("명령어 설정을 불러오는 중 오류가 발생했습니다.");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ZombiePlaceholderExpansion(this).register();
            logger.debug("PlaceholderAPI 연동을 활성화했습니다.");
        }

        getCommand("zombie").setExecutor(commands);
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getConsoleSender().sendMessage(Texts.text(TextNodes.PREFIX) + "플러그인이 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        try {
            BaseConfig.instance.update();
            StatView.saveAll();
        } catch (IOException | InvalidConfigurationException e) {
            logger.error("콘피그 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage(Texts.text(TextNodes.PREFIX) + "플러그인이 비활성화되었습니다.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (GameManager.isPendingReset(event.getPlayer().getUniqueId())) {
            Game.resetPlayerAfterGame(event.getPlayer());
            GameManager.clearPendingReset(event.getPlayer().getUniqueId());
        }
    }

}
