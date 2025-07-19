package me.aaaaadam.hearthstone.kit;

import me.aaaaadam.hearthstone.Hearthstone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class KitUI {

	private Hearthstone hearthstone;
	private NamespacedKey kitKey;

	public KitUI(Player player, Hearthstone hearthstone) {
		this.hearthstone = hearthstone;
		this.kitKey = new NamespacedKey(hearthstone, "kit_type");
		Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GRAY + "Kit Selection");

		for (KitType type : KitType.values()) {
			ItemStack is = new ItemStack(type.getMaterial());
			ItemMeta isMeta = is.getItemMeta();
			isMeta.setDisplayName(type.getDisplay());
			isMeta.setLore(Arrays.asList(type.getDescription()));

			isMeta.getPersistentDataContainer().set(kitKey, PersistentDataType.STRING, type.name());

			is.setItemMeta(isMeta);
			gui.addItem(is);
		}

		player.openInventory(gui);
	}
}