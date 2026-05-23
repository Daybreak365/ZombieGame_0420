package sinsa.zombie.config.base.wizard;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import sinsa.zombie.config.base.BaseConfig;
import sinsa.zombie.config.base.BaseNodes;
import sinsa.zombie.game.GamePhase;
import sinsa.zombie.utils.compat.MaterialX;
import sinsa.zombie.utils.minecraft.CustomGUI;
import sinsa.zombie.utils.minecraft.item.ItemBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

public class BaseConfigWizard extends CustomGUI {

	private final ItemStack ITEM_PHASE_PRE_GAME = new ItemBuilder(MaterialX.CLOCK).displayName("§b선정 시간").build();
	private final ItemStack ITEM_PHASE_DAYTIME = new ItemBuilder(MaterialX.CLOCK).displayName("§b낮 시간").build();
	private final ItemStack ITEM_PHASE_SUNSET = new ItemBuilder(MaterialX.CLOCK).displayName("§b해질녘 시간").build();
	private final ItemStack ITEM_PHASE_NIGHT = new ItemBuilder(MaterialX.CLOCK).displayName("§b밤 시간").build();
	private final ItemStack ITEM_PHASE_END = new ItemBuilder(MaterialX.CLOCK).displayName("§b최후 시간").build();
	private final ItemStack ITEM_COUNT_INITIAL_ZOMBIE = new ItemBuilder(MaterialX.ZOMBIE_HEAD).displayName("§b최초 좀비 수").build();
	private final ItemStack ITEM_COUNT_HERO = new ItemBuilder(MaterialX.DIAMOND_SWORD).displayName("§b영웅 수").build();
	private final ItemStack ITEM_COUNT_VACCINE = new ItemBuilder(MaterialX.EMERALD).displayName("§b백신 수").build();
	private final ItemStack ITEM_HEALTH_SURVIVOR = new ItemBuilder(MaterialX.APPLE).displayName("§b생존자 체력").build();
	private final ItemStack ITEM_HEALTH_HERO = new ItemBuilder(MaterialX.GOLDEN_APPLE).displayName("§b영웅 체력").build();
	private final ItemStack ITEM_HEALTH_INITIAL_ZOMBIE = new ItemBuilder(MaterialX.ROTTEN_FLESH).displayName("§b최초 좀비 체력").build();
	private final ItemStack ITEM_HEALTH_INFECTEE = new ItemBuilder(MaterialX.SPIDER_EYE).displayName("§b감염자 체력").build();
	private final ItemStack ITEM_ZOMBIE_BUFF_DAMAGE = new ItemBuilder(MaterialX.STONE_SWORD).displayName("§b좀비 공격력").build();
	private final ItemStack ITEM_ZOMBIE_BUFF_SPEED = new ItemBuilder(MaterialX.IRON_BOOTS).displayName("§b좀비 속도").build();
	private final ItemStack ITEM_HUMAN_BUFF_DAMAGE = new ItemBuilder(MaterialX.STONE_AXE).displayName("§b최후의 생존자 공격력").build();
	private final ItemStack ITEM_HUMAN_BUFF_SPEED = new ItemBuilder(MaterialX.DIAMOND_BOOTS).displayName("§b최후의 생존자 속도").build();
	private final ItemStack ITEM_ZOMBIE_INVENTORY_CLEAR = new ItemBuilder(MaterialX.BARRIER).displayName("§b좀비 리스폰시 인벤 초기화").build();

	public BaseConfigWizard(Player player, Plugin plugin) {
		super(player, 45, "§2§l콘피그", plugin);
	}

	@Override
	protected void openGUI(Inventory gui) {
		for (int i = 0; i <= 8; i++) gui.setItem(i, DECO);
		for (int i = 36; i <= 44; i++) gui.setItem(i, DECO);
		for (int i = 9; i < 45; i += 9) gui.setItem(i, DECO);
		for (int i = 17; i < 45; i += 9) gui.setItem(i, DECO);
		{
			final ItemMeta meta = ITEM_PHASE_PRE_GAME.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f게임 시작 전, 역할이 결정되는 시간.",
					"", "§b현재 값§7: §f" + BaseConfig.getPhaseLength(GamePhase.PRE_GAME) + "초", "",
					"§cSHIFT + 우클릭 §6» §e+ 10초",
					"§c우클릭         §6» §e+  1초",
					"§cSHIFT + 좌클릭 §6» §e- 10초",
					"§c좌클릭         §6» §e-  1초"
			));
			ITEM_PHASE_PRE_GAME.setItemMeta(meta);
			gui.setItem(10, ITEM_PHASE_PRE_GAME);
		}
		{
			final ItemMeta meta = ITEM_PHASE_DAYTIME.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f게임이 진행되는 시간.",
					"", "§b현재 값§7: §f" + BaseConfig.getPhaseLength(GamePhase.DAYTIME) + "초", "",
					"§cSHIFT + 우클릭 §6» §e+ 10초",
					"§c우클릭         §6» §e+  1초",
					"§cSHIFT + 좌클릭 §6» §e- 10초",
					"§c좌클릭         §6» §e-  1초"
			));
			ITEM_PHASE_DAYTIME.setItemMeta(meta);
			gui.setItem(11, ITEM_PHASE_DAYTIME);
		}
		{
			final ItemMeta meta = ITEM_PHASE_SUNSET.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f밤이 오기까지 대기하는 시간.",
					"", "§b현재 값§7: §f" + BaseConfig.getPhaseLength(GamePhase.SUNSET) + "초", "",
					"§cSHIFT + 우클릭 §6» §e+ 10초",
					"§c우클릭         §6» §e+  1초",
					"§cSHIFT + 좌클릭 §6» §e- 10초",
					"§c좌클릭         §6» §e-  1초"
			));
			ITEM_PHASE_SUNSET.setItemMeta(meta);
			gui.setItem(12, ITEM_PHASE_SUNSET);
		}
		{
			final ItemMeta meta = ITEM_PHASE_NIGHT.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f밤이 지속되는 시간.",
					"", "§b현재 값§7: §f" + BaseConfig.getPhaseLength(GamePhase.NIGHT) + "초", "",
					"§cSHIFT + 우클릭 §6» §e+ 10초",
					"§c우클릭         §6» §e+  1초",
					"§cSHIFT + 좌클릭 §6» §e- 10초",
					"§c좌클릭         §6» §e-  1초"
			));
			ITEM_PHASE_NIGHT.setItemMeta(meta);
			gui.setItem(13, ITEM_PHASE_NIGHT);
		}
		{
			final ItemMeta meta = ITEM_PHASE_END.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f밤 이후의 마지막 낮 시간.",
					"", "§b현재 값§7: §f" + BaseConfig.getPhaseLength(GamePhase.END) + "초", "",
					"§cSHIFT + 우클릭 §6» §e+ 10초",
					"§c우클릭         §6» §e+  1초",
					"§cSHIFT + 좌클릭 §6» §e- 10초",
					"§c좌클릭         §6» §e-  1초"
			));
			ITEM_PHASE_END.setItemMeta(meta);
			gui.setItem(29, ITEM_PHASE_END);
		}
		{
			final ItemMeta meta = ITEM_COUNT_INITIAL_ZOMBIE.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f최초 좀비로 선정되는 사람의 수.",
					"", "§b현재 값§7: §f" + BaseConfig.instance.get(BaseNodes.AMOUNT_INITIAL_ZOMBIE) + "명", "",
					"§cSHIFT + 우클릭 §6» §e+  5명",
					"§c우클릭         §6» §e+  1명",
					"§cSHIFT + 좌클릭 §6» §e-  5명",
					"§c좌클릭         §6» §e-  1명"
			));
			ITEM_COUNT_INITIAL_ZOMBIE.setItemMeta(meta);
			gui.setItem(14, ITEM_COUNT_INITIAL_ZOMBIE);
		}
		{
			final ItemMeta meta = ITEM_COUNT_HERO.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f영웅으로 선정되는 사람의 수.",
					"", "§b현재 값§7: §f" + BaseConfig.instance.get(BaseNodes.AMOUNT_HERO) + "명", "",
					"§cSHIFT + 우클릭 §6» §e+  5명",
					"§c우클릭         §6» §e+  1명",
					"§cSHIFT + 좌클릭 §6» §e-  5명",
					"§c좌클릭         §6» §e-  1명"
			));
			ITEM_COUNT_HERO.setItemMeta(meta);
			gui.setItem(15, ITEM_COUNT_HERO);
		}
		{
			final ItemMeta meta = ITEM_COUNT_VACCINE.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f백신이 지급될 사람의 수.",
					"", "§b현재 값§7: §f" + BaseConfig.instance.get(BaseNodes.AMOUNT_VACCINE) + "명", "",
					"§cSHIFT + 우클릭 §6» §e+  5명",
					"§c우클릭         §6» §e+  1명",
					"§cSHIFT + 좌클릭 §6» §e-  5명",
					"§c좌클릭         §6» §e-  1명"
			));
			ITEM_COUNT_VACCINE.setItemMeta(meta);
			gui.setItem(16, ITEM_COUNT_VACCINE);
		}
		{
			final ItemMeta meta = ITEM_HEALTH_SURVIVOR.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f생존자의 체력.",
					"", "§b현재 값§7: §f" + BaseConfig.instance.get(BaseNodes.HEALTH_SURVIVOR) + "§dhp", "",
					"§cSHIFT + 우클릭 §6» §e+  5hp",
					"§c우클릭         §6» §e+  1hp",
					"§cSHIFT + 좌클릭 §6» §e-  5hp",
					"§c좌클릭         §6» §e-  1hp"
			));
			ITEM_HEALTH_SURVIVOR.setItemMeta(meta);
			gui.setItem(19, ITEM_HEALTH_SURVIVOR);
		}
		{
			final ItemMeta meta = ITEM_HEALTH_HERO.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f영웅의 체력.",
					"", "§b현재 값§7: §f" + BaseConfig.instance.get(BaseNodes.HEALTH_HERO) + "§dhp", "",
					"§cSHIFT + 우클릭 §6» §e+  5hp",
					"§c우클릭         §6» §e+  1hp",
					"§cSHIFT + 좌클릭 §6» §e-  5hp",
					"§c좌클릭         §6» §e-  1hp"
			));
			ITEM_HEALTH_HERO.setItemMeta(meta);
			gui.setItem(20, ITEM_HEALTH_HERO);
		}
		{
			final ItemMeta meta = ITEM_HEALTH_INITIAL_ZOMBIE.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f최초 좀비의 체력.",
					"", "§b현재 값§7: §f" + BaseConfig.instance.get(BaseNodes.HEALTH_INITIAL_ZOMBIE) + "§dhp", "",
					"§cSHIFT + 우클릭 §6» §e+  5hp",
					"§c우클릭         §6» §e+  1hp",
					"§cSHIFT + 좌클릭 §6» §e-  5hp",
					"§c좌클릭         §6» §e-  1hp"
			));
			ITEM_HEALTH_INITIAL_ZOMBIE.setItemMeta(meta);
			gui.setItem(21, ITEM_HEALTH_INITIAL_ZOMBIE);
		}
		{
			final ItemMeta meta = ITEM_HEALTH_INFECTEE.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f감염자의 체력.",
					"", "§b현재 값§7: §f" + BaseConfig.instance.get(BaseNodes.HEALTH_INFECTEE) + "§dhp", "",
					"§cSHIFT + 우클릭 §6» §e+  5hp",
					"§c우클릭         §6» §e+  1hp",
					"§cSHIFT + 좌클릭 §6» §e-  5hp",
					"§c좌클릭         §6» §e-  1hp"
			));
			ITEM_HEALTH_INFECTEE.setItemMeta(meta);
			gui.setItem(22, ITEM_HEALTH_INFECTEE);
		}
		{
			final ItemMeta meta = ITEM_ZOMBIE_BUFF_DAMAGE.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f밤이 됐을 때 좀비의 추가 근접 공격력.",
					"", "§b현재 값§7: §f" + (100 + BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_DAMAGE)) + "%", "",
					"§cSHIFT + 우클릭 §6» §e+  5%p",
					"§c우클릭         §6» §e+  1%p",
					"§cSHIFT + 좌클릭 §6» §e-  5%p",
					"§c좌클릭         §6» §e-  1%p"
			));
			ITEM_ZOMBIE_BUFF_DAMAGE.setItemMeta(meta);
			gui.setItem(23, ITEM_ZOMBIE_BUFF_DAMAGE);
		}
		{
			final ItemMeta meta = ITEM_ZOMBIE_BUFF_SPEED.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f밤이 됐을 때 좀비의 추가 이동 속도.",
					"", "§b현재 값§7: §f" + (100 + BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_SPEED)) + "%", "",
					"§cSHIFT + 우클릭 §6» §e+  5%p",
					"§c우클릭         §6» §e+  1%p",
					"§cSHIFT + 좌클릭 §6» §e-  5%p",
					"§c좌클릭         §6» §e-  1%p"
			));
			ITEM_ZOMBIE_BUFF_SPEED.setItemMeta(meta);
			gui.setItem(24, ITEM_ZOMBIE_BUFF_SPEED);
		}
		{
			final ItemMeta meta = ITEM_HUMAN_BUFF_DAMAGE.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f최후의 생존자만 남았을 때 추가 근접 공격력.",
					"", "§b현재 값§7: §f" + (100 + BaseConfig.getInt(BaseNodes.BUFF_HUMAN_DAMAGE)) + "%", "",
					"§cSHIFT + 우클릭 §6» §e+  5%p",
					"§c우클릭         §6» §e+  1%p",
					"§cSHIFT + 좌클릭 §6» §e-  5%p",
					"§c좌클릭         §6» §e-  1%p"
			));
			ITEM_HUMAN_BUFF_DAMAGE.setItemMeta(meta);
			gui.setItem(25, ITEM_HUMAN_BUFF_DAMAGE);
		}
		{
			final ItemMeta meta = ITEM_HUMAN_BUFF_SPEED.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f최후의 생존자만 남았을 때 추가 이동 속도.",
					"", "§b현재 값§7: §f" + (100 + BaseConfig.getInt(BaseNodes.BUFF_HUMAN_SPEED)) + "%", "",
					"§cSHIFT + 우클릭 §6» §e+  5%p",
					"§c우클릭         §6» §e+  1%p",
					"§cSHIFT + 좌클릭 §6» §e-  5%p",
					"§c좌클릭         §6» §e-  1%p"
			));
			ITEM_HUMAN_BUFF_SPEED.setItemMeta(meta);
			gui.setItem(28, ITEM_HUMAN_BUFF_SPEED);
		}
		{
			final ItemMeta meta = ITEM_ZOMBIE_INVENTORY_CLEAR.getItemMeta();
			meta.setLore(Arrays.asList(
					"§f좀비가 사망할경우, 리스폰 후 인벤토리 초기화할지의 여부",
					"", "§b현재 설정§7: §f인벤토리" + (BaseConfig.getBoolean(BaseNodes.ZOMBIE_INVENTORY_CLEAR) ? "§a초기화" : "§c유지"), ""
			));
			ITEM_ZOMBIE_INVENTORY_CLEAR.setItemMeta(meta);
			gui.setItem(30, ITEM_ZOMBIE_INVENTORY_CLEAR);
		}

		player.openInventory(gui);
	}

	@Override
	protected void onClick(InventoryClickEvent e, Inventory gui) {
		e.setCancelled(true);
		ItemStack currentItem = e.getCurrentItem();
		if (currentItem != null) {
			switch (e.getSlot()) {
				case 10:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_PRE_GAME, BaseConfig.getPhaseLength(GamePhase.PRE_GAME) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_PRE_GAME, BaseConfig.getPhaseLength(GamePhase.PRE_GAME) + 10);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_PRE_GAME, Math.max(1, BaseConfig.getPhaseLength(GamePhase.PRE_GAME) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_PRE_GAME, Math.max(1, BaseConfig.getPhaseLength(GamePhase.PRE_GAME) - 10));
							show();
							break;
					}
					break;
				case 11:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_DAYTIME, BaseConfig.getPhaseLength(GamePhase.DAYTIME) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_DAYTIME, BaseConfig.getPhaseLength(GamePhase.DAYTIME) + 10);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_DAYTIME, Math.max(1, BaseConfig.getPhaseLength(GamePhase.DAYTIME) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_DAYTIME, Math.max(1, BaseConfig.getPhaseLength(GamePhase.DAYTIME) - 10));
							show();
							break;
					}
					break;
				case 12:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_SUNSET, BaseConfig.getPhaseLength(GamePhase.SUNSET) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_SUNSET, BaseConfig.getPhaseLength(GamePhase.SUNSET) + 10);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_SUNSET, Math.max(1, BaseConfig.getPhaseLength(GamePhase.SUNSET) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_SUNSET, Math.max(1, BaseConfig.getPhaseLength(GamePhase.SUNSET) - 10));
							show();
							break;
					}
					break;
				case 13:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_NIGHT, BaseConfig.getPhaseLength(GamePhase.NIGHT) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_NIGHT, BaseConfig.getPhaseLength(GamePhase.NIGHT) + 10);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_NIGHT, Math.max(1, BaseConfig.getPhaseLength(GamePhase.NIGHT) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_NIGHT, Math.max(1, BaseConfig.getPhaseLength(GamePhase.NIGHT) - 10));
							show();
							break;
					}
					break;
				case 29:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_END, BaseConfig.getPhaseLength(GamePhase.END) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_END, BaseConfig.getPhaseLength(GamePhase.END) + 10);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_END, Math.max(1, BaseConfig.getPhaseLength(GamePhase.END) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.PHASE_LENGTH_END, Math.max(1, BaseConfig.getPhaseLength(GamePhase.END) - 10));
							show();
							break;
					}
					break;
				case 14:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_INITIAL_ZOMBIE, BaseConfig.getInt(BaseNodes.AMOUNT_INITIAL_ZOMBIE) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_INITIAL_ZOMBIE, BaseConfig.getInt(BaseNodes.AMOUNT_INITIAL_ZOMBIE) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_INITIAL_ZOMBIE, Math.max(1, BaseConfig.getInt(BaseNodes.AMOUNT_INITIAL_ZOMBIE) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_INITIAL_ZOMBIE, Math.max(1, BaseConfig.getInt(BaseNodes.AMOUNT_INITIAL_ZOMBIE) - 5));
							show();
							break;
					}
					break;
				case 15:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_HERO, BaseConfig.getInt(BaseNodes.AMOUNT_HERO) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_HERO, BaseConfig.getInt(BaseNodes.AMOUNT_HERO) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_HERO, Math.max(1, BaseConfig.getInt(BaseNodes.AMOUNT_HERO) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_HERO, Math.max(1, BaseConfig.getInt(BaseNodes.AMOUNT_HERO) - 5));
							show();
							break;
					}
					break;
				case 16:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_VACCINE, BaseConfig.getInt(BaseNodes.AMOUNT_VACCINE) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_VACCINE, BaseConfig.getInt(BaseNodes.AMOUNT_VACCINE) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_VACCINE, Math.max(1, BaseConfig.getInt(BaseNodes.AMOUNT_VACCINE) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.AMOUNT_VACCINE, Math.max(1, BaseConfig.getInt(BaseNodes.AMOUNT_VACCINE) - 5));
							show();
							break;
					}
					break;
				case 19:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_SURVIVOR, BaseConfig.getInt(BaseNodes.HEALTH_SURVIVOR) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_SURVIVOR, BaseConfig.getInt(BaseNodes.HEALTH_SURVIVOR) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_SURVIVOR, Math.max(1, BaseConfig.getInt(BaseNodes.HEALTH_SURVIVOR) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_SURVIVOR, Math.max(1, BaseConfig.getInt(BaseNodes.HEALTH_SURVIVOR) - 5));
							show();
							break;
					}
					break;
				case 20:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_HERO, BaseConfig.getInt(BaseNodes.HEALTH_HERO) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_HERO, BaseConfig.getInt(BaseNodes.HEALTH_HERO) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_HERO, Math.max(1, BaseConfig.getInt(BaseNodes.HEALTH_HERO) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_HERO, Math.max(1, BaseConfig.getInt(BaseNodes.HEALTH_HERO) - 5));
							show();
							break;
					}
					break;
				case 21:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_INITIAL_ZOMBIE, BaseConfig.getInt(BaseNodes.HEALTH_INITIAL_ZOMBIE) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_INITIAL_ZOMBIE, BaseConfig.getInt(BaseNodes.HEALTH_INITIAL_ZOMBIE) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_INITIAL_ZOMBIE, Math.max(1, BaseConfig.getInt(BaseNodes.HEALTH_INITIAL_ZOMBIE) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_INITIAL_ZOMBIE, Math.max(1, BaseConfig.getInt(BaseNodes.HEALTH_INITIAL_ZOMBIE) - 5));
							show();
							break;
					}
					break;
				case 22:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_INFECTEE, BaseConfig.getInt(BaseNodes.HEALTH_INFECTEE) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_INFECTEE, BaseConfig.getInt(BaseNodes.HEALTH_INFECTEE) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_INFECTEE, Math.max(1, BaseConfig.getInt(BaseNodes.HEALTH_INFECTEE) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.HEALTH_INFECTEE, Math.max(1, BaseConfig.getInt(BaseNodes.HEALTH_INFECTEE) - 5));
							show();
							break;
					}
					break;
				case 23:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_ZOMBIE_DAMAGE, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_DAMAGE) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_ZOMBIE_DAMAGE, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_DAMAGE) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_ZOMBIE_DAMAGE, Math.max(0, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_DAMAGE) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_ZOMBIE_DAMAGE, Math.max(0, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_DAMAGE) - 5));
							show();
							break;
					}
					break;
				case 24:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_ZOMBIE_SPEED, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_SPEED) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_ZOMBIE_SPEED, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_SPEED) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_ZOMBIE_SPEED, Math.max(0, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_SPEED) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_ZOMBIE_SPEED, Math.max(0, BaseConfig.getInt(BaseNodes.BUFF_ZOMBIE_SPEED) - 5));
							show();
							break;
					}
					break;
				case 25:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_HUMAN_DAMAGE, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_DAMAGE) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_HUMAN_DAMAGE, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_DAMAGE) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_HUMAN_DAMAGE, Math.max(0, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_DAMAGE) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_HUMAN_DAMAGE, Math.max(0, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_DAMAGE) - 5));
							show();
							break;
					}
					break;
				case 28:
					switch (e.getClick()) {
						case RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_HUMAN_SPEED, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_SPEED) + 1);
							show();
							break;
						case SHIFT_RIGHT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_HUMAN_SPEED, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_SPEED) + 5);
							show();
							break;
						case LEFT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_HUMAN_SPEED, Math.max(0, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_SPEED) - 1));
							show();
							break;
						case SHIFT_LEFT:
							BaseConfig.instance.setProperty(BaseNodes.BUFF_HUMAN_SPEED, Math.max(0, BaseConfig.getInt(BaseNodes.BUFF_HUMAN_SPEED) - 5));
							show();
							break;
					}
					break;
				case 30:
					BaseConfig.instance.setProperty(BaseNodes.ZOMBIE_INVENTORY_CLEAR, !BaseConfig.getBoolean(BaseNodes.ZOMBIE_INVENTORY_CLEAR));
					show();
					break;
			}
		}
	}

	@Override
	protected void onUnregister(Inventory gui) {
		try {
			BaseConfig.instance.update();
		} catch (IOException | InvalidConfigurationException e1) {
			logger.log(Level.SEVERE, "콘피그를 업데이트하는 도중 오류가 발생하였습니다.");
		}
	}

}
