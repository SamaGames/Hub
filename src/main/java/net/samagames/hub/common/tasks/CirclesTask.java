package net.samagames.hub.common.tasks;


import net.samagames.hub.Hub;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
public class CirclesTask extends AbstractTask
{
    private static final double RADIUS = 0.5D;

    private final List<Location> locations;
    private double i;

    CirclesTask(Hub hub)
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
