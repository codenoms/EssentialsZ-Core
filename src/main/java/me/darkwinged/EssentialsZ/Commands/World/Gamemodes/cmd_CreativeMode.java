package me.darkwinged.EssentialsZ.Commands.World.Gamemodes;

import me.darkwinged.EssentialsZ.Libaries.Lang.Errors;
import me.darkwinged.EssentialsZ.Libaries.Lang.Permissions;
import me.darkwinged.EssentialsZ.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_CreativeMode implements CommandExecutor {

    private final Main plugin = Main.getInstance;

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gmc")) {
            if (plugin.getConfig().getBoolean("Commands.Gamemode", true)) {
                if (!(sender instanceof Player)) {
                    if (args.length != 1) {
                        sender.sendMessage(Errors.getErrors(Errors.GamemodeUsage));
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(Errors.getErrors(Errors.NoPlayerFound));
                        return true;
                    }
                    String Message = plugin.MessagesFile.getConfig().getString("Gamemode Other")
                            .replaceAll("%gamemode%", "creative");
                    sender.sendMessage(plugin.essentialsZAPI.utils.chat(plugin.MessagesFile.getConfig().getString("Prefix") + Message,
                            target, target, null, false));
                    target.setGameMode(GameMode.CREATIVE);
                    return true;
                }
                Player player = (Player) sender;
                if (args.length != 1) {
                    if (player.hasPermission(Permissions.CreativeMode) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
                        String Message = plugin.MessagesFile.getConfig().getString("Gamemode").replaceAll("%gamemode%", "creative");
                        player.sendMessage(plugin.essentialsZAPI.utils.chat(plugin.MessagesFile.getConfig().getString("Prefix") + Message,
                                null, null, null, false));
                        player.setGameMode(GameMode.CREATIVE);
                    } else {
                        player.sendMessage(Errors.getErrors(Errors.NoPermission));
                    }
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (player.hasPermission(Permissions.CreativeModeOther) || player.hasPermission(Permissions.GamemodeGlobal) || player.hasPermission(Permissions.GlobalOverwrite)) {
                        if (target == null) {
                            player.sendMessage(Errors.getErrors(Errors.NoPlayerFound));
                            return true;
                        }
                        String Message = plugin.MessagesFile.getConfig().getString("Gamemode Other")
                                .replaceAll("%gamemode%", "creative");
                        sender.sendMessage(plugin.essentialsZAPI.utils.chat(plugin.MessagesFile.getConfig().getString("Prefix") + Message,
                                target, target, null, false));
                        target.setGameMode(GameMode.CREATIVE);
                    }
                }
            }
        }
        return true;
    }
}
