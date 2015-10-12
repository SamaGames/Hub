package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class WorldEditionListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent event)
    {
        if (Hub.getInstance().getPlayerManager().canBuild())
        {
            if (!event.getPlayer().isOp())
                event.setCancelled(true);
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurnEvent(BlockBurnEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamageEvent(BlockDamageEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFadeEvent(BlockFadeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFormEvent(BlockFormEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromToEvent(BlockFromToEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockGrowEvent(BlockGrowEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgniteEvent(BlockIgniteEvent event)
    {
        if (Hub.getInstance().getPlayerManager().canBuild())
        {
            if (event.getPlayer() == null || !event.getPlayer().isOp())
                event.setCancelled(true);
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockMultiPlaceEvent(BlockMultiPlaceEvent event)
    {
        if (Hub.getInstance().getPlayerManager().canBuild())
        {
            if (!event.getPlayer().isOp())
                event.setCancelled(true);
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPhysicsEvent(BlockPhysicsEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event)
    {
        if (Hub.getInstance().getPlayerManager().canBuild())
        {
            if (!event.getPlayer().isOp())
                event.setCancelled(true);
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockSpreadEvent(BlockSpreadEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityBlockFormEvent(EntityBlockFormEvent event)
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
    public void onLeavesDecayEvent(LeavesDecayEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChangeEvent(WeatherChangeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkUnload(final ChunkUnloadEvent event)
    {
        event.setCancelled(true);
    }
}
