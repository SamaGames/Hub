package net.samagames.hub.cosmetics.gadgets.displayers;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.HelixLocationEffect;
import de.slikey.effectlib.util.ParticleEffect;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.hub.utils.SimpleBlock;
import net.samagames.tools.ColorUtils;
import org.bukkit.*;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;

public class NukeDisplayer extends AbstractDisplayer
{
    private BukkitTask loopFirst;
    private BukkitTask loopSecond;
    private HelixNukeEffect helixNukeEffect;

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
    }

    @Override
    public void display()
    {
        for (Location block : this.blocksUsed.keySet())
        {
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }

        this.player.teleport(this.baseLocation.clone().subtract(0.5D, 0.0D, 0.5D).add(0.0D, 2.0D, 0.0D));

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

        this.helixNukeEffect = new HelixNukeEffect(Hub.getInstance().getCosmeticManager().getParticleManager().getEffectManager(), this.baseLocation.subtract(0.5D, 0.0D, 0.5D));
        this.helixNukeEffect.start();

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

                Ocelot ocelot = baseLocation.getWorld().spawn(baseLocation.clone().subtract(0.5D, 0.0D, 0.5D).add(0.0D, 3.0D, 0.0D), Ocelot.class);
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
            }
        }, 1L, 1L);
    }

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        this.loopSecond.cancel();
        this.helixNukeEffect.cancel();
    }

    private class HelixNukeEffect extends HelixLocationEffect
    {
        public HelixNukeEffect(EffectManager effectManager, Location location)
        {
            super(effectManager, location);
            this.particle = ParticleEffect.CRIT;
        }
    }
}
