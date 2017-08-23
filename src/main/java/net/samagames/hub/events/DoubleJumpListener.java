package net.samagames.hub.events;

import net.samagames.hub.Hub;
import net.samagames.tools.ParticleEffect;
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

        if (event.getPlayer().getAllowFlight())
            return;

        if (this.hub.getPlayerManager().isBusy(event.getPlayer()))
            return;

        if (((LivingEntity) event.getPlayer()).isOnGround())
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

        this.allowed.remove(event.getPlayer().getUniqueId());

        if (this.hub.getPlayerManager().isBusy(event.getPlayer()))
            return;

        event.setCancelled(true);

        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(1.6D).setY(1.0D));
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0F, 1.0F);

        for (int i = 0; i < 20; i++)
            ParticleEffect.CLOUD.display(0.5F, 0.15F, 0.5F, 0.25F, 20, event.getPlayer().getLocation().subtract(0.0F, 0.20F, 0.0F));

        event.getPlayer().setAllowFlight(false);
    }
}
