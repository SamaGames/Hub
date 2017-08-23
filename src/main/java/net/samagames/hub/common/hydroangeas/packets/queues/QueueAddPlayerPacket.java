package net.samagames.hub.common.hydroangeas.packets.queues;

import net.samagames.hub.common.hydroangeas.QPlayer;

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
public class QueueAddPlayerPacket extends QueuePacket
{
    private QPlayer player;

    public QueueAddPlayerPacket() {}

    public QueueAddPlayerPacket(QueuePacket.TypeQueue typeQueue, String game, String map, QPlayer player)
    {
        super(typeQueue, game, map);

        this.player = player;
    }

    public QueueAddPlayerPacket(QueuePacket.TypeQueue typeQueue, String templateID, QPlayer player)
    {
        super(typeQueue, templateID);

        this.player = player;
    }

    public QPlayer getPlayer()
    {
        return this.player;
    }
}
