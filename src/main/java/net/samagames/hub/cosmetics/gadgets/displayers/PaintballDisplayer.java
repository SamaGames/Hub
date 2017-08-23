package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.tools.SimpleBlock;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
public class PaintballDisplayer extends AbstractDisplayer implements Listener
{
    private final UUID uuid;

    public PaintballDisplayer(Hub hub, Player player)
    {
        super(hub, player);

        this.uuid = UUID.randomUUID();

        hub.getServer().getPluginManager().registerEvents(this, hub);
    }

    @Override
    public void display()
    {
        new BukkitRunnable()
        {
            private int times = 0;

            @Override
            public void run()
            {
                Snowball snowball = PaintballDisplayer.this.player.launchProjectile(Snowball.class);
                snowball.setVelocity(snowball.getVelocity().multiply(1.5));
                snowball.setMetadata("paintball-ball", new FixedMetadataValue(PaintballDisplayer.this.hub, PaintballDisplayer.this.uuid.toString()));

                this.times++;

                if (this.times == 30)
                {
                    PaintballDisplayer.this.restore();
                    PaintballDisplayer.this.end();

                    HandlerList.unregisterAll(PaintballDisplayer.this);

                    this.cancel();
                }
            }
        }.runTaskTimer(this.hub, 5L, 5L);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event)
    {
        if (event.getEntity().getType() != EntityType.SNOWBALL || !event.getEntity().hasMetadata("paintball-ball") || !event.getEntity().getMetadata("paintball-ball").get(0).asString().equals(this.uuid.toString()))
            return;

        for (Block block : getNearbyBlocks(event.getEntity().getLocation(), 2))
        {
            if (block.getType() == Material.AIR || block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
                continue;

            if (this.isBlockGloballyUsed(block.getLocation()))
                continue;

            SimpleBlock simpleBlock = new SimpleBlock(Material.STAINED_CLAY, DyeColor.values()[new Random().nextInt(DyeColor.values().length)].getWoolData());
            this.addBlockToUse(block.getLocation(), simpleBlock);

            block.setType(simpleBlock.getType());
            block.setData(simpleBlock.getData());
        }

        event.getEntity().remove();
    }

    @Override
    public void handleInteraction(Entity who, Entity with) {}

    @Override
    public boolean isInteractionsEnabled()
    {
        return false;
    }

    @Override
    public boolean canUse()
    {
        return true;
    }

    private static List<Block> getNearbyBlocks(Location location, int radius)
    {
        List<Block> blocks = new ArrayList<>();

        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++)
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++)
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++)
                    blocks.add(location.getWorld().getBlockAt(x, y, z));

        return blocks;
    }
}
