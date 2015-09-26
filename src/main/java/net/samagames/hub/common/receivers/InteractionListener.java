package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.interactions.GuiFriends;
import net.samagames.hub.gui.interactions.GuiParty;
import org.bukkit.Bukkit;

public class InteractionListener implements IPacketsReceiver
{
    @Override
    public void receive(String channel, String packet)
    {
        String type = packet.split(":")[0];
        String playerName = packet.split(":")[1];

        if(Bukkit.getPlayer(playerName) == null)
            return;

        switch(type)
        {
            case "friends":
                Hub.getInstance().getGuiManager().openGui(Bukkit.getPlayer(playerName), new GuiFriends(1));
                break;

            case "party":
                Hub.getInstance().getGuiManager().openGui(Bukkit.getPlayer(playerName), new GuiParty(1));
                break;
        }
    }
}
