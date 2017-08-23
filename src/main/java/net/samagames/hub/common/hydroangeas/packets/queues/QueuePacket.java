package net.samagames.hub.common.hydroangeas.packets.queues;

import net.samagames.hub.common.hydroangeas.connection.Packet;

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
public class QueuePacket extends Packet
{
    public enum TypeQueue {NAMEDID, NAMED, RANDOM, FAST}

    private String game;
    private String map;
    private String templateID;
    private TypeQueue typeQueue;

    QueuePacket() {}

    QueuePacket(TypeQueue typeQueue, String game, String map)
    {
        this(typeQueue, game + "_" + map);

        this.game = game;
        this.map = map;
    }

    QueuePacket(TypeQueue typeQueue, String templateID)
    {
        this.typeQueue = typeQueue;
        this.templateID = templateID;
    }

    public TypeQueue getTypeQueue()
    {
        return this.typeQueue;
    }

    public String getTemplateID()
    {
        return this.templateID;
    }

    public String getMap()
    {
        return this.map;
    }

    public String getGame()
    {
        return this.game;
    }
}
