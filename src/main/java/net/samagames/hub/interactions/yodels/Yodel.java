package net.samagames.hub.interactions.yodels;

import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.hub.utils.ProximityUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Yodel extends AbstractInteraction
{
    private Location boarding;
    private Location start;
    private Location end;
    private Location landing;
    private boolean reverse;

    private double length;

    private final BukkitTask startTask;
    private final ArmorStand startBeacon;
    private final Map<UUID, YodelRunner> runnerList;

    public Yodel(Hub hub, Location boarding, Location start, Location end, Location landing, boolean reverse)
    {
        super(hub);
        this.boarding = boarding.clone();
        this.start    = start.clone();
        this.end      = end.clone();
        this.landing  = landing.clone();
        this.reverse = reverse;

        this.length = start.distanceSquared(end);
        this.runnerList = new HashMap<>();

        this.startBeacon = boarding.getWorld().spawn(boarding, ArmorStand.class);
        this.startBeacon.setVisible(false);
        this.startBeacon.setGravity(false);

        this.hub.getTaskManager().getCirclesTask().addCircleAt(boarding);
        this.startTask = ProximityUtils.onNearbyOf(this.hub, this.startBeacon, 0.5D, 0.5D, 0.5D, Player.class, this::play);
    }

    public Location getBoarding()
    {
        return boarding.clone();
    }

    public Location getStart()
    {
        return start.clone();
    }

    public Location getEnd()
    {
        return end.clone();
    }

    public Location getLanding()
    {
        return landing.clone();
    }

    public Vector getAngleVector()
    {
        return end.toVector().subtract(start.toVector());
    }

    public double getLength()
    {
        return length;
    }

    @Override
    public void play(Player player)
    {
        YodelRunner runner = new YodelRunner(this.hub, this, player, this.reverse);
        this.runnerList.put(player.getUniqueId(), runner);
        runner.start();
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return this.runnerList.containsKey(player.getUniqueId());
    }

    @Override
    public void onDisable()
    {
        this.runnerList.values().forEach(YodelRunner::stop);
        this.startTask.cancel();
        this.startBeacon.remove();
    }

    @Override
    public void stop(Player player)
    {
        YodelRunner runner = this.runnerList.get(player.getUniqueId());

        if (runner != null)
        {
            runner.stop();
            this.runnerList.remove(player.getUniqueId());
        }
    }
}
