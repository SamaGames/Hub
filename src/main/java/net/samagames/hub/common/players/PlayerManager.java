package net.samagames.hub.common.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.InventoryUtils;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class PlayerManager extends AbstractManager
{
    static Hub HUB;

    public static final String SETTINGS_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Paramêtres" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String MODERATING_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Modération" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String SHOPPING_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Boutique" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String COSMETICS_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Cosmétique" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;

    public static final float WALK_SPEED = 0.3F;
    public static final float FLY_SPEED = 0.3F;

    private final Map<UUID, Location> selections;
    private final List<UUID> hiders;
    private final StaticInventory staticInventory;
    private final Location spawn;

    private boolean canBuild;

    public PlayerManager(Hub hub)
    {
        super(hub);

        HUB = hub;

        this.selections = new HashMap<>();
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
                player.teleport(this.spawn);
                InventoryUtils.cleanPlayer(player);

                this.staticInventory.setInventoryToPlayer(player);
                this.updateHiders(player);

                if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.fly"))
                    this.hub.getServer().getScheduler().runTask(this.hub, () -> player.setAllowFlight(true));

                if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.announce"))
                    this.hub.getServer().broadcastMessage(PlayerUtils.getFullyFormattedPlayerName(player) + ChatColor.YELLOW + " a rejoint le hub !");
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

        if (this.hub.getParkourManager().getPlayerParkour(player.getUniqueId()) != null)
            busy = true;

        if (this.hub.getInteractionManager().isInteracting(player))
            busy = true;

        // TODO: Complete

        return busy;
    }

    public boolean canBuild()
    {
        return this.canBuild;
    }
}
