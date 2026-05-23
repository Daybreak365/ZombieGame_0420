package sinsa.zombie.stat;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import sinsa.zombie.utils.minecraft.CustomGUI;
import sinsa.zombie.utils.minecraft.item.ItemBuilder;

public class StatGUI extends CustomGUI {

    private final StatView statView;

    public StatGUI(Player player, Player target, Plugin plugin) {
        super(player, 45, "§3§l" + target.getName() + " 업적", plugin);
        this.statView = StatView.get(target);
    }

    public StatGUI(Player player, Plugin plugin) {
        this(player, player, plugin);
    }

    @Override
    protected void openGUI(Inventory gui) {
        for (int i = 0; i <= 8; i++) gui.setItem(i, DECO);
        for (int i = 36; i <= 44; i++) gui.setItem(i, DECO);
        for (int i = 9; i < 45; i += 9) gui.setItem(i, DECO);
        for (int i = 17; i < 45; i += 9) gui.setItem(i, DECO);

        for (Stats stats : Stats.values()) {
            gui.addItem(
                    new ItemBuilder(stats.getMaterial())
                            .displayName("§b" + stats.getDisplay())
                            .lore(
                                    "§f" + stats.getDescription(), "",
                                    "§3§l▶ §f§l" + statView.getValue(stats)
                            )
                            .build()
            );
        }

        player.openInventory(gui);
    }

    @Override
    protected void onClick(InventoryClickEvent e, Inventory gui) {
        e.setCancelled(true);
    }

    @Override
    protected void onUnregister(Inventory gui) {

    }
}
