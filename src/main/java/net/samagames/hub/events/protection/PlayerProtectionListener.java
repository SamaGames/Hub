package net.samagames.hub.events.protection;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class PlayerProtectionListener implements Listener
{
    private final Hub hub;

    public PlayerProtectionListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        if (!this.canDoAction(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().getType() == Material.WRITTEN_BOOK)
            return;

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.sign.selection"))
            {
                if (event.getItem() != null && event.getItem().getType() == Material.WOOD_AXE)
                {
                    Action act = event.getAction();

                    if (act == Action.LEFT_CLICK_BLOCK)
                    {
                        this.hub.getPlayerManager().setSelection(event.getPlayer(), event.getClickedBlock().getLocation());
                        event.getPlayer().sendMessage(ChatColor.GOLD + "Point sélectionné !");
                    }
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (event.getTo().getX() > 250 || event.getTo().getX() < -350 || event.getTo().getZ() > 620 || event.getTo().getZ() < -350)
        {
            event.getPlayer().sendMessage(ChatColor.RED + "Mais où allez-vous comme ça ?");
            event.setCancelled(true);
            this.hub.getServer().getScheduler().runTaskLater(this.hub, () -> {
                event.getPlayer().teleport(this.hub.getPlayerManager().getSpawn());
                if (event.getPlayer().isFlying())
                    event.getPlayer().setFlying(false);
                if (event.getPlayer().isGliding())
                    ((CraftPlayer)event.getPlayer()).getHandle().setFlag(7, false);
            }, 1);
        }
    }

    private boolean canDoAction(Player player)
    {
        return this.hub.getPlayerManager().canBuild() && player != null && player.isOp() && player.getGameMode() == GameMode.CREATIVE;
    }
}
