package net.samagames.hub.common.managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
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
public class EventBus implements EntryPoints
{
    private List<AbstractManager> managers;

    public EventBus()
    {
        this.managers = new ArrayList<>();
    }

    public void registerManager(AbstractManager manager)
    {
        this.managers.add(manager);
    }

    @Override
    public void onDisable()
    {
        this.managers.forEach(AbstractManager::onDisable);
    }

    @Override
    public void onLogin(Player player)
    {
        this.managers.forEach(manager -> manager.onLogin(player));
    }

    @Override
    public void onLogout(Player player)
    {
        this.managers.forEach(manager -> manager.onLogout(player));
    }
}
