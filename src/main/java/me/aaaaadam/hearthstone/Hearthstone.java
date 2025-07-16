package me.aaaaadam.hearthstone;

import me.aaaaadam.hearthstone.command.ArenaCommand;
import me.aaaaadam.hearthstone.listener.ConnectListener;
import me.aaaaadam.hearthstone.listener.GameListener;
import me.aaaaadam.hearthstone.manager.ArenaManager;
import me.aaaaadam.hearthstone.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Hearthstone extends JavaPlugin {

	private ArenaManager arenaManager;

	@Override
	public void onEnable() {
		ConfigManager.setupConfig(this);
		arenaManager = new ArenaManager(this);

		Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
		Bukkit.getPluginManager().registerEvents(new GameListener(this), this);

		getCommand("arena").setExecutor(new ArenaCommand(this));

	}

	public ArenaManager getArenaManager() { return arenaManager; }

}
