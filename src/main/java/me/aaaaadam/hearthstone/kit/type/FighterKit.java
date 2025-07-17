package me.aaaaadam.hearthstone.kit.type;

import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.kit.Kit;
import me.aaaaadam.hearthstone.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class FighterKit extends Kit {

	public FighterKit(Hearthstone hearthstone, UUID uuid) {
		super(hearthstone, KitType.FIGHTER, uuid);
	}

	@Override
	public void onStart(Player player) {
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
		player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 10, 100));

	}
}
