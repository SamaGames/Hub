package net.samagames.hub.gui;

import net.samagames.api.gui.AbstractGui;
import net.samagames.api.gui.IGuiManager;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
public class GuiManager extends AbstractManager implements IGuiManager
{
    private final ConcurrentMap<UUID, AbstractGui> playersGui;

    public GuiManager(Hub hub)
    {
        super(hub);

        this.playersGui = new ConcurrentHashMap<>();
    }

    @Override
    public void onDisable()
    {
        this.hub.getServer().getOnlinePlayers().forEach(this::onLogout);
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player)
    {
        if (this.playersGui.containsKey(player.getUniqueId()))
        {
            this.playersGui.get(player.getUniqueId()).onClose(player);
            this.playersGui.remove(player.getUniqueId());
        }
    }

    @Override
    public void openGui(Player player, AbstractGui gui)
    {
        if (this.playersGui.containsKey(player.getUniqueId()))
        {
            player.closeInventory();
            this.playersGui.remove(player.getUniqueId());
        }

        this.playersGui.put(player.getUniqueId(), gui);
        gui.display(player);
    }

    @Override
    public void closeGui(Player player)
    {
        if (this.playersGui.containsKey(player.getUniqueId()))
        {
            player.closeInventory();
            this.playersGui.remove(player.getUniqueId());
        }
    }

    @Override
    public void removeClosedGui(Player player)
    {
        if (this.playersGui.containsKey(player.getUniqueId()))
            this.playersGui.remove(player.getUniqueId());
    }

    @Override
    public AbstractGui getPlayerGui(HumanEntity humanEntity)
    {
        return this.getPlayerGui(humanEntity.getUniqueId());
    }

    @Override
    public AbstractGui getPlayerGui(UUID uuid)
    {
        return this.playersGui.containsKey(uuid) ? this.playersGui.get(uuid) : null;
    }

    @Override
    public ConcurrentHashMap<UUID, AbstractGui> getPlayersGui()
    {
        return (ConcurrentHashMap<UUID, AbstractGui>) this.playersGui;
    }
}
