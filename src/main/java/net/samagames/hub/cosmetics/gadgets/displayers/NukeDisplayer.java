package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.hub.utils.SimpleBlock;
import net.samagames.tools.ColorUtils;
import org.bukkit.*;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class NukeDisplayer extends AbstractDisplayer
{
    private BukkitTask loopFirst;
    private BukkitTask loopSecond;

    public NukeDisplayer(Player player)
    {
        super(player);

        HashMap<Location, SimpleBlock> firstLayer = new HashMap<>();
        firstLayer.put(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        firstLayer.put(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().add(2.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        firstLayer.put(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        firstLayer.put(this.baseLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        firstLayer.put(this.baseLocation.clone().add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        firstLayer.put(this.baseLocation.clone().add(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        firstLayer.put(this.baseLocation.clone(), new SimpleBlock(Material.SPONGE));
        firstLayer.put(this.baseLocation.clone().add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        firstLayer.put(this.baseLocation.clone().add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        firstLayer.put(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        firstLayer.put(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        firstLayer.put(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(2.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        firstLayer.put(this.baseLocation.clone().subtract(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        firstLayer.put(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));

        HashMap<Location, SimpleBlock> secondLayer = new HashMap<>();
        secondLayer.put(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        secondLayer.put(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        secondLayer.put(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        secondLayer.put(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        secondLayer.put(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        secondLayer.put(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        secondLayer.put(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        secondLayer.put(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));

        HashMap<Location, SimpleBlock> thirdLayer = new HashMap<>();
        thirdLayer.put(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        thirdLayer.put(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        thirdLayer.put(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        thirdLayer.put(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        thirdLayer.put(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        thirdLayer.put(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        thirdLayer.put(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        thirdLayer.put(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));

        HashMap<Location, SimpleBlock> fourthLayer = new HashMap<>();
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D).add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D).subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_GLASS, DyeColor.WHITE.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 3.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));

        HashMap<Location, SimpleBlock> fifthLayer = new HashMap<>();
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 4.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 4.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 4.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));
        fourthLayer.put(this.baseLocation.clone().add(0.0D, 4.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.HARD_CLAY, DyeColor.RED.getData()));

        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 5.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 6.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 7.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 8.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));

        this.addBlocksToUse(firstLayer);
        this.addBlocksToUse(secondLayer);
        this.addBlocksToUse(thirdLayer);
        this.addBlocksToUse(fourthLayer);
        this.addBlocksToUse(fifthLayer);
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

        final Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD + "Non ! " + player.getName() + " a lancé une bombe atomique à chat sur le monde ! Tous aux abris !");

        this.loopFirst = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), new Runnable() {
            int timer = 10;

            @Override
            public void run() {
                this.timer--;

                if (timer == 0)
                    timeToSendCatInTheHairLikeTheHandsInTheFamousSing();
                else if (timer <= 5)
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD + this.timer);

                for (Player player : players)
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

                if (loops == 500)
                {
                    restore();
                    end();
                    callback();
                }
                else if (loops == 1)
                {
                    player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 5.0F, false, false);
                }

                Ocelot ocelot = player.getWorld().spawn(player.getLocation().add(0.0D, 7.0D, 0.0D), Ocelot.class);
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
                }, 20L * 3);
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
    }
}
