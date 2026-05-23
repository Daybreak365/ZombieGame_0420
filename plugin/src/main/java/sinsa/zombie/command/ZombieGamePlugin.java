package sinsa.zombie;

import org.bukkit.plugin.java.JavaPlugin;
import sinsa.zombie.api.ZombieGameApiImpl;
import sinsa.zombie.command.ZBCommand;
import sinsa.zombie.config.FriendlyGlowSettings;
import sinsa.zombie.config.TeamDisplaySettings;
import sinsa.zombie.game.team.FriendlyGlowService;
import sinsa.zombie.game.team.TeamDisplayService;

public class ZombieGamePlugin extends JavaPlugin {

    private final ZombieGameApiImpl api = new ZombieGameApiImpl();
    private final TeamDisplaySettings teamDisplaySettings = new TeamDisplaySettings();
    private final FriendlyGlowSettings friendlyGlowSettings = new FriendlyGlowSettings();

    private TeamDisplayService teamDisplayService;
    private FriendlyGlowService friendlyGlowService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSystems();

        getCommand("zb").setExecutor(new ZBCommand(this));
        friendlyGlowService.start();
    }

    public void reloadGameConfig() {
        reloadConfig();
        loadSystems();
        teamDisplayService.updateAll();
    }

    private void loadSystems() {
        teamDisplaySettings.load(getConfig());
        friendlyGlowSettings.load(getConfig());

        teamDisplayService = new TeamDisplayService(api, teamDisplaySettings);
        friendlyGlowService = new FriendlyGlowService(this, api, friendlyGlowSettings);
    }

    public ZombieGameApiImpl getApi() {
        return api;
    }

    public TeamDisplayService getTeamDisplayService() {
        return teamDisplayService;
    }
}