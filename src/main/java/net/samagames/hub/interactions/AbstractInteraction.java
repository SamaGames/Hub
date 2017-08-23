package net.samagames.hub.interactions;

import net.samagames.hub.Hub;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

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
public abstract class AbstractInteraction implements Listener
{
    protected final Hub hub;

    public AbstractInteraction(Hub hub)
    {
        this.hub = hub;
    }

    public abstract void onDisable();

    public abstract void play(Player player);
    public abstract void stop(Player player);

    public abstract boolean hasPlayer(Player player);
}
