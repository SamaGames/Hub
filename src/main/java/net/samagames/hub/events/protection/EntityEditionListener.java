package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    @EventHandler
    public void onArmorStandEdit(PlayerArmorStandManipulateEvent event)
    {
        if (!this.canDoAction(event.getPlayer()))
            event.setCancelled(true);
    }

    private boolean canDoAction(Player player)
    {
        return this.hub.getPlayerManager().canBuild() && player != null && player.isOp() && player.getGameMode() == GameMode.CREATIVE;
    }
}
