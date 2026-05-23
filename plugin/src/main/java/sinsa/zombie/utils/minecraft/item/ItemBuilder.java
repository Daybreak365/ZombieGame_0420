package sinsa.zombie.utils.minecraft.item;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import sinsa.zombie.utils.compat.MaterialX;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

	private final MaterialX type;
	private Boolean unbreakable = null;
	private Short durability = null;
	private int amount = 1;
	private String displayName;
	private List<String> lore;

	public ItemBuilder(@NotNull final MaterialX type) {
		this.type = Preconditions.checkNotNull(type, "Type cannot be null");
	}

	public ItemBuilder unbreakable(final boolean unbreakable) {
		this.unbreakable = unbreakable;
		return this;
	}

	public ItemBuilder durability(final short durability) {
		this.durability = durability;
		return this;
	}

	public ItemBuilder amount(final int amount) {
		Preconditions.checkArgument(amount > 0, "amount must be greater than 0");
		this.amount = amount;
		return this;
	}

	public ItemBuilder displayName(final String displayName) {
		this.displayName = displayName;
		return this;
	}

	public ItemBuilder lore(final String... lore) {
		this.lore = Arrays.asList(lore);
		return this;
	}

	public ItemBuilder lore(final List<String> lore) {
		this.lore = lore;
		return this;
	}

	public ItemBuilder emptyLore() {
		this.lore = null;
		return this;
	}

	public ItemStack build() {
		final ItemStack stack = type.createItem(amount);
		final ItemMeta meta = stack.getItemMeta();
		if (displayName != null) meta.setDisplayName(displayName);
		if (lore != null) meta.setLore(lore);
		if (unbreakable != null) meta.setUnbreakable(unbreakable);
		if (durability != null && sinsa.zombie.utils.compat.version.ServerVersion.INSTANCE.getVersion() < 13) {
			stack.setDurability(durability);
		}
		stack.setItemMeta(meta);
		return stack;
	}

}
