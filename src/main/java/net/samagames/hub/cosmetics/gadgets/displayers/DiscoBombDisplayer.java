package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.lobbyutils.Plugin;
import net.samagames.lobbyutils.utils.ParticleEffect;
import net.samagames.lobbyutils.utils.SimpleBlock;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Random;

public class DiscoBombDisplayer extends AbstractDisplayer
{
    private int loopId;

    public DiscoBombDisplayer(Player player)
    {
        super(player);

        this.baseLocation = player.getLocation().add(0.0D, 6.0D, 0.0D);

        this.addBlockToUse(this.baseLocation, new SimpleBlock(Material.GLOWSTONE));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.STAINED_GLASS));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.STAINED_GLASS));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_GLASS));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_GLASS));
    }

    @Override
    public void display()
    {
        for (Location block : this.blocksUsed.keySet())
        {
            block.getBlock().setType(this.blocksUsed.get(block).getType());

            if(this.blocksUsed.get(block).getType() == Material.STAINED_GLASS)
            {
                DyeColor random = DyeColor.values()[new Random().nextInt(DyeColor.values().length)];
                block.getBlock().setData(random.getData());
            }
        }

        this.loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.instance, new Runnable()
        {
            int timer = 0;
            int ticks = 0;

            @Override
            public void run()
            {
                this.ticks++;

                if (ticks == 20)
                {
                    this.ticks = 0;
                    this.timer++;

                    for (Location block : blocksUsed.keySet())
                    {
                        if(block.getBlock().getType() != Material.GLOWSTONE)
                        {
                            DyeColor random = DyeColor.values()[new Random().nextInt(DyeColor.values().length)];
                            block.getBlock().setType(Material.STAINED_GLASS);
                            block.getBlock().setData(random.getData());
                        }
                    }
                }

                ParticleEffect.NOTE.display(1.0F, 1.0F, 1.0F, 0.25F, 2, baseLocation, 160.0);

                if (timer == 15)
                {
                    baseLocation.getWorld().createExplosion(baseLocation.getX(), baseLocation.getY(), baseLocation.getZ(), 5, false, false);
                    restore();
                    end();
                    callback();
                }
            }
        }, 1L, 1L);
    }

    public boolean canUse()
    {
        return Plugin.noteBlockMachine.getCurrentSong() != null;
    }

    private void callback() {
        Bukkit.getScheduler().cancelTask(this.loopId);
    }
}
