package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.hub.utils.SimpleBlock;
import net.samagames.tools.ColorUtils;
import net.samagames.tools.ParticleEffect;
import org.bukkit.*;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;

public class NukeDisplayer extends AbstractDisplayer
{
    private BukkitTask loopFirst;
    private BukkitTask loopSecond;

    public NukeDisplayer(Player player)
    {
        super(player);

        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone(), new SimpleBlock(Material.SPONGE));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.BARRIER, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.BARRIER, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.BARRIER, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.BARRIER, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.BARRIER, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.BARRIER, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.BARRIER, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.BARRIER, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.BARRIER, 1));
    }

    @Override
    public void display()
    {
        for (Location block : this.blocksUsed.keySet())
        {
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }

        this.player.teleport(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 2.0D, 0.5D));

        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD + "Non ! " + player.getName() + " a lancé une bombe atomique à chat sur le monde ! Tous aux abris !");

        this.loopFirst = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), new Runnable()
        {
            int timer = 10;

            @Override
            public void run()
            {
                this.timer--;

                if (timer == 0)
                    timeToSendCatInTheHairLikeTheHandsInTheFamousSing();
                else if (timer <= 5)
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD + this.timer);

                for (Player player : Bukkit.getOnlinePlayers())
                    player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
            }
        }, 20L, 20L);
    }

    public void timeToSendCatInTheHairLikeTheHandsInTheFamousSing()
    {
        this.loopFirst.cancel();
        this.loopSecond = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), new Runnable()
        {
            int loops = 0;

            @Override
            public void run()
            {
                loops++;

                if (loops == 600)
                {
                    baseLocation.getWorld().createExplosion(baseLocation.getX(), baseLocation.getY(), baseLocation.getZ(), 10, false, false);

                    restore();
                    end();
                    callback();
                }
                else if (loops == 1)
                {
                    player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 5.0F, false, false);
                }

                Ocelot ocelot = baseLocation.getWorld().spawn(baseLocation.getBlock().getLocation().clone().add(0.5D, 3.0D, 0.5D), Ocelot.class);
                ocelot.setCatType(Ocelot.Type.values()[new Random().nextInt(Ocelot.Type.values().length)]);
                ocelot.setVelocity(new Vector(new Random().nextInt(8) - 4, 3, new Random().nextInt(8) - 4));
                ocelot.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Meow");
                ocelot.setCustomNameVisible(true);

                if(new Random().nextInt(5) == 3)
                    for (Player player : Bukkit.getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);

                Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
                {
                    Color a = ColorUtils.getColor(new Random().nextInt(17) + 1);
                    Color b = ColorUtils.getColor(new Random().nextInt(17) + 1);

                    FireworkEffect fw = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(a).withFade(b).build();
                    FireworkUtils.launchfw(ocelot.getLocation(), fw);

                    ocelot.setHealth(0);
                    ocelot.remove();
                }, 20L * 5);

                ParticleEffect.FLAME.display(0, 1.5F, 0, 0, 3, baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), 100.0D);
                ParticleEffect.FLAME.display(0, 1.5F, 0, 0, 3, baseLocation.clone().subtract(2.0D, 0.0D, 2.0D), 100.0D);
                ParticleEffect.FLAME.display(0, 1.5F, 0, 0, 3, baseLocation.clone().add(2.0D, 0.0D, 2.0D), 100.0D);
                ParticleEffect.FLAME.display(0, 1.5F, 0, 0, 3, baseLocation.clone().add(2.0D, 0.0D, 0.0D).subtract(0.0D, 0.0D, 2.0D), 100.0D);
            }
        }, 1L, 1L);

        new BukkitRunnable()
        {
            double t = Math.PI / 4;
            Location loc = baseLocation.getBlock().getLocation().clone().add(0.5D, 3.0D, 0.5D);

            public void run()
            {
                t = t + 0.1 * Math.PI;

                for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 32)
                {
                    double x = t * Math.cos(theta);
                    double y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
                    double z = t * Math.sin(theta);
                    loc.add(x, y, z);
                    ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, loc, 100.0D);
                    loc.subtract(x, y, z);

                    theta = theta + Math.PI / 64;

                    x = t * Math.cos(theta);
                    y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
                    z = t * Math.sin(theta);
                    loc.add(x, y, z);
                    ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(230, 126, 34), loc, 100.0D);
                    loc.subtract(x, y, z);
                }

                if (t > 30)
                    this.cancel();
            }
        }.runTaskTimerAsynchronously(Hub.getInstance(), 0, 1);
    }

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        this.loopSecond.cancel();
    }
}
