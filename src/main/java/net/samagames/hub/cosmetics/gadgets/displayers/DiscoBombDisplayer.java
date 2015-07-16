package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.utils.MusicUtils;
import net.samagames.hub.utils.SimpleBlock;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;

public class DiscoBombDisplayer extends AbstractDisplayer
{
    private int loopId;
    private boolean music;

    public DiscoBombDisplayer(Player player)
    {
        super(player);

        this.music = false;
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
        Hub.getInstance().getGuiManager().closeGui(this.player);

        for (Location block : this.blocksUsed.keySet())
        {
            block.getBlock().setType(this.blocksUsed.get(block).getType());

            if(this.blocksUsed.get(block).getType() == Material.STAINED_GLASS)
            {
                DyeColor random = DyeColor.values()[new Random().nextInt(DyeColor.values().length)];
                block.getBlock().setData(random.getData());
            }
        }

        if(Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong() == null)
        {
            MusicUtils.playRecord(this.baseLocation, Material.RECORD_7);
            this.music = true;
        }

        this.loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Hub.getInstance(), new Runnable()
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

                    blocksUsed.keySet().stream().filter(block -> block.getBlock().getType() != Material.GLOWSTONE).forEach(block ->
                    {
                        DyeColor random = DyeColor.values()[new Random().nextInt(DyeColor.values().length)];
                        block.getBlock().setType(Material.STAINED_GLASS);
                        block.getBlock().setData(random.getData());
                    });
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

    @Override
    public void handleInteraction(Entity who, Entity with) {}

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        Bukkit.getScheduler().cancelTask(this.loopId);

        if(this.music)
            MusicUtils.stopRecord(this.baseLocation);
    }
}
