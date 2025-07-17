package me.aaaaadam.hearthstone.kit;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum KitType {

	MINER(ChatColor.GOLD + "Miner", Material.DIAMOND_PICKAXE, "The best mining kit!"),
	FIGHTER(ChatColor.RED + "Fighter", Material.IRON_SWORD, "the best fighting kit!");

	private String display, description;
	private Material material;

	KitType(String display, Material material, String description) {
		this.display = display;
		this.material = material;
		this.description = description;
	}

	public String getDisplay() { return display; }
	public Material getMaterial() { return material; }
	public String getDescription() { return description; }


}
