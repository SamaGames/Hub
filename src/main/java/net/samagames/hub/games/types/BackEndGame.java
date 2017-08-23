package net.samagames.hub.games.types;

import net.samagames.api.stats.IPlayerStats;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

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
public class BackEndGame extends AbstractGame
{
    private final String codeName;
    private final String publicName;
    private final Location spawn;
    private final String websiteDescriptionURL;
    private final boolean hasResourcesPack;
    private final Function<IPlayerStats, Boolean> isPlayerFirstGameFunction;

    public BackEndGame(Hub hub, String codeName, String publicName, Location spawn, String websiteDescriptionURL, boolean hasResourcesPack, Function<IPlayerStats, Boolean> isPlayerFirstGameFunction)
    {
        super(hub);

        this.codeName = codeName;
        this.publicName = publicName;
        this.spawn = spawn;
        this.websiteDescriptionURL = websiteDescriptionURL;
        this.hasResourcesPack = hasResourcesPack;
        this.isPlayerFirstGameFunction = isPlayerFirstGameFunction;
    }

    public BackEndGame(Hub hub, String codeName, String publicName, Location spawn, String websiteDescriptionURL, boolean hasResourcesPack)
    {
        this(hub, codeName, publicName, spawn, websiteDescriptionURL, hasResourcesPack, null);
    }

    @Override
    public String getCodeName()
    {
        return this.codeName;
    }

    @Override
    public String getName()
    {
        return this.publicName;
    }

    @Override
    public String getCategory()
    {
        return null;
    }

    @Override
    public ItemStack getIcon()
    {
        return null;
    }

    @Override
    public String[] getDescription()
    {
        return null;
    }

    @Override
    public String[] getDevelopers()
    {
        return null;
    }

    @Override
    public String getWebsiteDescriptionURL()
    {
        return this.websiteDescriptionURL;
    }

    @Override
    public int getSlotInMainMenu()
    {
        return -1;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return this.spawn;
    }

    @Override
    public Location getWebsiteDescriptionSkull()
    {
        return null;
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        return null;
    }

    @Override
    public State getState()
    {
        return State.OPENED;
    }

    @Override
    public boolean hasResourcesPack()
    {
        return this.hasResourcesPack;
    }

    @Override
    public boolean isPlayerFirstGame(IPlayerStats playerStats)
    {
        if (this.isPlayerFirstGameFunction == null)
            return false;
        else
            return this.isPlayerFirstGameFunction.apply(playerStats);
    }

    @Override
    public boolean isGroup()
    {
        return false;
    }
}
