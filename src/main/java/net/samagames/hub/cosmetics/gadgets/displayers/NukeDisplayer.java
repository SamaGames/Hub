package net.samagames.hub.cosmetics.gadgets.displayers;

import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.*;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.tools.ColorUtils;
import net.samagames.tools.ParticleEffect;
import net.samagames.tools.SimpleBlock;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class NukeDisplayer extends AbstractDisplayer
{
    private static final String TAG = ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD;

    private BukkitTask loopFirst;
    private BukkitTask loopSecond;

    public NukeDisplayer(Hub hub, Player player)
    {
        super(hub, player);

        this.addBlockToUse(this.baseLocation.clone(), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 1.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 2.0D, 0.0D).subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 1.0D, 1.0D), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 2.0D, 1.0D), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 1.0D, 0.0D), new SimpleBlock(Material.BARRIER));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 2.0D, 0.0D), new SimpleBlock(Material.BARRIER));
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

        TornadoEffect tornadoEffect = new TornadoEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        tornadoEffect.setLocation(this.baseLocation.clone().add(0.5D, 1.5D, 0.5D));
        tornadoEffect.showCloud = false;
        tornadoEffect.yOffset = 0.0D;
        tornadoEffect.tornadoHeight = 25.0F;
        tornadoEffect.tornadoParticle = de.slikey.effectlib.util.ParticleEffect.FIREWORKS_SPARK;
        tornadoEffect.asynchronous = true;
        tornadoEffect.run();

        AtomEffect atomEffect = new AtomEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        atomEffect.particleNucleus = de.slikey.effectlib.util.ParticleEffect.FLAME;
        atomEffect.particleOrbital = de.slikey.effectlib.util.ParticleEffect.PORTAL;
        atomEffect.type = EffectType.REPEATING;
        atomEffect.iterations = -1;
        atomEffect.asynchronous = true;
        atomEffect.run();

        restore();
        end();

        /**this.loopSecond = this.hub.getServer().getScheduler().runTaskTimer(this.hub, new Runnable()
        {
            int loops = 0;

            @Override
            public void run()
            {
                this.loops++;

                if (this.loops == 600)
                {
                    baseLocation.getWorld().createExplosion(baseLocation.getX(), baseLocation.getY(), baseLocation.getZ(), 10, false, false);

                    restore();
                    end();
                    callback();
                }
                else if (this.loops == 1)
                {
                    player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 5.0F, false, false);
                }

                Ocelot ocelot = baseLocation.getWorld().spawn(baseLocation.getBlock().getLocation().clone().add(0.5D, 3.0D, 0.5D), Ocelot.class);
                ocelot.setCatType(Ocelot.Type.values()[GadgetManager.RANDOM.nextInt(Ocelot.Type.values().length)]);
                ocelot.setVelocity(new Vector(GadgetManager.RANDOM.nextInt(8) - 4, 5, GadgetManager.RANDOM.nextInt(8) - 4));
                ocelot.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Meow");
                ocelot.setCustomNameVisible(true);

                if(GadgetManager.RANDOM.nextInt(5) == 3)
                    for (Player player : hub.getServer().getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);

                hub.getServer().getScheduler().runTaskLater(hub, () ->
                {
                    Color a = ColorUtils.getColor(GadgetManager.RANDOM.nextInt(17) + 1);
                    Color b = ColorUtils.getColor(GadgetManager.RANDOM.nextInt(17) + 1);

                    FireworkEffect fw = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(a).withFade(b).build();
                    FireworkUtils.launchfw(hub, ocelot.getLocation(), fw);

                    ocelot.setHealth(0);
                    ocelot.remove();
                }, 20L * 5);

                ParticleEffect.FLAME.display(0, 1.5F, 0, 0, 5, baseLocation.getBlock().getLocation().clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D).add(0.5D, 0.0D, 0.5D), 100.0D);
                ParticleEffect.FLAME.display(0, 1.5F, 0, 0, 5, baseLocation.getBlock().getLocation().clone().subtract(2.0D, 0.0D, 2.0D).add(0.5D, 0.0D, 0.5D), 100.0D);
                ParticleEffect.FLAME.display(0, 1.5F, 0, 0, 5, baseLocation.getBlock().getLocation().clone().add(2.0D, 0.0D, 2.0D).add(0.5D, 0.0D, 0.5D), 100.0D);
                ParticleEffect.FLAME.display(0, 1.5F, 0, 0, 5, baseLocation.getBlock().getLocation().clone().add(2.0D, 0.0D, 0.0D).subtract(0.0D, 0.0D, 2.0D).add(0.5D, 0.0D, 0.5D), 100.0D);
            }
        }, 1L, 1L);**/
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
