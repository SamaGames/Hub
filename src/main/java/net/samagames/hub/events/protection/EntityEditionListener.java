package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;

public class EntityEditionListener implements Listener
{
    private final Hub hub;

    public EntityEditionListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityCombustByBlock(EntityCombustByBlockEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event)
    {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteract(EntityInteractEvent event)
    {
        if (event.getEntity().getType() != EntityType.PLAYER || !this.canDoAction((Player) event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTame(EntityTameEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosionPrime(ExplosionPrimeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFood(FoodLevelChangeEvent event)
    {
        if (event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event)
    {
        event.setCancelled(true);
    }

    private boolean canDoAction(Player player)
    {
        return this.hub.getPlayerManager().canBuild() && player != null && player.isOp();
    }
}
