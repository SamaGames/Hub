package net.samagames.hub.common.hydroangeas.packets;

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
public abstract class PacketCallBack<PACKET extends Packet>
{
    private Class<? extends Packet> packet;

    protected PacketCallBack(Class<? extends Packet> packet)
    {
        this.packet = packet;
    }

    public abstract void call(PACKET packet);

    public Class getPacketClass()
    {
        return packet;
    }
}
