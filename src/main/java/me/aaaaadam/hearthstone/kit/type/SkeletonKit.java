package me.aaaaadam.hearthstone.kit.type;

import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.kit.Kit;
import me.aaaaadam.hearthstone.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompSound;

import java.util.UUID;

public class SkeletonKit extends Kit {

	private Hearthstone hearthstone;

	public SkeletonKit(Hearthstone hearthstone, UUID uuid) {
		super(hearthstone, KitType.SKELETON, uuid);
		this.hearthstone = hearthstone;
	}

	@Override
	public void onStart(Player player) {
		player.getInventory().addItem(new ItemStack(Material.IRON_HOE));
	}

	@EventHandler
	public void onBlockClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_AIR)
			return;

		Player player = e.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		if (item == null || item.getType() != Material.IRON_HOE) {
			return;
		}

		// Only allow the kit owner to use this ability
		if (!player.getUniqueId().equals(uuid)) {
			return;
		}

		Arrow arrow = player.getWorld().spawn(player.getEyeLocation(), Arrow.class);

		arrow.setVelocity(player.getEyeLocation().getDirection().multiply(2.0));
		CompSound.ENTITY_SKELETON_SHOOT.play(player);
	}
}
