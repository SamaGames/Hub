package net.samagames.hub.interactions.bumper;

import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.hub.utils.ProximityUtils;
import net.samagames.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by Rigner for project Hub.
 */
public class Bumper extends AbstractInteraction implements Listener
{
    private final Location bumperLocation;
    private final BukkitTask startTask;
    private final ArmorStand startBeacon;
    private final Map<UUID, BukkitTask> flyTasks;
    private final List<UUID> flyingPlayers;

    Bumper(Hub hub, Location location)
    {
        super(hub);

        this.bumperLocation = location;
        this.flyTasks = new HashMap<>();
        this.flyingPlayers = new ArrayList<>();

        this.startBeacon = location.getWorld().spawn(this.bumperLocation.clone().add(new Vector(
                Math.cos(this.bumperLocation.getYaw() * Math.PI * 2D / 180D),
                Math.cos(this.bumperLocation.getPitch() * Math.PI * 2D / 180D),
                Math.sin(this.bumperLocation.getYaw() * Math.PI * 2D / 180D)
        )), ArmorStand.class);
        this.startBeacon.setVisible(false);
        this.startBeacon.setGravity(false);

        this.startTask = ProximityUtils.onNearbyOf(hub, this.startBeacon, 1D, 1D, 1D, Player.class, this::play);
    }

    @Override
    public void play(Player player)
    {
        if (this.flyingPlayers.contains(player.getUniqueId()))
            return ;
        this.flyingPlayers.add(player.getUniqueId());
        player.setVelocity(this.bumperLocation.getDirection().multiply(15D));
        this.flyTasks.put(player.getUniqueId(), this.hub.getServer().getScheduler().runTaskLater(this.hub, () -> {
            player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            ((CraftPlayer)player).getHandle().setFlag(7, true);
            Titles.sendTitle(player, 10, 40, 10, "", ChatColor.GOLD + "Bon vol !");
            this.stop(player);
        }, 40L));
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return (flyingPlayers.contains(player.getUniqueId()));
    }

    @Override
    public void onDisable()
    {
        this.startBeacon.remove();
        this.startTask.cancel();

        this.flyTasks.forEach(((uuid, bukkitTask) -> bukkitTask.cancel()));
    }

    @Override
    public void stop(Player player)
    {
        this.flyingPlayers.remove(player.getUniqueId());
        this.flyTasks.remove(player.getUniqueId());
    }
}
