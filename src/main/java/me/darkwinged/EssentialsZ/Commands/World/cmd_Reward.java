package me.darkwinged.EssentialsZ.Commands.World;

import me.darkwinged.EssentialsZ.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class cmd_Reward implements CommandExecutor {

    private final Main plugin;
    public cmd_Reward(Main plugin) { this.plugin = plugin; }

    HashMap<UUID, Inventory> test = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reward")) {

        }
        return false;
    }

}