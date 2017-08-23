package net.samagames.hub.common.hydroangeas;

import java.util.UUID;

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
public class QPlayer
{
    private UUID uuid;
    private int priority;

    QPlayer(UUID uuid, int priority)
    {
        this.uuid = uuid;
        this.priority = priority;
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    public void setUUID(UUID uuid)
    {
        this.uuid = uuid;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
