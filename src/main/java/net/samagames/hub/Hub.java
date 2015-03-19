package net.samagames.hub;

import net.samagames.hub.common.managers.*;
import net.samagames.hub.events.player.*;
import net.samagames.hub.events.protection.*;
import net.samagames.hub.games.GameManager;
import net.samagames.hub.gui.GuiManager;
import net.samagames.hub.npcs.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Hub extends JavaPlugin
{
    private static Hub instance;

    private World hubWorld;

    private PlayerManager playerManager;
    private ChatManager chatManager;
    private GuiManager guiManager;
    private EntityManager entityManager;
    private NPCManager npcManager;
    private ScoreboardManager scoreboardManager;
    private GameManager gameManager;

    @Override
    public void onEnable()
    {
        instance = this;

        this.hubWorld = Bukkit.getWorlds().get(0);

        this.log(Level.INFO, "Loading managers...");
        this.playerManager = new PlayerManager(this);
        this.chatManager = new ChatManager(this);
        this.guiManager = new GuiManager(this);
        this.entityManager = new EntityManager(this);
        this.npcManager = new NPCManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.gameManager = new GameManager(this);
        this.log(Level.INFO, "Managers loaded with success.");

        this.log(Level.INFO, "Subscribing channels...");
        //SamaGamesAPI.get().getPubSub().subscribe("lobbysChannel", new ArenaSubscriber());
        this.log(Level.INFO, "Channels subscribed with success.");

        this.log(Level.INFO, "Registering events...");
        this.registerEvents();
        this.log(Level.INFO, "Events registered with success.");

        this.log(Level.INFO, "Hub ready!");
    }

    @Override
    public void onDisable()
    {

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
    public EntityManager getEntityManager() { return this.entityManager; }
    public NPCManager getNPCManager() { return this.npcManager; }
    public ScoreboardManager getScoreboardManager() { return this.scoreboardManager; }
    public GameManager getGameManager() { return this.gameManager; }

    public World getHubWorld()
    {
        return this.hubWorld;
    }

    public static Hub getInstance()
    {
        return instance;
    }
}
