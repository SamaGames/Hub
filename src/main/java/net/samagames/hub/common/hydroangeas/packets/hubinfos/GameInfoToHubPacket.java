package net.samagames.hub.common.hydroangeas.packets.hubinfos;

import net.samagames.hub.common.hydroangeas.connection.Packet;

public class GameInfoToHubPacket extends Packet
{
    private int playerMaxForMap;
    private int playerWaitFor;
    private int totalPlayerOnServers;

    private String templateID;

    public GameInfoToHubPacket() {}

    public GameInfoToHubPacket(String templateID)
    {
        this.templateID = templateID;
    }

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
