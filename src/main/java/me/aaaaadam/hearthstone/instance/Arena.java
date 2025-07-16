package me.aaaaadam.hearthstone.instance;

import me.aaaaadam.hearthstone.GameState;
import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Arena {

	private Hearthstone hearthstone;

	private int id;
	private int yRespawn;
	private HashMap<Team, Location> spawns;
	private HashMap<Team, BedLocation> beds;

	private GameState state;
	private List<UUID> players;
	private Countdown countdown;
	private Game game;

	public Arena(Hearthstone hearthstone, int id, int yRespawn, HashMap<Team, Location> spawns, HashMap<Team, BedLocation> beds) {
		this.hearthstone = hearthstone;

		this.id	= id;
		this.yRespawn = yRespawn;
		this.spawns = spawns;
		this.beds = beds;

		this.state = GameState.RECRUITING;
		this.players = new ArrayList<>();
		this.countdown = new Countdown(hearthstone, this);
		this.game = new Game(this);
	}

	/* GAME */

	public void start() { game.start(); }


	public void reset(boolean kickPlayers) {
		if (kickPlayers) {
			Location loc = ConfigManager.getLobbySpawn();
			for (UUID uuid : players) {
				Bukkit.getPlayer(uuid).teleport(loc);
			}
			players.clear();
		}
		sendTitle("", "");
		state = GameState.RECRUITING;
		countdown.cancel();
		countdown = new Countdown(hearthstone, this);
		game.cancelTasks();
		game = new Game(this);

	}

	/* TOOLS */

	public void sendMessage(String message) {
		for (UUID uuid : players) {
			Bukkit.getPlayer(uuid).sendMessage(message);
		}
	}

	public void sendTitle(String title, String subtitle) {
		for (UUID uuid : players) {
			Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
		}
	}

	/* PLAYERS */

	public void addPlayer(Player player) {
		players.add(player.getUniqueId());

		if (state.equals(GameState.RECRUITING) && players.size() >= ConfigManager.getRequiredPlayers()) {
			countdown.start();
		}
	}

	public void removePlayer(Player player) {
		players.remove(player.getUniqueId());
		player.teleport(ConfigManager.getLobbySpawn());
		player.sendTitle("", "");

		if (state == GameState.COUNTDOWN && players.size() < ConfigManager.getRequiredPlayers()) {
			sendMessage(ChatColor.RED + "There's not enough players. Countdown Stopped.");
			reset(false);
			return;
		}

		if (state == GameState.LIVE && players.size() < ConfigManager.getRequiredPlayers()) {
			sendMessage(ChatColor.RED + "The game has ended due to no players");
			reset(false);
		}
	}

	/* info */

	public Hearthstone getHearthstone() { return hearthstone; }

	public int getId() { return id; }
	public int getyRespawn() { return yRespawn;}

	public HashMap<Team, Location> getSpawns() { return spawns; }
	public GameState getState() { return state; }
	public List<UUID> getPlayers() { return players; }
	public Game getGame() { return game; }
	public HashMap<Team, BedLocation> getBeds() { return beds; }

	public void setState(GameState state) { this.state = state; }

}
