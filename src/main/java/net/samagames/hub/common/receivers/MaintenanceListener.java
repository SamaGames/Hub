package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.signs.GameSign;

import java.util.List;

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
public class MaintenanceListener implements IPacketsReceiver
{
    private final Hub hub;

    public MaintenanceListener(Hub hub)
    {
        this.hub = hub;
    }

    @Override
    public void receive(String channel, String packet)
    {
        String[] data = packet.split(":");

        String game = data[0];
        String template = data[1];
        boolean flag = Boolean.parseBoolean(data[2]);

        AbstractGame gameObject = this.hub.getGameManager().getGameByIdentifier(game);

        if (gameObject == null)
            return;

        List<GameSign> sign = gameObject.getGameSignsByTemplate(template);

        if (sign == null)
            return;

        sign.forEach(gameSign -> gameSign.setMaintenance(flag));
    }
}
