package net.samagames.hub.events;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DoubleJumpListener implements Listener
{
    private final List<UUID> allowed;
    private final Hub hub;

    public DoubleJumpListener(Hub hub)
    {
        this.allowed = new ArrayList<>();
        this.hub = hub;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;

        if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.fly"))
            return;

        if (this.hub.getPlayerManager().isBusy(event.getPlayer()))
            return;

        if (((LivingEntity) event.getPlayer()).isOnGround() && this.hub.getParkourManager().getPlayerParkour(event.getPlayer().getUniqueId()) == null)
        {
            event.getPlayer().setAllowFlight(true);
            this.allowed.add(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event)
    {
        if (!this.allowed.contains(event.getPlayer().getUniqueId()))
            return;

        event.setCancelled(true);
        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(1.6D).setY(1.0D));
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0F, 1.0F);
        event.getPlayer().setAllowFlight(false);

        this.allowed.remove(event.getPlayer().getUniqueId());
    }
}
