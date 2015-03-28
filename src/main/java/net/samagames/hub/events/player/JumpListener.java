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
            if (event.getClickedBlock().getType().equals(Material.IRON_PLATE))
                Hub.getInstance().getJumpManager().onPressurePlatePressed(event.getPlayer(), event.getClickedBlock().getLocation());
    }

    @EventHandler
    public void onLogout(final PlayerQuitEvent event)
    {
        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () -> Hub.getInstance().getJumpManager().logout(event.getPlayer().getUniqueId()));
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
                    Hub.getInstance().getJumpManager().onFall(player);
            }
        }
    }
}
