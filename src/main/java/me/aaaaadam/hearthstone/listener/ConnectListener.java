package me.aaaaadam.hearthstone.listener;

import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.instance.Arena;
import me.aaaaadam.hearthstone.manager.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListener implements Listener {

	private Hearthstone hearthstone;

	public ConnectListener(Hearthstone hearthstone) {
		this.hearthstone = hearthstone;
	}


	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		e.getPlayer().teleport(ConfigManager.getLobbySpawn());

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e ) {

		Arena arena = hearthstone.getArenaManager().getArena(e.getPlayer());
		if (arena != null) {
			arena.removePlayer(e.getPlayer());
		}

	}

}
