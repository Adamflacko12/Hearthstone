package me.aaaaadam.hearthstone.kit.type;

import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.kit.Kit;
import me.aaaaadam.hearthstone.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.mineacademy.fo.remain.CompSound;

import java.util.UUID;

public class StrikerKit extends Kit {

	private Hearthstone hearthstone;

	public StrikerKit(Hearthstone hearthstone, UUID uuid) {
		super(hearthstone, KitType.STRIKER, uuid);
		this.hearthstone = hearthstone;
	}

	@Override
	public void onStart(Player player) {
		player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
	}

	@EventHandler
	public void onBlockClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_AIR)
			return;

		Player player = e.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		if (item == null || item.getType() != Material.IRON_AXE) {
			return;
		}

		// Only allow the kit owner to use this ability
		if (!player.getUniqueId().equals(uuid)) {
			return;
		}

		Wolf wolf = player.getWorld().spawn(player.getEyeLocation(), Wolf.class);

		wolf.setBaby();
		wolf.setAngry(true);
		wolf.setVelocity(player.getEyeLocation().getDirection().multiply(2.0));
		CompSound.ENTITY_WOLF_ANGRY_GROWL.play(player);

		// Add metadata to track the owner
		wolf.setMetadata("thrower", new FixedMetadataValue(hearthstone, player.getUniqueId().toString()));
		wolf.setMetadata("projectile", new FixedMetadataValue(hearthstone, true));

		// Damage detection task
		BukkitTask task = Bukkit.getScheduler().runTaskTimer(hearthstone, () -> {
			if (wolf.isDead()) return;

			for (Player nearby : wolf.getWorld().getPlayers()) {
				if (nearby.getUniqueId().toString().equals(wolf.getMetadata("thrower").get(0).asString())) {
					continue; // Don't damage thrower
				}

				if (nearby.getLocation().distance(wolf.getLocation()) <= 1.5) {
					// Deal damage
					nearby.damage(4.0);

					CompSound.ENTITY_PLAYER_HURT.play(nearby);
					String throwerName = Bukkit.getPlayer(UUID.fromString(
							wolf.getMetadata("thrower").get(0).asString())).getName();
					nearby.sendMessage(ChatColor.RED + "Hit by " + throwerName + "'s wolf!");

					wolf.remove();
					return;
				}
			}
		}, 0L, 1L);

		// Auto-remove after 5 seconds
		Bukkit.getScheduler().runTaskLater(hearthstone, () -> {
			if (!wolf.isDead()) {
				wolf.remove();
			}
			task.cancel();
		}, 100L);
	}
}