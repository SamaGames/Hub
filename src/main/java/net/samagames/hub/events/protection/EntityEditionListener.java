package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;

public class EntityEditionListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityBreakDoorEvent(EntityBreakDoorEvent event)
    {
        if (Hub.getInstance().getPlayerManager().canBuild())
        {
            if (event.getEntity() instanceof Player)
            {
                if (event.getEntity().isOp())
                    event.setCancelled(false);
                else
                    event.setCancelled(true);
            }
            else
            {
                event.setCancelled(true);
            }
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityCombustByBlockEvent(EntityCombustByBlockEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeathEvent(EntityDeathEvent event)
    {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplodeEvent(EntityExplodeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteractEvent(EntityInteractEvent event)
    {
        if (Hub.getInstance().getPlayerManager().canBuild())
        {
            if (event.getEntity() instanceof Player)
            {
                if (event.getEntity().isOp())
                    event.setCancelled(false);
                else
                    event.setCancelled(true);
            }
            else
            {
                event.setCancelled(true);
            }
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTameEvent(EntityTameEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTargetEvent(EntityTargetEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosionPrimeEvent(ExplosionPrimeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFood(FoodLevelChangeEvent event)
    {
        if (event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlimeSplitEvent(SlimeSplitEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingBreakEvent(HangingBreakEvent event)
    {
        event.setCancelled(true);
    }
}
