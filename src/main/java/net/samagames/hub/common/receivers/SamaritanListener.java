package net.samagames.hub.common.receivers;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SamaritanListener implements IPacketsReceiver
{
    private static final String TAG = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Samaritan" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "] ";
    private static final FireworkEffect FIREWORK = FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.RED, Color.ORANGE, Color.YELLOW).withFade(Color.ORANGE).withTrail().withFlicker().build();

    private static final long DAY = 6000L;
    private static final long NIGHT = 18000L;

    private final Hub hub;

    public SamaritanListener(Hub hub)
    {
        this.hub = hub;
    }

    @Override
    public void receive(String channel, String packet)
    {
        String[] splited = packet.split("#####");
        String cheater = splited[0];
        String reason = splited[1];

        this.doStuff(cheater, reason, false);
    }

    public void doStuff(String cheater, String reason, boolean simulation)
    {
        World world = this.hub.getWorld();
        world.setTime(world.getTime() == DAY ? NIGHT : DAY);

        this.hub.getServer().broadcastMessage(TAG + "Le joueur " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + cheater + ChatColor.DARK_PURPLE + ChatColor.BOLD + " a été banni pour la raison " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reason + ChatColor.DARK_PURPLE + ChatColor.BOLD + ".");

        for (Player player : this.hub.getServer().getOnlinePlayers())
        {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2.0F, 0.75F);
            world.strikeLightningEffect(player.getLocation());

            this.hub.getServer().getScheduler().runTask(this.hub, () ->
            {
                FireworkUtils.launchfw(this.hub, player.getLocation(), FIREWORK);
            });
        }

        new BukkitRunnable()
        {
            private int loop;

            @Override
            public void run()
            {
                SamaritanListener.this.hub.getWorld().getEntitiesByClass(ArmorStand.class).stream().filter(entity -> entity.getCustomName() != null && entity.getCustomName().equals("Helicot")).forEach(entity ->
                {
                    Location to = entity.getLocation().clone();
                    to.setYaw(entity.getLocation().getYaw() + 75);

                    entity.teleport(to);
                });

                if (this.loop == 4 * 3)
                    this.cancel();
                else
                    this.loop++;
            }
        }.runTaskTimer(this.hub, 5L, 5L);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () -> world.setTime(world.getTime() == DAY ? NIGHT : DAY), 20L * 3);
    }
}
