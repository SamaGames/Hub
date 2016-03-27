package net.samagames.hub.common.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.InventoryUtils;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.PlayerUtils;
import net.samagames.tools.bossbar.BossBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerManager extends AbstractManager
{
    public static final float WALK_SPEED = 0.3F;
    public static final float FLY_SPEED = 0.3F;

    private final Map<UUID, Location> selections;
    private final StaticInventory staticInventory;
    private final Location spawn;

    private boolean canBuild;

    public PlayerManager(Hub hub)
    {
        super(hub);

        this.selections = new HashMap<>();
        this.staticInventory = new StaticInventory(hub);
        this.spawn = LocationUtils.str2loc(hub.getConfig().getString("spawn"));
        this.canBuild = false;

        this.spawn.getWorld().setSpawnLocation(this.spawn.getBlockX(), this.spawn.getBlockY(), this.spawn.getBlockZ());
    }

    @Override
    public void onDisable()
    {

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

        if (this.selections.containsKey(player.getUniqueId()))
            this.selections.remove(player.getUniqueId());
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

        // TODO: Complete

        return busy;
    }

    public boolean canBuild()
    {
        return this.canBuild;
    }
}
