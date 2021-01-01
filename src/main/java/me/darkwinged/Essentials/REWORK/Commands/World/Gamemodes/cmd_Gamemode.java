package me.darkwinged.Essentials.REWORK.Commands.World.Gamemodes;

import me.darkwinged.Essentials.Main;
import me.darkwinged.Essentials.REWORK.Utils.CustomFiles.Chat.MessagesFile;
import me.darkwinged.Essentials.REWORK.Utils.ErrorMessages;
import me.darkwinged.Essentials.REWORK.Utils.Permissions;
import me.darkwinged.Essentials.REWORK.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_Gamemode implements CommandExecutor {

	private MessagesFile messagesFile;
	private Main plugin;
	public cmd_Gamemode(MessagesFile messagesFile, Main plugin) {
		this.plugin = plugin;
		this.messagesFile = messagesFile;
	}

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gamemode")) {
			if (plugin.getConfig().getBoolean("cmd_Gamemode", true)) {
				if (args.length <= 2) {
					if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")) {
						if (!(sender instanceof Player)) {
							if (args.length != 2) {
								sender.sendMessage(ErrorMessages.GamemodeUsage);
								return true;
							}
							Player target = Bukkit.getPlayer(args[1]);
							if (target == null) {
								sender.sendMessage(ErrorMessages.NoPlayerFound);
								return true;
							}
							String Message = messagesFile.getConfig().getString("Gamemode Other")
									.replaceAll("%gamemode%", "survival")
									.replaceAll("%player%", target.getName());
							sender.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
							target.setGameMode(GameMode.SURVIVAL);
						}
						Player player = (Player) sender;
						if (args.length != 2) {
							if (player.hasPermission(Permissions.SurvivalMode) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
								String Message = messagesFile.getConfig().getString("Gamemode").replaceAll("%gamemode%", "survival");
								player.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
								player.setGameMode(GameMode.SURVIVAL);
							} else {
								player.sendMessage(ErrorMessages.NoPermission);
							}
						} else {
							Player target = Bukkit.getPlayer(args[1]);
							if (player.hasPermission(Permissions.SurvivalModeOther) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
								if (target == null) {
									sender.sendMessage(ErrorMessages.NoPlayerFound);
									return true;
								}
								String Message = messagesFile.getConfig().getString("Gamemode Other")
										.replaceAll("%gamemode%", "survival")
										.replaceAll("%player%", target.getName());
								sender.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
								target.setGameMode(GameMode.SURVIVAL);
							}
						}
					} else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")) {
						if (!(sender instanceof Player)) {
							if (args.length != 2) {
								sender.sendMessage(ErrorMessages.GamemodeUsage);
								return true;
							}
							Player target = Bukkit.getPlayer(args[1]);
							if (target == null) {
								sender.sendMessage(ErrorMessages.NoPlayerFound);
								return true;
							}
							String Message = messagesFile.getConfig().getString("Gamemode Other")
									.replaceAll("%gamemode%", "creative")
									.replaceAll("%player%", target.getName());
							sender.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
							target.setGameMode(GameMode.CREATIVE);
						}
						Player player = (Player) sender;
						if (args.length != 2) {
							if (player.hasPermission(Permissions.CreativeMode) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
								String Message = messagesFile.getConfig().getString("Gamemode").replaceAll("%gamemode%", "creative");
								player.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
								player.setGameMode(GameMode.CREATIVE);
							} else {
								player.sendMessage(ErrorMessages.NoPermission);
							}
						} else {
							Player target = Bukkit.getPlayer(args[1]);
							if (player.hasPermission(Permissions.CreativeModeOther) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
								if (target == null) {
									sender.sendMessage(ErrorMessages.NoPlayerFound);
									return true;
								}
								String Message = messagesFile.getConfig().getString("Gamemode Other")
										.replaceAll("%gamemode%", "creative")
										.replaceAll("%player%", target.getName());
								sender.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
								target.setGameMode(GameMode.CREATIVE);
							}
						}
					} else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")) {
						if (!(sender instanceof Player)) {
							if (args.length != 2) {
								sender.sendMessage(ErrorMessages.GamemodeUsage);
								return true;
							}
							Player target = Bukkit.getPlayer(args[1]);
							if (target == null) {
								sender.sendMessage(ErrorMessages.NoPlayerFound);
								return true;
							}
							String Message = messagesFile.getConfig().getString("Gamemode Other")
									.replaceAll("%gamemode%", "adventure")
									.replaceAll("%player%", target.getName());
							sender.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
							target.setGameMode(GameMode.ADVENTURE);
						}
						Player player = (Player) sender;
						if (args.length != 2) {
							if (player.hasPermission(Permissions.AdventureMode) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
								String Message = messagesFile.getConfig().getString("Gamemode").replaceAll("%gamemode%", "adventure");
								player.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
								player.setGameMode(GameMode.ADVENTURE);
							} else {
								player.sendMessage(ErrorMessages.NoPermission);
							}
						} else {
							Player target = Bukkit.getPlayer(args[1]);
							if (player.hasPermission(Permissions.AdventureModeOther) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
								if (target == null) {
									sender.sendMessage(ErrorMessages.NoPlayerFound);
									return true;
								}
								String Message = messagesFile.getConfig().getString("Gamemode Other")
										.replaceAll("%gamemode%", "adventure")
										.replaceAll("%player%", target.getName());
								sender.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
								target.setGameMode(GameMode.ADVENTURE);
							}
						}
					} else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp")) {
						if (!(sender instanceof Player)) {
							if (args.length != 2) {
								sender.sendMessage(ErrorMessages.GamemodeUsage);
								return true;
							}
							Player target = Bukkit.getPlayer(args[1]);
							if (target == null) {
								sender.sendMessage(ErrorMessages.NoPlayerFound);
								return true;
							}
							String Message = messagesFile.getConfig().getString("Gamemode Other")
									.replaceAll("%gamemode%", "spectator")
									.replaceAll("%player%", target.getName());
							sender.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
							target.setGameMode(GameMode.SPECTATOR);
						}
						Player player = (Player) sender;
						if (args.length != 2) {
							if (player.hasPermission(Permissions.SpectatorMode) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
								String Message = messagesFile.getConfig().getString("Gamemode").replaceAll("%gamemode%", "spectator");
								player.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
								player.setGameMode(GameMode.SPECTATOR);
							} else {
								player.sendMessage(ErrorMessages.NoPermission);
							}
						} else {
							Player target = Bukkit.getPlayer(args[1]);
							if (player.hasPermission(Permissions.SpectatorModeOther) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
								if (target == null) {
									sender.sendMessage(ErrorMessages.NoPlayerFound);
									return true;
								}
								String Message = messagesFile.getConfig().getString("Gamemode Other")
										.replaceAll("%gamemode%", "spectator")
										.replaceAll("%player%", target.getName());
								sender.sendMessage(Utils.chat(messagesFile.getConfig().getString("Prefix") + Message));
								target.setGameMode(GameMode.SPECTATOR);
							}
						}
					} else
						sender.sendMessage(ErrorMessages.GamemodeUsage);
				}
			}
        }
        return true;
    }
}
