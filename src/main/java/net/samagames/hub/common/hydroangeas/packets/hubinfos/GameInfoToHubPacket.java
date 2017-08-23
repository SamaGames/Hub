package net.samagames.hub.common.hydroangeas.packets.hubinfos;

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
public class GameInfoToHubPacket extends Packet
{
    private int playerMaxForMap;
    private int playerWaitFor;
    private int totalPlayerOnServers;

    private String templateID;

    public GameInfoToHubPacket() {}

    public int getPlayerMaxForMap()
    {
        return this.playerMaxForMap;
    }

    public void setPlayerMaxForMap(int playerMaxForMap)
    {
        this.playerMaxForMap = playerMaxForMap;
    }

    public int getPlayerWaitFor()
    {
        return this.playerWaitFor;
    }

    public void setPlayerWaitFor(int playerWaitFor)
    {
        this.playerWaitFor = playerWaitFor;
    }

    public int getTotalPlayerOnServers()
    {
        return this.totalPlayerOnServers;
    }

    public void setTotalPlayerOnServers(int totalPlayerOnServers)
    {
        this.totalPlayerOnServers = totalPlayerOnServers;
    }

    public String getTemplateID()
    {
        return this.templateID;
    }
}
