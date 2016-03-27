package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;

public class InteractionListener implements IPacketsReceiver
{
    private final Hub hub;

    public InteractionListener(Hub hub)
    {
        this.hub = hub;
    }

    @Override
    public void receive(String channel, String packet)
    {
        String type = packet.split(":")[0];
        String playerName = packet.split(":")[1];

        if(this.hub.getServer().getPlayer(playerName) == null)
            return;

        /**switch(type)
        {
            case "friends":
                Hub.getInstance().getGuiManager().openGui(Bukkit.getPlayer(playerName), new GuiFriends(1));
                break;

            case "party":
                Hub.getInstance().getGuiManager().openGui(Bukkit.getPlayer(playerName), new GuiParty(1));
                break;
        }**/
    }
}
