package net.samagames.hub;

import de.slikey.effectlib.EffectLib;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import net.samagames.api.stats.IStatsManager;
import net.samagames.hub.commands.CommandManager;
import net.samagames.hub.common.HubRefresher;
import net.samagames.hub.common.hydroangeas.HydroangeasManager;
import net.samagames.hub.common.managers.EntityManager;
import net.samagames.hub.common.managers.EventBus;
import net.samagames.hub.common.players.ChatManager;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.common.tasks.TaskManager;
import net.samagames.hub.cosmetics.CosmeticManager;
import net.samagames.hub.events.*;
import net.samagames.hub.events.protection.EntityEditionListener;
import net.samagames.hub.events.protection.InventoryEditionListener;
import net.samagames.hub.events.protection.PlayerProtectionListener;
import net.samagames.hub.events.protection.WorldEditionListener;
import net.samagames.hub.games.GameManager;
import net.samagames.hub.games.signs.SignManager;
import net.samagames.hub.gui.GuiManager;
import net.samagames.hub.interactions.InteractionManager;
import net.samagames.hub.parkours.ParkourManager;
import net.samagames.hub.scoreboards.ScoreboardManager;
import net.samagames.hub.utils.ServerStatus;
import net.samagames.tools.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Hub extends JavaPlugin
{
    private World world;

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    private HubRefresher hubRefresher;
    private BukkitTask hubRefresherTask;

    private EventBus eventBus;
    private HydroangeasManager hydroangeasManager;
    private TaskManager taskManager;
    private EntityManager entityManager;
    private PlayerManager playerManager;
    private ChatManager chatManager;
    private GameManager gameManager;
    private SignManager signManager;
    private ScoreboardManager scoreboardManager;
    private GuiManager guiManager;
    private ParkourManager parkourManager;
    private InteractionManager interactionManager;
    private CosmeticManager cosmeticManager;
    private CommandManager commandManager;

    private ScheduledFuture hydroangeasSynchronization;

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();

        this.world = this.getServer().getWorlds().get(0);
        this.world.setGameRuleValue("randomTickSpeed", "0");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setTime(this.getConfig().getLong("time", 6000L));

        this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
        this.executorMonoThread = Executors.newScheduledThreadPool(1);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        for (IStatsManager.StatsNames statName : IStatsManager.StatsNames.values())
        {
            SamaGamesAPI.get().getShopsManager().setShopToLoad(statName, true);
            SamaGamesAPI.get().getStatsManager().setStatsToLoad(statName, true);
        }

        if (!this.getConfig().getBoolean("disconnected", false))
        {
            this.hubRefresher = new HubRefresher(this);
            this.hubRefresherTask = this.getServer().getScheduler().runTaskTimerAsynchronously(this, this.hubRefresher, 20L, 20L);
        }

        this.eventBus = new EventBus();
        this.hydroangeasManager = new HydroangeasManager(this);
        this.taskManager = new TaskManager(this);
        this.entityManager = new EntityManager(this);
        this.playerManager = new PlayerManager(this);
        this.chatManager = new ChatManager(this);
        this.gameManager = new GameManager(this);
        this.signManager = new SignManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.guiManager = new GuiManager(this);
        this.parkourManager = new ParkourManager(this);
        this.interactionManager = new InteractionManager(this);
        this.cosmeticManager = new CosmeticManager(this);
        this.commandManager = new CommandManager(this);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ParkourListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GuiListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DoubleJumpListener(), this);
        this.getServer().getPluginManager().registerEvents(new PetListener(), this);

        this.getServer().getPluginManager().registerEvents(new EntityEditionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryEditionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerProtectionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new WorldEditionListener(this), this);

        this.hydroangeasSynchronization = this.getScheduledExecutorService().scheduleAtFixedRate(() -> new ServerStatus(SamaGamesAPI.get().getServerName(), "Hub", "Map", Status.IN_GAME, Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()).sendToHydro(), 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable()
    {
        if (this.hubRefresher != null)
        {
            this.hubRefresherTask.cancel();
            this.hubRefresher.removeFromList();
        }

        this.hydroangeasSynchronization.cancel(true);
        
        this.eventBus.onDisable();
    }

    public World getWorld()
    {
        return this.world;
    }

    public ScheduledExecutorService getExecutorMonoThread()
    {
        return this.executorMonoThread;
    }

    public ScheduledExecutorService getScheduledExecutorService()
    {
        return this.scheduledExecutorService;
    }

    public HubRefresher getHubRefresher()
    {
        return this.hubRefresher;
    }

    public EventBus getEventBus()
    {
        return this.eventBus;
    }

    public HydroangeasManager getHydroangeasManager() { return this.hydroangeasManager; }
    public TaskManager getTaskManager() { return this.taskManager; }
    public EntityManager getEntityManager() { return this.entityManager; }
    public PlayerManager getPlayerManager() { return this.playerManager; }
    public ChatManager getChatManager() { return this.chatManager; }
    public GameManager getGameManager() { return this.gameManager; }
    public SignManager getSignManager() { return this.signManager; }
    public ScoreboardManager getScoreboardManager() { return this.scoreboardManager; }
    public GuiManager getGuiManager() { return this.guiManager; }
    public ParkourManager getParkourManager() { return this.parkourManager; }
    public InteractionManager getInteractionManager() { return this.interactionManager; }
    public CosmeticManager getCosmeticManager() { return this.cosmeticManager; }
    public CommandManager getCommandManager() { return this.commandManager; }

    public EffectLib getEffectLib()
    {
        Plugin effectLib = Bukkit.getPluginManager().getPlugin("EffectLib");

        if (effectLib == null || !(effectLib instanceof EffectLib))
            return null;

        return (EffectLib) effectLib;
    }
}
