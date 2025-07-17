package me.aaaaadam.hearthstone.kit.type;

import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.kit.Kit;
import me.aaaaadam.hearthstone.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class MinerKit extends Kit {

	public MinerKit(Hearthstone hearthstone, UUID uuid) {
		super(hearthstone, KitType.MINER, uuid);
	}

	@Override
	public void onStart(Player player) {
		player.getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE));
		player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 10, 100));

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {

		if (uuid.equals(e.getPlayer().getUniqueId())) {
			System.out.println(e.getPlayer().getName() + " the Miner has broken a block!");
		}
	}
}