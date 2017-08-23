package net.samagames.hub.games.shops;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.shop.GuiShopCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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
public class ShopCategory extends ShopIcon
{
    protected final AbstractGame game;
    protected final List<ShopIcon> contents;

    public ShopCategory(Hub hub, AbstractGame game, int storageId, int slot) throws Exception
    {
        super(hub, null, storageId, slot, new int[0]);

        this.game = game;
        this.contents = new ArrayList<>();
    }

    public void addContent(ShopIcon icon)
    {
        this.contents.add(icon);
    }

    @Override
    public void execute(Player player, ClickType clickType)
    {
        this.hub.getGuiManager().openGui(player, new GuiShopCategory(this.hub, this.game, this, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player)));
    }

    @Override
    public ItemStack getFormattedIcon(Player player)
    {
        return this.getIcon().clone();
    }

    public ShopIcon getIconByAction(String action)
    {
        for (ShopIcon icon : this.contents)
            if (icon.getStorageId() == Long.parseLong(action))
                return icon;

        return null;
    }

    public List<ShopIcon> getContents()
    {
        return this.contents;
    }
}
