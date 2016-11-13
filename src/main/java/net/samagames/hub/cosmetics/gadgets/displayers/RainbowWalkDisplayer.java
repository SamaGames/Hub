package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.tools.SimpleBlock;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 13/11/2016
 */
public class RainbowWalkDisplayer extends AbstractDisplayer
{
    public RainbowWalkDisplayer(Hub hub, Player player)
    {
        super(hub, player);
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
                if (this.times < 50)
                {
                    Block block = RainbowWalkDisplayer.this.player.getLocation().getBlock().getRelative(BlockFace.DOWN);

                    if (block.getType() != Material.AIR && !RainbowWalkDisplayer.this.isBlockGloballyUsed(block.getLocation()))
                    {
                        SimpleBlock simpleBlock = new SimpleBlock(Material.STAINED_CLAY, DyeColor.values()[new Random().nextInt(DyeColor.values().length)].getWoolData());
                        RainbowWalkDisplayer.this.addBlockToUse(block.getLocation(), simpleBlock);

                        block.setType(simpleBlock.getType());
                        block.setData(simpleBlock.getData());

                        RainbowWalkDisplayer.this.hub.getServer().getScheduler().runTaskLater(RainbowWalkDisplayer.this.hub, () -> RainbowWalkDisplayer.this.restore(block.getLocation()), 20L * 5);
                    }
                }
                else if (this.times == 150)
                {
                    RainbowWalkDisplayer.this.restore();
                    RainbowWalkDisplayer.this.end();

                    this.cancel();
                }

                this.times++;
            }
        }.runTaskTimer(this.hub, 5L, 5L);
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
}
