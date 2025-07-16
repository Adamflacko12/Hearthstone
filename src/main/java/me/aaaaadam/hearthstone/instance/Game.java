package me.aaaaadam.hearthstone.instance;

import me.aaaaadam.hearthstone.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Game {

	private Arena arena;
	private HashMap<UUID, Team> teams;
	private List<BukkitTask> tasks;

	private HashMap<Team, Boolean> bedsAlive;
	private List<UUID> alive;

	public Game(Arena arena) {
		this.arena = arena;
		teams = new HashMap<>();
		tasks = new ArrayList<>();
		alive = new ArrayList<>();
		bedsAlive = new HashMap<>();
	}

	public void start() {
		arena.setState(GameState.LIVE);
		arena.sendMessage(ChatColor.GREEN + "Game has begun! Fight to the death!");



		for (int i = 0; i < arena.getPlayers().size(); i++) {
			UUID uuid = arena.getPlayers().get(i);
			Team team = Team.values()[i];
			teams.put(uuid, team);
			bedsAlive.put(team, true);

			BedLocation location = arena.getBeds().get(team);
			Block block = location.getBlock();
			for (Bed.Part part : Bed.Part.values()) {
				block.setBlockData(Bukkit.createBlockData(Material.RED_BED, (data) -> {
					((Bed) data).setPart(part);
					((Bed) data).setFacing(location.getFacing());
				}));
				block.setMetadata("team", new FixedMetadataValue(arena.getHearthstone(), team.name()));
				block = block.getRelative(location.getFacing().getOppositeFace());
			}

				Player player = Bukkit.getPlayer(uuid);
				player.setGameMode(GameMode.SURVIVAL);
				player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
				player.teleport(arena.getSpawns().get(team));
				alive.add(uuid);
			}

		tasks.add(Bukkit.getScheduler().runTaskTimer(arena.getHearthstone(), () -> {
			for (UUID uuid : alive ){
				if (Bukkit.getPlayer(uuid).getLocation().getY() <= arena.getyRespawn()) {
					Team team = teams.get(uuid);
					if (bedsAlive.get(teams.get(uuid))) {
						Bukkit.getPlayer(uuid).teleport(arena.getSpawns().get(team));
					} else {

					}
				}
			}
		}, 4, 4));
	}

	public boolean destroyBed(Team team, Player player) {
		if (teams.get(player.getUniqueId()) == team) { return true; }
		arena.sendMessage(player.getName() + " has broken " + team.getName() + "'s bed!");
		return false;

	}

	public void cancelTasks() {
		for (BukkitTask task : tasks) {
			task.cancel();
		}
	}
}
