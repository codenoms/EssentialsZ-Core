package me.darkwinged.Essentials.REWORK.Events.World;

import me.darkwinged.Essentials.Main;
import me.darkwinged.Essentials.REWORK.Utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ChangeMOTD implements Listener {

    private Main plugin;
    public ChangeMOTD(Main plugin) { this.plugin = plugin; }

    @EventHandler
    public void MOTDChange(ServerListPingEvent event) {
        if (plugin.getConfig().getBoolean("Change MOTD", true)) {
            event.setMotd(Utils.chat(plugin.getConfig().getString("MOTD")
                    .replace("%n", "\n")));
        }
    }

}
