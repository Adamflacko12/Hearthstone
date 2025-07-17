package me.aaaaadam.hearthstone.command;

import me.aaaaadam.hearthstone.GameState;
import me.aaaaadam.hearthstone.Hearthstone;
import me.aaaaadam.hearthstone.instance.Arena;
import me.aaaaadam.hearthstone.kit.KitUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

	private Hearthstone hearthstone;

	public ArenaCommand(Hearthstone hearthstone) {
		this.hearthstone = hearthstone;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
				player.sendMessage(ChatColor.GREEN + "These are the available arenas:");
				for (Arena arena : hearthstone.getArenaManager().getArenas()) {
					player.sendMessage(ChatColor.GREEN + "- " + arena.getId() + "(" + arena.getState().name() + ")");
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("kit")) {
				Arena arena	= hearthstone.getArenaManager().getArena(player);
				if (arena != null) {
					if (arena.getState() != GameState.LIVE) {
						new KitUI(player);
					} else {
						player.sendMessage(ChatColor.RED + "You cannot select a kit at this time!");
					}
				} else {
					player.sendMessage(ChatColor.RED + "You are not in an arena!");
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
				Arena arena = hearthstone.getArenaManager().getArena(player);
				if (arena != null) {
					player.sendMessage(ChatColor.RED + "You left the arena");
					arena.removePlayer(player);
				} else {
					player.sendMessage(ChatColor.RED + "You are not in an arena!");
				}
			} else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
				if (hearthstone.getArenaManager().getArena(player) != null) {
					player.sendMessage(ChatColor.RED + "You are already playing in an arena!");
					return false;
				}

				int id;
				try {
					id = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					player.sendMessage(ChatColor.RED + "You specifed an invalid arena ID.");
					return false;
				}

				if (id >= 0 && id < hearthstone.getArenaManager().getArenas().size()) {
					Arena arena = hearthstone.getArenaManager().getArena(id);
					if (arena.getPlayers().size() != 4) {
						if (arena.getState() == GameState.RECRUITING || arena.getState() == GameState.COUNTDOWN) {
							player.sendMessage(ChatColor.GREEN + "You are now playing in Arena " + id + ".");
							arena.addPlayer(player);
						} else {
							player.sendMessage(ChatColor.GREEN + "You cannot join this arena");
						}
					} else {
							player.sendMessage(ChatColor.RED + "This game is full!");
						}
					} else {
						player.sendMessage(ChatColor.RED + "You specifed an invalid arena ID.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "Invalid usage! These are the options:");
				player.sendMessage(ChatColor.RED + "- /arena list");
				player.sendMessage(ChatColor.RED + "- /arena leave");
				player.sendMessage(ChatColor.RED + "- /arena join <id>");
				player.sendMessage(ChatColor.RED + "- /arena kit");
			}
		}

		return false;
	}
}
