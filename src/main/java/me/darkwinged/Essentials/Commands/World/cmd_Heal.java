package me.darkwinged.Essentials.Commands.World;

import me.darkwinged.Essentials.Main;
import me.darkwinged.Essentials.Utils.Lang.Errors;
import me.darkwinged.Essentials.Utils.Lang.Permissions;
import me.darkwinged.Essentials.Utils.Lang.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class cmd_Heal implements CommandExecutor {

    private Main plugin;

    public cmd_Heal(Main plugin) {
        this.plugin = plugin;
    }

    private final GameMode creative = GameMode.CREATIVE;
    private final GameMode spectator = GameMode.SPECTATOR;

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("heal")) {
            if (plugin.getConfig().getBoolean("cmd_Heal", true)) {
                if (!(sender instanceof Player)) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target == null) {
                            sender.sendMessage(Utils.chat(Errors.getErrors(Errors.NoPlayerFound)));
                            return true;
                        }
                        if (target.getGameMode().equals(creative) || target.getGameMode().equals(spectator)) {
                            sender.sendMessage(Errors.getErrors(Errors.InvalidGameMode));
                            return true;
                        }
                        target.setHealth(20.0);
                        target.setFoodLevel(20);
                        removepotions(target);
                        sender.sendMessage(Utils.chat(plugin.MessagesFile.getConfig().getString("Prefix")) +
                                Utils.chat(plugin.MessagesFile.getConfig().getString("Healed Other").replaceAll("%player%", target.getName())));
                    } else {
                        sender.sendMessage(Utils.chat(Errors.getErrors(Errors.SpecifyPlayer)));
                    }
                    return true;
                }
                Player player = (Player) sender;
                if (args.length == 1) {
                    if (player.hasPermission(Permissions.HealOther) || player.hasPermission(Permissions.GlobalOverwrite)) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target == null) {
                            sender.sendMessage(Utils.chat(Errors.getErrors(Errors.NoPlayerFound)));
                            return true;
                        }
                        if (target.getGameMode().equals(creative) || target.getGameMode().equals(spectator)) {
                            sender.sendMessage(Errors.getErrors(Errors.InvalidGameMode));
                            return true;
                        }
                        target.setHealth(20.0);
                        target.setFoodLevel(20);
                        removepotions(target);
                        sender.sendMessage(Utils.chat(plugin.MessagesFile.getConfig().getString("Prefix")) +
                                Utils.chat(plugin.MessagesFile.getConfig().getString("Healed Other").replaceAll("%player%", target.getName())));
                    } else {
                        player.sendMessage(Errors.getErrors(Errors.NoPermission));
                    }
                } else {
                    if (player.hasPermission(Permissions.Heal) || player.hasPermission(Permissions.GlobalOverwrite)) {
                        if (player.getGameMode().equals(creative) || player.getGameMode().equals(spectator)) {
                            sender.sendMessage(Errors.getErrors(Errors.InvalidGameMode));
                            return true;
                        }
                        player.setHealth(20.0);
                        player.setFoodLevel(20);
                        removepotions(player);
                        sender.sendMessage(Utils.chat(plugin.MessagesFile.getConfig().getString("Prefix")) +
                                Utils.chat(plugin.MessagesFile.getConfig().getString("Healed")));
                    } else {
                        player.sendMessage(Errors.getErrors(Errors.NoPermission));
                    }
                }
            }
        }
        return true;
    }

    public void removepotions(Player player) {
        player.removePotionEffect(PotionEffectType.ABSORPTION);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.CONFUSION);
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.HARM);
        player.removePotionEffect(PotionEffectType.HEAL);
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        player.removePotionEffect(PotionEffectType.HUNGER);
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.POISON);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.SATURATION);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.WEAKNESS);
        player.removePotionEffect(PotionEffectType.WITHER);
    }

}