package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.hub.utils.SimpleBlock;
import net.samagames.tools.ParticleEffect;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class TrampoSlimeDisplayer extends AbstractDisplayer
{
    private BukkitTask loopTask;
    private HashMap<Location, SimpleBlock> corners = new HashMap<>();

    public TrampoSlimeDisplayer(Player player)
    {
        super(player);

        this.corners.put(this.baseLocation.clone().add(2.0D, 0.0D, 0.0D).subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.GREEN.getData()));
        this.corners.put(this.baseLocation.clone().add(2.0D, 0.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.GREEN.getData()));
        this.corners.put(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.GREEN.getData()));
        this.corners.put(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.GREEN.getData()));

        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));

        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 0.0D).subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));

        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation, new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));

        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.SLIME_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));

        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));

        this.addBlocksToUse(this.corners);
    }

    @Override
    public void display()
    {
        for (Location block : this.blocksUsed.keySet())
        {
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }

        this.player.teleport(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D));

        this.loopTask = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), new Runnable()
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
                }

                ParticleEffect.SLIME.display(1.0F, 1.0F, 1.0F, 0.25F, 2, baseLocation.clone().add(0.0D, 0.5D, 0.0D), 160.0);

                if (timer == 10 && ticks == 0)
                {
                    FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.ORANGE).withFade(Color.YELLOW).withFlicker().build();

                    for (Location block : corners.keySet())
                    {
                        block.getBlock().setData(DyeColor.YELLOW.getData());
                        FireworkUtils.launchfw(block.clone().add(0.0D, 1.0D, 0.0D), effect);
                    }
                }
                else if (timer == 20 && ticks == 0)
                {
                    FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.RED).withFade(Color.FUCHSIA).withFlicker().build();

                        for (Location block : corners.keySet())
                        {
                            block.getBlock().setData(DyeColor.RED.getData());
                            FireworkUtils.launchfw(block.clone().add(0.0D, 1.0D, 0.0D), effect);
                        }
                }
                else if (timer == 30 && ticks == 0)
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

    @Override
    public boolean isInteractionsEnabled()
    {
        return false;
    }

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        this.loopTask.cancel();
    }
}
