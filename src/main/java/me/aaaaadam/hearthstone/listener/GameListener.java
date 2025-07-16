package me.aaaaadam.hearthstone.listener;

import me.aaaaadam.hearthstone.GameState;
import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.instance.Arena;
import me.aaaaadam.hearthstone.instance.Game;
import me.aaaaadam.hearthstone.instance.Team;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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
}
