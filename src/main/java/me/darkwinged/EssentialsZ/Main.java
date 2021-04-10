package me.darkwinged.EssentialsZ;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.darkwinged.EssentialsZ.Commands.Chat.*;
import me.darkwinged.EssentialsZ.Commands.Economy.*;
import me.darkwinged.EssentialsZ.Commands.Teleport.Staff.*;
import me.darkwinged.EssentialsZ.Commands.Teleport.*;
import me.darkwinged.EssentialsZ.Commands.World.Gamemodes.*;
import me.darkwinged.EssentialsZ.Commands.World.*;
import me.darkwinged.EssentialsZ.Commands.cmd_Reload;
import me.darkwinged.EssentialsZ.Events.Chat.ChatControl;
import me.darkwinged.EssentialsZ.Events.Chat.Color;
import me.darkwinged.EssentialsZ.Events.Chat.Displayname;
import me.darkwinged.EssentialsZ.Events.Chat.JoinAndLeaveMessage;
import me.darkwinged.EssentialsZ.Events.Economy.AccountSetup;
import me.darkwinged.EssentialsZ.Events.Economy.BankNotes;
import me.darkwinged.EssentialsZ.Events.Economy.MoneyPouchesEvent;
import me.darkwinged.EssentialsZ.Events.Economy.PlayerHeads;
import me.darkwinged.EssentialsZ.Events.PlayerData;
import me.darkwinged.EssentialsZ.Events.Signs.Sign_Balance;
import me.darkwinged.EssentialsZ.Events.Signs.Sign_Gamemode;
import me.darkwinged.EssentialsZ.Events.Signs.Sign_Up;
import me.darkwinged.EssentialsZ.Events.Teleport.Back;
import me.darkwinged.EssentialsZ.Events.Teleport.NoVoid;
import me.darkwinged.EssentialsZ.Events.Teleport.OnRespawn;
import me.darkwinged.EssentialsZ.Events.Teleport.SpawnOnJoin;
import me.darkwinged.EssentialsZ.Events.World.*;
import me.darkwinged.EssentialsZ.Libaries.EssentialsZEconomy.EconomyManager;
import me.darkwinged.EssentialsZ.Libaries.EssentialsZEconomy.Economy_EssentialsZ;
import me.darkwinged.EssentialsZ.Libaries.Lang.CustomConfig;
import me.darkwinged.EssentialsZ.Libaries.Lang.MetricsLite;
import me.darkwinged.EssentialsZ.Libaries.Lang.Utils;
import me.darkwinged.EssentialsZ.Libaries.PlaceHolders;
import me.darkwinged.EssentialsZ.Libaries.TeleportUtils;
import me.darkwinged.EssentialsZ.Libaries.VaultHook;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin implements Listener {

    public static Main getInstance;
    public EconomyManager economyManager;
    public ProtocolManager protocolManager;
    public Economy_EssentialsZ economy_essentialsZ;
    public EssentialsZAPI essentialsZAPI = (EssentialsZAPI) Bukkit.getServer().getPluginManager().getPlugin("EssentialsZAPI");
    public EssentialsZSpawn essentialsZSpawn = (EssentialsZSpawn) Bukkit.getServer().getPluginManager().getPlugin("EssentialsZSpawn");

    public int Delay = getConfig().getInt("Teleportation_Delay");
    public boolean Module_Economy = false;

    private VaultHook vaultHook;
    private static Chat chat = null;

    public CustomConfig MoneyPouchesFile = new CustomConfig(this, "Economy/Money Pouches", true);
    public CustomConfig MessagesFile = new CustomConfig(this, "Messages", true);
    public CustomConfig BlockedCommandsFile = new CustomConfig(this, "Chat/Blocked Commands", true);
    public CustomConfig ChatFilterFile = new CustomConfig(this, "Chat/Chat Filter", true);
    public CustomConfig AutoMessagesFile = new CustomConfig(this, "Chat/Auto Messages", true);
    public CustomConfig WorthFile = new CustomConfig(this, "Economy/Worth", true);
    public CustomConfig CouponsFile = new CustomConfig(this, "Coupons", true);

    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("EssentialsZAPI") == null) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDisabling plugin, No EssentialsZ API Found."));
            getServer().getPluginManager().disablePlugin(this);
        }
        getInstance = this;

        // Loading Files
        loadConfig();
        loadCustomFiles();

        // Getting ProtocolLib, Teleport Utils, MetricsLite, PlaceholderAPI, Vault, EssentialsZ APi, Bungeecord, Adding EconomyManager
        TeleportUtils teleportUtils = new TeleportUtils(this);
        MetricsLite metricsLite = new MetricsLite(this, 9811);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // Vault hook ================================================
        economyManager = new EconomyManager();
        economy_essentialsZ = new Economy_EssentialsZ();

        setupChat();
        vaultHook = new VaultHook();
        vaultHook.hook();
        //=============================================================

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolders().register();
        }
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            this.protocolManager = ProtocolLibrary.getProtocolManager();
        }

        loadAutoMessages();

        // Registering Commands / Events / Loops
        registerCommands();
        registerEvents();
        AutoMessage();
        vanishCheck();

        // Console Start Message
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEssentialsZ Core plugin has been enabled!"));
    }

    public void onDisable() {
        // Saving accounts
        for (Player online : Bukkit.getOnlinePlayers()) {
            economyManager.saveBalance(online);
        }

        // Unhooking vault
        vaultHook.unhook();

        // Console Stop Message
        getServer().getConsoleSender().sendMessage(essentialsZAPI.utils.chat("&cEssentialsZ Core plugin has been disabled!", null, null, null, false));
    }

    @SuppressWarnings("ConstantConditions")
    public void registerCommands() {
        getCommand("essentials").setExecutor(new cmd_Reload(this));

        // Economy
        getCommand("economy").setExecutor(new cmd_Economy());
        getCommand("balance").setExecutor(new cmd_Balance());
        getCommand("pay").setExecutor(new cmd_Pay());
        getCommand("withdraw").setExecutor(new cmd_Withdraw());
        getCommand("pouches").setExecutor(new cmd_MoneyPouches());
        //getCommand("autosell").setExecutor(new cmd_Autosell(this));
        //getCommand("sellhand").setExecutor(new cmd_Sellhand(this));
        //getCommand("sell").setExecutor(new cmd_Sell(this));

        // Chat
        getCommand("staffchat").setExecutor(new cmd_Staffchat(this));
        getCommand("clearchat").setExecutor(new cmd_Clearchat(this));
        getCommand("broadcast").setExecutor(new cmd_Broadcast(this));
        getCommand("mutechat").setExecutor(new cmd_Mutechat(this));
        getCommand("motd").setExecutor(new cmd_MOTD(this));
        getCommand("sudo").setExecutor(new cmd_Sudo(this));

        // Teleportation
        getCommand("tp").setExecutor(new cmd_TP(this));
        getCommand("tphere").setExecutor(new cmd_TPhere(this));
        getCommand("hub").setExecutor(new cmd_Hub(this));
        getCommand("rtp").setExecutor(new cmd_RandomTeleport(this));
        getCommand("top").setExecutor(new cmd_Top(this));
        getCommand("back").setExecutor(new cmd_Back(this));
        getCommand("setwarp").setExecutor(new cmd_SetWarp());
        getCommand("delwarp").setExecutor(new cmd_DelWarp(this));
        getCommand("warp").setExecutor(new cmd_Warp());
        getCommand("warps").setExecutor(new cmd_Warps());
        getCommand("sethome").setExecutor(new cmd_SetHome());
        getCommand("delhome").setExecutor(new cmd_DelHome());
        getCommand("home").setExecutor(new cmd_Home());
        getCommand("homes").setExecutor(new cmd_Homes());

        // World
        getCommand("hat").setExecutor(new cmd_Hat());
        getCommand("ping").setExecutor(new cmd_Ping());
        getCommand("gma").setExecutor(new cmd_AdventureMode(this));
        getCommand("gmc").setExecutor(new cmd_CreativeMode(this));
        getCommand("gms").setExecutor(new cmd_SurvivalMode(this));
        getCommand("gmsp").setExecutor(new cmd_SpectatorMode(this));
        getCommand("gamemode").setExecutor(new cmd_Gamemode(this));
        getCommand("invsee").setExecutor(new cmd_Invsee());
        getCommand("vanish").setExecutor(new cmd_Vanish());
        getCommand("repair").setExecutor(new cmd_Repair());
        getCommand("world").setExecutor(new cmd_CreateWorld());
        getCommand("delworld").setExecutor(new cmd_DelWorld());
        getCommand("enderchest").setExecutor(new cmd_Enderchest());
        getCommand("disposal").setExecutor(new cmd_Disposal());
        getCommand("craft").setExecutor(new cmd_Craft());
        getCommand("kill").setExecutor(new cmd_Kill());
        getCommand("god").setExecutor(new cmd_God());
        getCommand("heal").setExecutor(new cmd_Heal());
        getCommand("fly").setExecutor(new cmd_Fly());
        getCommand("feed").setExecutor(new cmd_Feed());
        getCommand("smite").setExecutor(new cmd_Smite());
        getCommand("filltnt").setExecutor(new cmd_TNTFill());

    }

    public void registerEvents() {
        // Registering events in this class
        getServer().getPluginManager().registerEvents(this, this);

        // Chat Events
        getServer().getPluginManager().registerEvents(new ChatControl(), this);
        getServer().getPluginManager().registerEvents(new JoinAndLeaveMessage(), this);
        getServer().getPluginManager().registerEvents(new Color(), this);
        getServer().getPluginManager().registerEvents(new Displayname(), this);

        // Economy Events
        getServer().getPluginManager().registerEvents(new PlayerHeads(), this);
        getServer().getPluginManager().registerEvents(new AccountSetup(), this);
        getServer().getPluginManager().registerEvents(new BankNotes(), this);
        getServer().getPluginManager().registerEvents(new MoneyPouchesEvent(), this);

        // SIGN EVENTS
        getServer().getPluginManager().registerEvents(new Sign_Balance(), this);
        getServer().getPluginManager().registerEvents(new Sign_Gamemode(), this);
        getServer().getPluginManager().registerEvents(new Sign_Up(), this);

        // TELEPORTATION EVENTS
        getServer().getPluginManager().registerEvents(new NoVoid(), this);
        getServer().getPluginManager().registerEvents(new OnRespawn(), this);
        getServer().getPluginManager().registerEvents(new SpawnOnJoin(), this);
        getServer().getPluginManager().registerEvents(new Back(), this);

        // WORLD EVENTS
        getServer().getPluginManager().registerEvents(new NoItemDropAndPickup(), this);
        getServer().getPluginManager().registerEvents(new NoPlaceBreakBlocks(), this);
        getServer().getPluginManager().registerEvents(new ChangeMOTD(), this);
        getServer().getPluginManager().registerEvents(new DoubleJump(), this);
        getServer().getPluginManager().registerEvents(new cmd_God(), this);
        getServer().getPluginManager().registerEvents(new Tablist(), this);
        getServer().getPluginManager().registerEvents(new WorldControl(), this);
        getServer().getPluginManager().registerEvents(new Rider(), this);
        getServer().getPluginManager().registerEvents(new PlayerCoords(), this);
        getServer().getPluginManager().registerEvents(new Playtime(), this);
        getServer().getPluginManager().registerEvents(new NetherWater(), this);
        getServer().getPluginManager().registerEvents(new InventoryFull(), this);

        getServer().getPluginManager().registerEvents(new PlayerData(), this);
    }

    // Loading the config and custom files to the server
    private FileConfiguration config;
    private File cfile;
    public void loadConfig() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        cfile = new File(getDataFolder(), "config.yml");
    }

    public void loadCustomFiles() {
        CouponsFile.saveDefaultConfig();

        // Economy
        MoneyPouchesFile.saveDefaultConfig();
        WorthFile.saveDefaultConfig();

        // Chat
        MessagesFile.saveDefaultConfig();
        BlockedCommandsFile.saveDefaultConfig();
        ChatFilterFile.saveDefaultConfig();
        AutoMessagesFile.saveDefaultConfig();
    }

    // Managing auto messages
    public void loadAutoMessages() {
        if (!AutoMessagesFile.getConfig().contains("Messages.")) return;
        Utils.AutoMessages.addAll(AutoMessagesFile.getConfig().getStringList("Messages"));
    }
    public void AutoMessage() {
        if (getConfig().getBoolean("Chat.enabled", true)) {
            if (getConfig().getBoolean("Chat.Settings.Auto Messages", true)) {
                for (String key : AutoMessagesFile.getConfig().getConfigurationSection("Messages").getKeys(false)) {
                    String message = AutoMessagesFile.getConfig().getString("Messages." + key + ".content").replaceAll("%n", "\n");
                    long interval = (long) AutoMessagesFile.getConfig().getInt("Messages." + key + ".interval") * 60;
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                        public void run() {
                            if (AutoMessagesFile.getConfig().getBoolean("Messages." + key + ".center", true)) {
                                Bukkit.broadcastMessage(essentialsZAPI.utils.chat(essentialsZAPI.utils.CenteredMessage(message),
                                        null, null, null, false));
                                return;
                            }
                            Bukkit.broadcastMessage(essentialsZAPI.utils.chat(message, null, null, null, false));
                        }
                    }, 20L * interval, 20L * interval);
                }
            }
        }
    }

    // Checking if player is in vanish and alerting.
    public void vanishCheck() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (Utils.invisible_list.contains(player.getUniqueId())) {
                        essentialsZAPI.utils.sendActionBar(player, "&fYou are in &cVanish");
                    }
                }
            }
        }, 0L, 20L);
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public Chat getChat() {
        return chat;
    }
}
