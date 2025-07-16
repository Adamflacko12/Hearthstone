package me.aaaaadam.hearthstone.manager;

import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.instance.Arena;
import me.aaaaadam.hearthstone.instance.BedLocation;
import me.aaaaadam.hearthstone.instance.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaManager {

	private List<Arena> arenas = new ArrayList<>();

	public ArenaManager(Hearthstone hearthstone) {
		FileConfiguration config = hearthstone.getConfig();
		for (String str : config.getConfigurationSection("arenas.").getKeys(false)) {
			World world = Bukkit.createWorld(new WorldCreator(config.getString("arenas." + str + ".world")));
			HashMap<Team, Location> spawns = new HashMap<>();
			for (String team : config.getConfigurationSection("arenas." + str + ".spawns.").getKeys(false)) {
				spawns.put(Team.valueOf(team.toUpperCase()), new Location(
						world,
						config.getDouble("arenas." + str + ".spawns." + team + ".x"),
						config.getDouble("arenas." + str + ".spawns." + team + ".y"),
						config.getDouble("arenas." + str + ".spawns." + team + ".z"),
				(float) config.getDouble("arenas." + str + ".spawns." + team + ".yaw"),
				(float) config.getDouble("arenas." + str + ".spawns." + team + ".pitch")));
			}
			HashMap<Team, BedLocation> beds = new HashMap<>();
			for (String team : config.getConfigurationSection("arenas." + str + ".beds.").getKeys(false)) {
				beds.put(Team.valueOf(team.toUpperCase()), new BedLocation(
						world,
						config.getDouble("arenas." + str + ".beds." + team + ".x"),
						config.getDouble("arenas." + str + ".beds." + team + ".y"),
						config.getDouble("arenas." + str + ".beds." + team + ".z"),
						BlockFace.valueOf(config.getString("arenas." + str + ".beds." + team + ".facing").toUpperCase())));
			}
			arenas.add(new Arena(hearthstone, Integer.parseInt(str), Integer.parseInt(config.getString("arenas." + str + ".y-respawn")), spawns, beds));
		}
	}

	public List<Arena> getArenas() { return arenas; }

	public Arena getArena(Player player) {
		for (Arena arena : arenas) {
			if (arena.getPlayers().contains(player.getUniqueId())) {
				return arena;
			}
		}
		return null;
	}

	public Arena getArena(int id) {
		for (Arena arena : arenas) {
			if (arena.getId() == id) {
				return arena;
			}
		}
		return null;
	}

}