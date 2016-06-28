package net.samagames.hub.common.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.permissions.IPermissionsEntity;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.InventoryUtils;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.PlayerUtils;
import net.samagames.tools.chat.ActionBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.logging.Level;

public class PlayerManager extends AbstractManager
{
    static Hub HUB;

    public static final String SETTINGS_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Paramêtres" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String MODERATING_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Modération" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String SHOPPING_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Boutique" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String COSMETICS_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Cosmétique" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String RULES_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Règles" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;

    public static final float WALK_SPEED = 0.20F;
    public static final float FLY_SPEED = 0.15F;

    private final Map<UUID, Location> selections;
    private final Map<UUID, BukkitTask> rulesBookTasks;
    private final List<UUID> hiders;
    private final StaticInventory staticInventory;
    private final Location spawn;

    private boolean canBuild;

    public PlayerManager(Hub hub)
    {
        super(hub);

        HUB = hub;

        this.selections = new HashMap<>();
        this.rulesBookTasks = new HashMap<>();
        this.hiders = new ArrayList<>();
        this.staticInventory = new StaticInventory(hub);
        this.spawn = LocationUtils.str2loc(hub.getConfig().getString("spawn"));
        this.canBuild = false;

        this.spawn.getWorld().setSpawnLocation(this.spawn.getBlockX(), this.spawn.getBlockY(), this.spawn.getBlockZ());
    }

    @Override
    public void onDisable()
    {
        this.selections.clear();
        this.hiders.clear();

        this.rulesBookTasks.values().forEach(BukkitTask::cancel);
        this.rulesBookTasks.clear();
    }

    @Override
    public void onLogin(Player player)
    {
        this.log(Level.INFO, "Handling login from '" + player.getUniqueId() + "'...");

        this.hub.getScheduledExecutorService().execute(() ->
        {
            this.hub.getServer().getScheduler().runTask(this.hub, () ->
            {
                player.setGameMode(GameMode.ADVENTURE);
                player.setWalkSpeed(WALK_SPEED);
                player.setFlySpeed(FLY_SPEED);
                player.setAllowFlight(false);
                player.setFoodLevel(20);
                player.setHealth(20.0D);
                InventoryUtils.cleanPlayer(player);

                Jedis jedis = SamaGamesAPI.get().getBungeeResource();
                String key = "lastgame." + player.getUniqueId().toString();

                if (jedis == null || jedis.exists(key))
                {
                    String lastGame = jedis == null ? "TESTENV" : jedis.get(key);
                    if (jedis != null)
                        jedis.del(key);

                    if (this.hub.getGameManager().getGameByIdentifier(lastGame) != null)
                        player.teleport(this.hub.getGameManager().getGameByIdentifier(lastGame).getLobbySpawn());
                    else
                        player.teleport(this.spawn);
                }
                else
                {
                    player.teleport(this.spawn);
                }

                if (jedis != null)
                    jedis.close();

                player.getInventory().clear();

                try
                {
                    IPermissionsEntity permissionsEntity = SamaGamesAPI.get().getPermissionsManager().getPlayer(player.getUniqueId());

                    if (permissionsEntity.getGroupId() >= 3 && SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isElytraActivated())
                    {
                        ItemStack elytra = new ItemStack(Material.ELYTRA);
                        ItemMeta meta = elytra.getItemMeta();
                        meta.spigot().setUnbreakable(true);
                        elytra.setItemMeta(meta);

                        player.getInventory().setChestplate(elytra);
                    }
                }
                catch (NullPointerException ignored)
                {
                    player.sendMessage(ChatColor.RED + "Une erreur a été détectée lors du chargement de votre joueur, vous devrez peut-être vous reconnecter.");
                }

                this.staticInventory.setInventoryToPlayer(player);

                this.updateHiders(player);

                if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.fly"))
                    this.hub.getServer().getScheduler().runTask(this.hub, () -> player.setAllowFlight(true));

                if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.announce") && !SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasNickname())
                    this.hub.getServer().broadcastMessage(PlayerUtils.getFullyFormattedPlayerName(player) + ChatColor.YELLOW + " a rejoint le hub !");

                if (player.getUniqueId().equals(UUID.fromString("568046c8-6045-4c59-a255-28027aac8c33")))
                    ActionBarAPI.sendMessage(player, ChatColor.RED + "\u2764");
            });
        });
    }

    @Override
    public void onLogout(Player player)
    {
        this.log(Level.INFO, "Handling logout from '" + player.getUniqueId() + "'...");

        this.hub.getScheduledExecutorService().execute(() ->
        {
            if (this.selections.containsKey(player.getUniqueId()))
                this.selections.remove(player.getUniqueId());
        });
    }

    public void addHider(Player player)
    {
        this.hiders.add(player.getUniqueId());

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
                this.hub.getServer().getOnlinePlayers().stream().filter(p -> !SamaGamesAPI.get().getPermissionsManager().hasPermission(p, "hub.announce") && !SamaGamesAPI.get().getFriendsManager().areFriends(player.getUniqueId(), p.getUniqueId())).forEach(p ->
                        this.hub.getServer().getScheduler().runTask(this.hub, () -> player.hidePlayer(p))));
    }

    public void removeHider(Player player)
    {
        if(this.hiders.contains(player.getUniqueId()))
            this.hiders.remove(player.getUniqueId());

        this.hub.getServer().getScheduler().runTask(this.hub, () -> this.hub.getServer().getOnlinePlayers().forEach(player::showPlayer));
    }

    private void updateHiders(Player newConnected)
    {
        this.hub.getScheduledExecutorService().execute(() ->
        {
            List<UUID> uuidList = new ArrayList<>();
            uuidList.addAll(this.hiders);

            for (UUID uuid : uuidList)
            {
                Player player = this.hub.getServer().getPlayer(uuid);

                if(player != null && !player.equals(newConnected))
                    if (!SamaGamesAPI.get().getPermissionsManager().hasPermission(newConnected, "hub.announce") && !SamaGamesAPI.get().getFriendsManager().areFriends(newConnected.getUniqueId(), uuid))
                        this.hub.getServer().getScheduler().runTask(this.hub, () -> player.hidePlayer(newConnected));
            }
        });
    }

    public void setSelection(Player player, Location selection)
    {
        this.selections.put(player.getUniqueId(), selection);
    }

    public void setRulesBookTask(Player player, BukkitTask bukkitTask)
    {
        if (this.rulesBookTasks.containsKey(player.getUniqueId()))
        {
            this.rulesBookTasks.get(player.getUniqueId()).cancel();
            this.rulesBookTasks.remove(player.getUniqueId());
        }

        this.rulesBookTasks.put(player.getUniqueId(), bukkitTask);
    }

    public void removeRulesBookTask(Player player)
    {
        if (this.rulesBookTasks.containsKey(player.getUniqueId()))
        {
            this.rulesBookTasks.get(player.getUniqueId()).cancel();
            this.rulesBookTasks.remove(player.getUniqueId());
        }
    }

    public void setBuild(boolean canBuild)
    {
        this.canBuild = canBuild;
    }

    public Location getSpawn()
    {
        return this.spawn;
    }

    public StaticInventory getStaticInventory()
    {
        return this.staticInventory;
    }

    public Location getSelection(Player player)
    {
        return this.selections.containsKey(player.getUniqueId()) ? this.selections.get(player.getUniqueId()) : null;
    }

    public boolean isBusy(Player player)
    {
        boolean busy = false;

        if (this.hub.getGuiManager().getPlayerGui(player.getUniqueId()) != null)
            busy = true;
        else if (this.hub.getParkourManager().getPlayerParkour(player.getUniqueId()) != null)
            busy = true;
        else if (this.hub.getInteractionManager().isInteracting(player))
            busy = true;
        else if (this.hub.getCosmeticManager().getGadgetManager().hasGadget(player))
            busy = true;

        return busy;
    }

    public boolean canBuild()
    {
        return this.canBuild;
    }
}
