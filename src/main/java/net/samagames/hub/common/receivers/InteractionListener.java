package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;

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
        /*String type = packet.split(":")[0];
        String playerName = packet.split(":")[1];

        if (this.hub.getServer().getPlayer(playerName) == null)
            return;

        switch (type)
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
