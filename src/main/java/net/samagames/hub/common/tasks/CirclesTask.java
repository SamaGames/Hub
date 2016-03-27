package net.samagames.hub.common.tasks;


import net.samagames.hub.Hub;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class CirclesTask extends AbstractTask
{
    private static final double RADIUS = 0.5D;

    private final List<Location> locations;
    private double i;

    public CirclesTask(Hub hub)
    {
        super(hub);

        this.locations = new ArrayList<>();
        this.task = this.hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, this, 1L, 1L);
    }

    @Override
    public void run()
    {
        for (Location location : this.locations)
            ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.0F, 1, new Location(location.getWorld(), location.getX() + Math.cos(this.i) * RADIUS, location.getY() + 0.15D, location.getZ() + Math.sin(this.i) * RADIUS), 32.0D);

        this.i += 0.25D;

        if (this.i > Math.PI * 2.0D)
            this.i = 0.0D;
    }

    public void addCircleAt(Location center)
    {
        if (!this.locations.contains(center))
            this.locations.add(center);
    }

    public void removeCircleAt(Location center)
    {
        if (this.locations.contains(center))
            this.locations.remove(center);
    }
}
