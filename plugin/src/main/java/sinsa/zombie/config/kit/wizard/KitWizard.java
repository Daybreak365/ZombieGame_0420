package sinsa.zombie.config.kit.wizard;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import sinsa.zombie.config.kit.KitNodes;
import sinsa.zombie.config.kit.Kits;
import sinsa.zombie.utils.compat.MaterialX;
import sinsa.zombie.utils.logging.LogType;
import sinsa.zombie.utils.minecraft.CustomGUI;
import sinsa.zombie.utils.minecraft.item.ItemBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitWizard extends CustomGUI {

    private static final int KIT_SIZE = 36;
    private static final int ARMOR_SIZE = 4;
    private static final int ARMOR_START = KIT_SIZE;
    private static final int GUI_SIZE = 45;

    private final KitNodes node;

    public KitWizard(Player player, KitNodes node, Plugin plugin) {
        super(player, GUI_SIZE, "§2§l아이템/방어구를 설정하세요. §0§l(§8§l" + node.name() + "§0§l)§r", plugin);
        this.node = node;
    }

    @Override
    protected void openGUI(Inventory gui) {
        int slot = 0;
        for (ItemStack itemStack : Kits.getKit(node)) {
            if (slot >= KIT_SIZE) break;
            gui.setItem(slot++, itemStack);
        }

        slot = ARMOR_START;
        for (ItemStack itemStack : Kits.getArmorKit(node)) {
            if (slot >= ARMOR_START + ARMOR_SIZE) break;
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                gui.setItem(slot, itemStack);
            }
            slot++;
        }

        fillDecoration(gui);
        fillArmorPlaceholders(gui);

        player.sendMessage("§7상단 36칸은 지급 아이템, 하단 첫 4칸은 방어구(신발/레깅스/흉갑/투구)입니다.");
        player.openInventory(gui);
    }

    @Override
    protected void onClick(InventoryClickEvent e, Inventory gui) {
        if (e.getClickedInventory() == null) return;
        if (!e.getInventory().equals(gui)) return;

        int slot = e.getRawSlot();
        if (slot < KIT_SIZE || slot >= GUI_SIZE) {
            return;
        }

        e.setCancelled(true);
        if (slot < ARMOR_START || slot >= ARMOR_START + ARMOR_SIZE) {
            return;
        }

        ItemStack current = gui.getItem(slot);
        ItemStack cursor = e.getCursor();

        if (isArmorPlaceholder(current)) {
            if (cursor != null && cursor.getType() != Material.AIR) {
                gui.setItem(slot, cursor.clone());
                e.setCursor(null);
            }
            return;
        }

        if (current != null && current.getType() != Material.AIR) {
            if (cursor == null || cursor.getType() == Material.AIR) {
                e.setCursor(current.clone());
                gui.setItem(slot, createArmorPlaceholder(slot));
                return;
            }

            gui.setItem(slot, cursor.clone());
            e.setCursor(current.clone());
        }
    }

    @Override
    protected void onUnregister(Inventory gui) {
        try {
            Kits.setKit(node, compact(Arrays.asList(gui.getContents()).subList(0, KIT_SIZE)));
            Kits.setArmorKit(node, collectArmorSlots(gui));
            Kits.instance.update();
            player.sendMessage("§2§l" + node.name() + " 킷/방어구 설정 완료!");
        } catch (IOException | InvalidConfigurationException e) {
            logger.log(LogType.ERROR, "킷을 저장하는 도중 오류가 발생헀습니다.");
        }
    }

    private List<ItemStack> collectArmorSlots(Inventory gui) {
        List<ItemStack> armorSlots = new ArrayList<>(ARMOR_SIZE);
        for (int i = 0; i < ARMOR_SIZE; i++) {
            ItemStack item = gui.getItem(ARMOR_START + i);
            armorSlots.add(item == null || item.getType() == Material.AIR || isArmorPlaceholder(item) ? null : item);
        }
        return armorSlots;
    }

    private void fillDecoration(Inventory gui) {
        ItemStack deco = new ItemBuilder(MaterialX.BLACK_STAINED_GLASS_PANE)
                .displayName("§8")
                .lore("§7방어구 슬롯을 선택해 장착/해제하세요.")
                .build();

        for (int i = KIT_SIZE; i < GUI_SIZE; i++) {
            if (i < ARMOR_START || i >= ARMOR_START + ARMOR_SIZE) {
                gui.setItem(i, deco);
            }
        }
    }

    private List<ItemStack> compact(List<ItemStack> items) {
        List<ItemStack> compacted = new ArrayList<>();
        for (ItemStack item : items) {
            compacted.add(item == null || item.getType() == Material.AIR || isArmorPlaceholder(item) ? null : item);
        }
        return compacted;
    }

    private void fillArmorPlaceholders(Inventory gui) {
        for (int i = ARMOR_START; i < ARMOR_START + ARMOR_SIZE; i++) {
            ItemStack current = gui.getItem(i);
            if (current == null || current.getType() == Material.AIR) {
                gui.setItem(i, createArmorPlaceholder(i));
            }
        }
    }

    private ItemStack createArmorPlaceholder(int slot) {
        return new ItemBuilder(MaterialX.LIGHT_BLUE_STAINED_GLASS_PANE)
                .displayName("§b§l방어구 슬롯: " + armorSlotName(slot - ARMOR_START))
                .lore(
                        "§7여기를 클릭해서 방어구를 배치하세요.",
                        "§8- 클릭한 상태의 아이템이 등록됩니다.",
                        "§8- 등록된 방어구를 클릭하면 제거됩니다."
                )
                .build();
    }

    private String armorSlotName(int index) {
        return switch (index) {
            case 0 -> "신발";
            case 1 -> "레깅스";
            case 2 -> "흉갑";
            case 3 -> "투구";
            default -> "알 수 없음";
        };
    }

    private boolean isArmorPlaceholder(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(MaterialX.LIGHT_BLUE_STAINED_GLASS_PANE.getMaterial())) return false;
        if (!item.hasItemMeta() || item.getItemMeta() == null || !item.getItemMeta().hasDisplayName()) return false;
        return item.getItemMeta().getDisplayName().startsWith("§b§l방어구 슬롯:");
    }
}
