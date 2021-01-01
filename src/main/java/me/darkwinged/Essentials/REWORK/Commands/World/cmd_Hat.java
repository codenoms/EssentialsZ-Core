package me.darkwinged.Essentials.REWORK.Commands.World;

import me.darkwinged.Essentials.Main;
import me.darkwinged.Essentials.REWORK.Utils.ErrorMessages;
import me.darkwinged.Essentials.REWORK.Utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class cmd_Hat implements CommandExecutor {

    private Main plugin;
    public cmd_Hat(Main plugin) {
        this.plugin = plugin; }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("hat")) {
            if (plugin.getConfig().getBoolean("cmd_Hat", true)) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ErrorMessages.Console);
                    return true;
                }
                Player player = (Player) sender;
                if (player.hasPermission(Permissions.Hat) || player.hasPermission(Permissions.GlobalOverwrite)) {
                    ItemStack inHand = player.getItemInHand();
                    if (player.getInventory().getHelmet() != null) {
                        player.getInventory().addItem(player.getInventory().getHelmet());
                    }
                    player.getInventory().setHelmet(inHand);
                    player.getInventory().setItemInHand(null);
                } else {
                    player.sendMessage(ErrorMessages.NoPermission);
                }
            }
        }
        return true;
    }

}
