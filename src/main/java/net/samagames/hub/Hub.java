package net.samagames.hub;

import de.slikey.effectlib.EffectLib;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.commands.CommandManager;
import net.samagames.hub.common.HubRefresher;
import net.samagames.hub.common.managers.*;
import net.samagames.hub.common.receivers.ArenaListener;
import net.samagames.hub.common.receivers.MaintenanceListener;
import net.samagames.hub.common.receivers.SamaritanListener;
import net.samagames.hub.cosmetics.CosmeticManager;
import net.samagames.hub.events.player.GuiListener;
import net.samagames.hub.events.player.JumpListener;
import net.samagames.hub.events.player.PlayerListener;
import net.samagames.hub.events.protection.EntityEditionListener;
import net.samagames.hub.events.protection.InventoryEditionListener;
import net.samagames.hub.events.protection.PlayerEditionListener;
import net.samagames.hub.events.protection.WorldEditionListener;
import net.samagames.hub.games.GameManager;
import net.samagames.hub.games.sign.SignManager;
import net.samagames.hub.gui.GuiManager;
import net.samagames.hub.jump.JumpManager;
import net.samagames.hub.npcs.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Hub extends JavaPlugin
{
    private static Hub instance;

    private World hubWorld;
    private boolean debug;

    private PlayerManager playerManager;
    private ChatManager chatManager;
    private GuiManager guiManager;
    private HologramManager hologramManager;
    private EntityManager entityManager;
    private NPCManager npcManager;
    private ScoreboardManager scoreboardManager;
    private GameManager gameManager;
    private SignManager signManager;
    private JumpManager jumpManager;
    private CosmeticManager cosmeticManager;
    private StatsManager statsManager;

    private HubRefresher hubRefresher;

    public static Hub getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;

        this.hubWorld = Bukkit.getWorlds().get(0);

        this.saveDefaultConfig();
        this.debug = this.getConfig().getBoolean("debug", false);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.log(Level.INFO, "Loading managers...");
        this.playerManager = new PlayerManager(this);
        this.chatManager = new ChatManager(this);
        this.guiManager = new GuiManager(this);
        this.hologramManager = new HologramManager(this);
        this.entityManager = new EntityManager(this);
        this.npcManager = new NPCManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.gameManager = new GameManager(this);
        this.signManager = new SignManager(this);
        this.jumpManager = new JumpManager(this);
        this.cosmeticManager = new CosmeticManager(this);
        this.statsManager = new StatsManager(this);
        this.log(Level.INFO, "Managers loaded with success.");

        this.log(Level.INFO, "Registering packets packets...");
        SamaGamesAPI.get().getPubSub().subscribe("cheat", new SamaritanListener());
        SamaGamesAPI.get().getPubSub().subscribe("hubsChannel", new ArenaListener());
        SamaGamesAPI.get().getPubSub().subscribe("maintenanceSignChannel", new MaintenanceListener());
        this.log(Level.INFO, "Packets packets registered with success.");

        this.log(Level.INFO, "Registering events...");
        this.registerEvents();
        this.log(Level.INFO, "Events registered with success.");

        this.log(Level.INFO, "Registering commands...");
        new CommandManager(this);
        this.log(Level.INFO, "Commands registered with success.");

        this.log(Level.INFO, "Starting HubRefresher...");
        this.hubRefresher = new HubRefresher(this);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this.hubRefresher, 20L, 20L);
        this.log(Level.INFO, "Hubs list will be refreshed every seconds.");

        this.log(Level.INFO, "Hub ready!");
    }

    @Override
    public void onDisable()
    {
        this.npcManager.onServerClose();
        this.hologramManager.onServerClose();
        this.scoreboardManager.onServerClose();
    }

    public void registerEvents()
    {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
        Bukkit.getPluginManager().registerEvents(new JumpListener(), this);

        Bukkit.getPluginManager().registerEvents(new EntityEditionListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryEditionListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEditionListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldEditionListener(), this);
    }

    public void log(AbstractManager manager, Level level, String message)  { this.getLogger().log(level, "[" + manager.getName() + "] " + message); }

    public void log(Level level, String message) { this.getLogger().log(level, "[Core] " + message); }

    public PlayerManager getPlayerManager() { return this.playerManager; }
    public ChatManager getChatManager() { return this.chatManager; }
    public GuiManager getGuiManager() { return this.guiManager; }
    public HologramManager getHologramManager() { return this.hologramManager; }
    public EntityManager getEntityManager() { return this.entityManager; }
    public NPCManager getNPCManager() { return this.npcManager; }
    public ScoreboardManager getScoreboardManager() { return this.scoreboardManager; }
    public GameManager getGameManager() { return this.gameManager; }
    public SignManager getSignManager() { return this.signManager; }
    public JumpManager getJumpManager() { return this.jumpManager; }
    public CosmeticManager getCosmeticManager() { return this.cosmeticManager; }
    public StatsManager getStatsManager() { return this.statsManager; }

    public World getHubWorld()
    {
        return this.hubWorld;
    }

    public HubRefresher getHubRefresher()
    {
        return this.hubRefresher;
    }

    public EffectLib getEffectLib()
    {
        Plugin effectLib = Bukkit.getPluginManager().getPlugin("EffectLib");

        if (effectLib == null || !(effectLib instanceof EffectLib))
            return null;

        return (EffectLib) effectLib;
    }

    public boolean isDebugEnabled()
    {
        return this.debug;
    }
}
