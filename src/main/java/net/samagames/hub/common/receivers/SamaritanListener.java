package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class SamaritanListener implements IPacketsReceiver
{
    @Override
    public void receive(String channel, String packet)
    {
        String[] splited = packet.split("#####");
        String player = splited[0];
        String reason = splited[1];

        World world = Hub.getInstance().getHubWorld();

        world.setTime(18000L);
        //world.setTime(6000L);

        for(Player p : Bukkit.getOnlinePlayers())
        {
            world.strikeLightning(p.getLocation());

            Bukkit.getScheduler().runTask(Hub.getInstance(), () ->
                FireworkUtils.launchfw(p.getLocation().clone().add(0.0D, 7.0D, 0.0D), FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).withColor(Color.FUCHSIA).withFade(Color.PURPLE).withFlicker().withTrail().build()));

            p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.2F, 1.2F);
        }

        String samaritanTag = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Samaritan" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "] " + ChatColor.RESET;
        Bukkit.broadcastMessage(samaritanTag + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Le joueur " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + player + ChatColor.DARK_PURPLE + ChatColor.BOLD + " à été banni pour la raison " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + reason);

        //Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () -> world.setTime(6000L), 20L * 3);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () -> world.setTime(18000L), 20L * 3);
    }
}
