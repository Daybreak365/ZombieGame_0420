package sinsa.zombie.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import sinsa.zombie.ZombieGamePlugin;

public class ZBCommand implements CommandExecutor {

    private final ZombieGamePlugin plugin;

    public ZBCommand(ZombieGamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadGameConfig();
            sender.sendMessage("§a[ZombieGame] 설정을 다시 불러왔습니다.");
            return true;
        }

        sender.sendMessage("§c사용법: /zb reload");
        return true;
    }
}