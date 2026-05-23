package sinsa.zombie;

import org.bukkit.command.CommandSender;
import sinsa.zombie.config.text.TextNodes;
import sinsa.zombie.config.text.Texts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Messages {

    private Messages() {}

    public static List<String> asList(String... strings) {
        return new ArrayList<>(Arrays.asList(strings));
    }

    public static void sendError(CommandSender sender, String str) {
        sender.sendMessage(Texts.text(TextNodes.ERROR_PREFIX) + str);
    }

}
