package sinsa.zombie.utils.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import sinsa.zombie.utils.compat.MaterialX;
import sinsa.zombie.utils.logging.Logger;
import sinsa.zombie.utils.minecraft.item.ItemBuilder;

public abstract class CustomGUI {

	protected static final Logger logger = Logger.getLogger(CustomGUI.class.getName());

	protected static final ItemStack DECO = new ItemBuilder(MaterialX.WHITE_STAINED_GLASS_PANE)
			.displayName(ChatColor.WHITE.toString())
			.build();

	private Inventory gui = null;
	private final int inventorySize;
	private final String inventoryName;
	protected final Player player;

	protected CustomGUI(final Player player, int inventorySize, String inventoryName, Plugin plugin) {
		this.inventorySize = inventorySize;
		this.inventoryName = inventoryName;
		this.player = player;
		new Listener() {
			{
				Bukkit.getPluginManager().registerEvents(this, plugin);
			}

			@EventHandler
			private void onInventoryClose(InventoryCloseEvent e) {
				if (e.getInventory().equals(gui)) {
					HandlerList.unregisterAll(this);
					onUnregister(gui);
				}
			}

			@EventHandler
			private void onQuit(PlayerQuitEvent e) {
				if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
					HandlerList.unregisterAll(this);
					onUnregister(gui);
				}
			}

			@EventHandler
			private void onInventoryClick(InventoryClickEvent e) {
				if (e.getInventory().equals(gui)) {
					onClick(e, gui);
				}
			}

		};
	}

	public void show() {
		this.gui = Bukkit.createInventory(null, inventorySize, inventoryName);
		openGUI(gui);
	}

	protected abstract void openGUI(Inventory gui);
	protected abstract void onClick(InventoryClickEvent e, Inventory gui);
	protected abstract void onUnregister(final Inventory gui);

}
