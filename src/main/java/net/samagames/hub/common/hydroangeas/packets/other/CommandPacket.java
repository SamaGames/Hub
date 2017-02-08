package net.samagames.hub.common.hydroangeas.packets.other;

import net.samagames.hub.common.hydroangeas.connection.Packet;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 08/02/2017
 */
public class CommandPacket extends Packet
{
    private String sourceUUID;
    private String action;

    public CommandPacket() {}

    public CommandPacket(String source, String action)
    {
        this.sourceUUID = source;
        this.action = action;
    }

    public String getAction()
    {
        return action;
    }

    public String getSourceUUID()
    {
        return sourceUUID;
    }
}
