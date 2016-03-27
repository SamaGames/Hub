package net.samagames.hub;

import net.samagames.hub.common.HubRefresher;
import net.samagames.hub.common.hydroangeas.HydroangeasManager;
import net.samagames.hub.common.managers.EntityManager;
import net.samagames.hub.common.managers.EventBus;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.common.tasks.TaskManager;
import net.samagames.hub.events.GuiListener;
import net.samagames.hub.events.ParkourListener;
import net.samagames.hub.events.PlayerListener;
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
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Hub extends JavaPlugin
{
    private World world;

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    private HubRefresher hubRefresher;

    private EventBus eventBus;
    private HydroangeasManager hydroangeasManager;
    private TaskManager taskManager;
    private EntityManager entityManager;
    private PlayerManager playerManager;
    private GameManager gameManager;
    private SignManager signManager;
    private ScoreboardManager scoreboardManager;
    private GuiManager guiManager;
    private ParkourManager parkourManager;
    private InteractionManager interactionManager;

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();

        File interactionsDirectory = new File(this.getDataFolder(), "interactions");

        if (!interactionsDirectory.exists())
            interactionsDirectory.mkdir();

        this.world = this.getServer().getWorlds().get(0);
        this.world.setGameRuleValue("randomTickSpeed", "0");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setTime(this.getConfig().getLong("time", 6000L));

        this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
        this.executorMonoThread = Executors.newScheduledThreadPool(1);

        if (!this.getConfig().getBoolean("disconnected", false))
            this.hubRefresher = new HubRefresher(this);

        this.eventBus = new EventBus();
        this.hydroangeasManager = new HydroangeasManager(this);
        this.taskManager = new TaskManager(this);
        this.entityManager = new EntityManager(this);
        this.playerManager = new PlayerManager(this);
        this.gameManager = new GameManager(this);
        this.signManager = new SignManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.guiManager = new GuiManager(this);
        this.parkourManager = new ParkourManager(this);
        this.interactionManager = new InteractionManager(this);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ParkourListener(this), this);
        this.getServer().getPluginManager().registerEvents(new GuiListener(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityEditionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryEditionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerProtectionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new WorldEditionListener(this), this);
    }

    @Override
    public void onDisable()
    {
        if (this.hubRefresher != null)
            this.hubRefresher.removeFromList();

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
    public GameManager getGameManager() { return this.gameManager; }
    public SignManager getSignManager() { return this.signManager; }
    public ScoreboardManager getScoreboardManager() { return this.scoreboardManager; }
    public GuiManager getGuiManager() { return this.guiManager; }
    public ParkourManager getParkourManager() { return this.parkourManager; }
    public InteractionManager getInteractionManager() { return this.interactionManager; }
}
