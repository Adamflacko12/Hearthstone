package me.aaaaadam.hearthstone.listener;

import me.aaaaadam.hearthstone.GameState;
import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.instance.Arena;
import me.aaaaadam.hearthstone.instance.Game;
import me.aaaaadam.hearthstone.instance.Team;
import me.aaaaadam.hearthstone.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GameListener implements Listener {

	private Hearthstone hearthstone;

	public GameListener(Hearthstone hearthstone) {
		this.hearthstone = hearthstone;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {

		Arena arena = hearthstone.getArenaManager().getArena(e.getPlayer());
		if (arena != null && arena.getState().equals(GameState.LIVE)) {
			Game game = arena.getGame();
			if (e.getBlock().getType() == Material.RED_BED && e.getBlock().hasMetadata("team")) {
				e.setCancelled(game.destroyBed(Team.valueOf(e.getBlock().getMetadata("team").get(0).asString()), e.getPlayer()));
			}

		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {

		Arena arena = hearthstone.getArenaManager().getArena(e.getEntity());
		if (arena != null && arena.getState().equals(GameState.LIVE)) {
			Game game = arena.getGame();
			game.death(e.getEntity());

		}
	}


	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {

		Arena arena = hearthstone.getArenaManager().getArena(e.getPlayer());
		if (arena != null && arena.getState().equals(GameState.LIVE)) {
			e.setRespawnLocation(arena.getGame().respawn(e.getPlayer()));
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();

		if (e.getView().getTitle().contains("Kit Selection") && e.getInventory() != null && e.getCurrentItem() != null) {
			e.setCancelled(true);

			ItemMeta meta = e.getCurrentItem().getItemMeta();
			if (meta != null) {
				// Create the same NamespacedKey as used in KitUI
				NamespacedKey kitKey = new NamespacedKey(hearthstone, "kit_type");

				// Get the kit type from PersistentDataContainer
				String kitTypeName = meta.getPersistentDataContainer().get(kitKey, PersistentDataType.STRING);

				if (kitTypeName != null) {
					try {
						KitType type = KitType.valueOf(kitTypeName);

						Arena arena = hearthstone.getArenaManager().getArena(player);
						if (arena != null) {
							KitType activated = arena.getKitType(player);
							if (activated != null) {
								player.sendMessage(ChatColor.RED + "You already have this kit equipped!");
							} else {
								player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " Kit!");
								arena.setKit(player.getUniqueId(), type);
							}

							player.closeInventory();
						}
					} catch (IllegalArgumentException ex) {
						player.sendMessage(ChatColor.RED + "Invalid kit selection!");
						player.closeInventory();
					}
				} else {
					player.sendMessage(ChatColor.RED + "No kit data found!");
					player.closeInventory();
				}
			} else {
				player.sendMessage(ChatColor.RED + "Item has no metadata!");
				player.closeInventory();
			}
		}
	}
}