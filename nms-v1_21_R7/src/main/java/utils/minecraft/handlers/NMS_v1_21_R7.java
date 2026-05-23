package utils.minecraft.handlers;

import net.minecraft.network.protocol.game.PacketPlayInClientCommand;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.Player;
import utils.minecraft.NMSHandler;

public class NMS_v1_21_R7 implements NMSHandler {

    @Override
    public void respawn(Player player) {
        ((CraftPlayer) player).getHandle().g.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.a));
    }
}
