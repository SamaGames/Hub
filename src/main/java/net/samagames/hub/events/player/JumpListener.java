package net.samagames.hub.events.player;

import net.samagames.hub.Hub;
import net.samagames.hub.jump.Jump;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JumpListener implements Listener
{
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getAction().equals(Action.PHYSICAL))
        {
            if (event.getClickedBlock().getType().equals(Material.IRON_PLATE))
            {
                Jump jump = Hub.getInstance().getJumpManager().getOfPlayer(event.getPlayer().getUniqueId());

                if (jump != null)
                {
                    if (jump.getEnd().equals(event.getClickedBlock()))
                    {
                        jump.winPlayer(event.getPlayer());
                        return;
                    }
                    else if (jump.getBegin().equals(event.getClickedBlock()))
                    {
                        return;
                    }
                    else
                    {
                        jump.removePlayer(event.getPlayer().getUniqueId());
                    }
                }

                for (Jump jumpp : Hub.getInstance().getJumpManager().getJumps())
                {
                    if (jumpp.getBegin().equals(event.getClickedBlock()))
                    {
                        jumpp.addPlayer(event.getPlayer());
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLogout(final PlayerQuitEvent event)
    {
        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
        {
            Jump jump = Hub.getInstance().getJumpManager().getOfPlayer(event.getPlayer().getUniqueId());

            if (jump != null)
                jump.removePlayer(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler
    public void onFall(final EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();

            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause() == EntityDamageEvent.DamageCause.VOID)
            {
                Location loc = player.getLocation();
                Block block = loc.getBlock();
                loc.setY(loc.getY() - 1);

                Jump jump = Hub.getInstance().getJumpManager().getOfPlayer(player.getUniqueId());

                if (jump == null)
                    return;

                if (block == null || !jump.inWhitelist(block.getType()))
                {
                    jump.losePlayer(player);
                }
            }
        }
    }
}
