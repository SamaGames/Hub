package net.samagames.hub.cosmetics.gadgets.displayers;

import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.*;
import net.minecraft.server.v1_9_R2.BossBattleServer;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.tools.ColorUtils;
import net.samagames.tools.ParticleEffect;
import net.samagames.tools.SimpleBlock;
import net.samagames.tools.bossbar.BossBarAPI;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_9_R2.boss.CraftBossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class NukeDisplayer extends AbstractDisplayer
{
    private static final String TAG = ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD;

    private CraftBossBar meowBossBar;
    private BukkitTask loopFirst;
    private BukkitTask loopSecond;

    public NukeDisplayer(Hub hub, Player player)
    {
        super(hub, player);

        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.PURPLE.getData()));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.PURPLE.getData()));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone(), new SimpleBlock(Material.PURPUR_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.PURPLE.getData()));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.PURPLE.getData()));
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

        this.hub.getServer().broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD + "Non ! " + this.player.getName() + " a lancé une bombe atomique à chat sur le monde ! Tous aux abris !");

        this.meowBossBar = new CraftBossBar(ChatColor.RED + "Meow", BarColor.RED, BarStyle.SEGMENTED_12);
        this.hub.getServer().getOnlinePlayers().forEach(this.meowBossBar::addPlayer);

        this.loopFirst = this.hub.getServer().getScheduler().runTaskTimerAsynchronously(this.hub, new Runnable()
        {
            int timer = 10;

            @Override
            public void run()
            {
                this.timer--;

                if (this.timer == 0)
                    timeToSendCatInTheHairLikeTheHandsInTheFamousSing();
                else if (this.timer <= 5)
                    hub.getServer().broadcastMessage(TAG + this.timer);

                meowBossBar.setProgress((100.0D - (this.timer * 10)) / 100);

                for (Player player : hub.getServer().getOnlinePlayers())
                    player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
            }
        }, 20L, 20L);
    }

    @Override
    public void handleInteraction(Entity who, Entity with) {}

    private void timeToSendCatInTheHairLikeTheHandsInTheFamousSing()
    {
        this.loopFirst.cancel();
        this.meowBossBar.setProgress(1.0D);

        TornadoEffect tornadoEffect = new TornadoEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        tornadoEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 1.5D, 0.5D));
        tornadoEffect.showCloud = false;
        tornadoEffect.yOffset = 0.0D;
        tornadoEffect.tornadoHeight = 15.0F;
        tornadoEffect.tornadoParticle = de.slikey.effectlib.util.ParticleEffect.FIREWORKS_SPARK;
        tornadoEffect.start();

        AtomEffect atomEffect = new AtomEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        atomEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 1.5D, 0.5D));
        atomEffect.particleNucleus = de.slikey.effectlib.util.ParticleEffect.FLAME;
        atomEffect.particleOrbital = de.slikey.effectlib.util.ParticleEffect.PORTAL;
        atomEffect.infinite();
        atomEffect.start();

        HelixEffect helixEffect = new HelixEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        helixEffect.particle = de.slikey.effectlib.util.ParticleEffect.CRIT_MAGIC;
        helixEffect.radius = 10;
        helixEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 0.25D, 0.5D));
        helixEffect.infinite();
        helixEffect.start();

        this.loopSecond = this.hub.getServer().getScheduler().runTaskTimer(this.hub, new Runnable()
        {
            int loops = 0;

            @Override
            public void run()
            {
                this.loops++;

                if (this.loops == 600)
                {
                    baseLocation.getWorld().createExplosion(baseLocation.getX(), baseLocation.getY(), baseLocation.getZ(), 10, false, false);
                    meowBossBar.removeAll();

                    atomEffect.cancel();
                    helixEffect.cancel();

                    restore();
                    end();
                    callback();
                }
                else if (this.loops == 1)
                {
                    player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 5.0F, false, false);
                }

                Ocelot ocelot = baseLocation.getWorld().spawn(baseLocation.clone().add(0.5D, 3.0D, 0.5D), Ocelot.class);
                ocelot.setCatType(Ocelot.Type.values()[GadgetManager.RANDOM.nextInt(Ocelot.Type.values().length)]);
                ocelot.setVelocity(new Vector(GadgetManager.RANDOM.nextInt(8) - 4, 5, GadgetManager.RANDOM.nextInt(8) - 4));
                ocelot.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Meow");
                ocelot.setCustomNameVisible(true);

                meowBossBar.setProgress((100.0D - (this.loops * 100 / 600)) / 100);

                if(GadgetManager.RANDOM.nextInt(5) == 3)
                    for (Player player : hub.getServer().getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);

                hub.getServer().getScheduler().runTaskLater(hub, () ->
                {
                    Color a = ColorUtils.getColor(GadgetManager.RANDOM.nextInt(17) + 1);
                    Color b = ColorUtils.getColor(GadgetManager.RANDOM.nextInt(17) + 1);

                    FireworkEffect fw = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(a).withFade(b).build();
                    FireworkUtils.launchfw(hub, ocelot.getLocation(), fw);

                    ocelot.setHealth(0);
                    ocelot.remove();
                }, 20L * 8);
            }
        }, 1L, 1L);

        new BukkitRunnable()
        {
            double t = Math.PI / 4;
            Location loc = baseLocation.getBlock().getLocation().clone().add(0.5D, 3.0D, 0.5D);

            @Override
            public void run()
            {
                this.t = this.t + 0.1D * Math.PI;

                for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 32)
                {
                    double x = this.t * Math.cos(theta);
                    double y = 2 * Math.exp(-0.1 * this.t) * Math.sin(this.t) + 1.5;
                    double z = this.t * Math.sin(theta);
                    this.loc.add(x, y, z);
                    ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, 0, 1, this.loc, 100.0D);
                    this.loc.subtract(x, y, z);

                    theta = theta + Math.PI / 64;

                    x = t * Math.cos(theta);
                    y = 2 * Math.exp(-0.1 * this.t) * Math.sin(t) + 1.5;
                    z = t * Math.sin(theta);

                    this.loc.add(x, y, z);
                    ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(230, 126, 34), loc, 100.0D);
                    this.loc.subtract(x, y, z);
                }

                if (this.t > 30)
                    this.cancel();
            }
        }.runTaskTimerAsynchronously(this.hub, 0L, 1L);
    }

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
        this.loopSecond.cancel();
    }
}
