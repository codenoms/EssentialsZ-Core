package me.darkwinged.Essentials.REWORK.Events.Economy;

import me.darkwinged.Essentials.Main;
import me.darkwinged.Essentials.REWORK.Utils.CustomFiles.Economy.MoneyPouchFile;
import me.darkwinged.Essentials.REWORK.Utils.EconomyManager;
import me.darkwinged.Essentials.REWORK.Utils.ErrorMessages;
import me.darkwinged.Essentials.REWORK.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

import static jdk.nashorn.internal.objects.ArrayBufferView.length;
import static org.apache.commons.lang.math.RandomUtils.nextInt;
import static org.bukkit.Material.getMaterial;

public class MoneyPouchesEvent implements Listener {

    private MoneyPouchFile moneyPouchFile;
    private Main plugin;
    public MoneyPouchesEvent(MoneyPouchFile moneyPouchFile, Main plugin) {
        this.moneyPouchFile = moneyPouchFile;
        this.plugin = plugin;
    }

    HashMap<UUID, Integer> open = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (plugin.getConfig().getBoolean("Economy", true)) {
            if (plugin.getConfig().getBoolean("Money_Pouches", true)) {
                Player player = event.getPlayer();
                if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    for (String name : Utils.MoneyPouches.keySet()) {
                        if (name == null || !player.getItemInHand().hasItemMeta()) return;
                        if (!name.equals(player.getItemInHand().getItemMeta().getDisplayName())) return;
                        if (open.containsKey(player.getUniqueId())) return;
                        if (!EconomyManager.hasAccount(player.getName())) return;
                        // Getting the amount
                        int max = Utils.MoneyPouches_max.get(name);
                        int min = Utils.MoneyPouches_min.get(name);
                        int int_amount = nextInt((max-min) + 1) + min;
                        double amount;
                        try {
                            amount = int_amount;
                            event.setCancelled(true);
                        } catch(Exception e) {
                            event.setCancelled(true);
                            player.updateInventory();
                            return;
                        }
                        // Adding the amount to the players balance
                        EconomyManager.setBalance(player.getName(), EconomyManager.getBalance(player.getName()) + amount);
                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                        int open_time = String.valueOf(int_amount).length();
                        open.put(player.getUniqueId(), open_time);
                        new BukkitRunnable() {
                            public void run() {
                                if (!open.containsKey(player.getUniqueId())) return;
                                if (open.get(player.getUniqueId()) <= 0) {
                                    open.remove(player.getUniqueId());
                                    String line1 = Utils.chat("&a"+plugin.getConfig().getString("Currency_Symbol") + int_amount);
                                    String line2 = Utils.chat("&fOpening Pouch...");
                                    player.sendTitle(line1, line2);
                                    player.sendMessage(Utils.chat("&aYou have oppend a money pouch and gained &2" + plugin.getConfig().getString("Currency_Symbol") + int_amount));
                                    cancel();
                                    return;
                                }
                                // Removing 1 from the count
                                if (open_time > 0) {
                                    player.sendTitle(Utils.chat("&a$&k"+int_amount), Utils.chat("&fOpening Pouch..."));
                                }
                                open.put(player.getUniqueId(), open.get(player.getUniqueId()) - 1);
                            }
                        }.runTaskTimer(plugin, 0L, 8L * open_time);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        for (String name : Utils.MoneyPouches.keySet()) {
            if (name == null || !player.getItemInHand().hasItemMeta()) return;
            if (!name.equals(player.getItemInHand().getItemMeta().getDisplayName())) return;
            event.setCancelled(true);
        }
    }


}
