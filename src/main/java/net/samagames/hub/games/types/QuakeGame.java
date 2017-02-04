package net.samagames.hub.games.types;

import net.samagames.api.games.GamesNames;
import net.samagames.api.stats.IPlayerStats;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopDependsItem;
import net.samagames.hub.games.shops.ShopImprovableItem;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuakeGame extends AbstractGame
{
    public QuakeGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "quake";
    }

    @Override
    public String getName()
    {
        return "Quake";
    }

    @Override
    public String getCategory()
    {
        return "Match à mort";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.DIAMOND_HOE, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Même si vous n'êtes pas un fermier,",
                "tirez sur vos adversaires avec",
                "votre houe !"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "Silvanosky"
        };
    }

    @Override
    public String getWebsiteDescriptionURL()
    {
        return null;
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 24;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        try
        {
            ShopCategory shopCategory = new ShopCategory(this.hub, this, 80, 0);
            ShopCategory hoesCategory = new ShopCategory(this.hub, this, 88, 21);

            int[] hoesIds = new int[]{ 82, 83, 84, 85, 86, 87 };

            ShopDependsItem woodenHoe = new ShopDependsItem(this.hub, "Houe", 81, 31, hoesIds, null).defaultItem();
            hoesCategory.addContent(woodenHoe);

            ShopDependsItem stoneHoe = new ShopDependsItem(this.hub, "Houe", hoesIds[0], 20, hoesIds, woodenHoe);
            hoesCategory.addContent(stoneHoe);

            ShopDependsItem ironHoe = new ShopDependsItem(this.hub, "Houe", hoesIds[1], 21, hoesIds, stoneHoe);
            hoesCategory.addContent(ironHoe);

            ShopDependsItem goldHoe = new ShopDependsItem(this.hub, "Houe", hoesIds[2], 22, hoesIds, ironHoe);
            hoesCategory.addContent(goldHoe);

            ShopDependsItem diamondHoe = new ShopDependsItem(this.hub, "Houe", hoesIds[3], 23, hoesIds, goldHoe);
            hoesCategory.addContent(diamondHoe);

            ShopDependsItem diamondHoe2 = new ShopDependsItem(this.hub, "Houe", hoesIds[4], 24, hoesIds, diamondHoe);
            hoesCategory.addContent(diamondHoe2);

            ShopDependsItem blaster = new ShopDependsItem(this.hub, "Houe", hoesIds[5], 13, hoesIds, diamondHoe2);
            hoesCategory.addContent(blaster);

            shopCategory.addContent(hoesCategory);

            ShopImprovableItem shopImprovableItem = new ShopImprovableItem(this.hub, "Grenades", 89, 23, 90);
            for (int i = 91; i <= 95; i++)
                shopImprovableItem.addLevel(i);

            shopCategory.addContent(shopImprovableItem);

            return shopCategory;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), 31D, 102.0D, -31D, -90.0F, 0.0F);
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        List<HubLeaderboard> leaderBoards = new ArrayList<>();

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands1 = new ArrayList<>();
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 37, 102, -24), new Location(this.hub.getWorld(), 38.5, 104, -23.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 36, 102, -24), new Location(this.hub.getWorld(), 36.5, 104, -24.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 37, 102, -22), new Location(this.hub.getWorld(), 38.5, 104, -21.5)));
        leaderBoards.add(new HubLeaderboard(this.hub, GamesNames.QUAKE, "Quake", "Victoires", "wins", new Location(this.hub.getWorld(), 37, 103, -24), leaderBoardStands1));

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands2 = new ArrayList<>();
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 37, 102, -16), new Location(this.hub.getWorld(), 38.5, 104, -15.5)));
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 37, 102, -18), new Location(this.hub.getWorld(), 38.5, 104, -17.5)));
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 36, 102, -16), new Location(this.hub.getWorld(), 36.5, 104, -14.5)));
        leaderBoards.add(new HubLeaderboard(this.hub, GamesNames.QUAKE, "Quake", "Meurtres", "kills", new Location(this.hub.getWorld(), 37, 103, -16), leaderBoardStands2));

        return leaderBoards;
    }

    @Override
    public State getState()
    {
        return State.LOCKED;
    }

    @Override
    public boolean hasResourcesPack()
    {
        return false;
    }

    @Override
    public boolean isPlayerFirstGame(IPlayerStats playerStats)
    {
        return playerStats.getQuakeStatistics().getPlayedGames() == 0;
    }

    @Override
    public boolean isGroup()
    {
        return false;
    }
}
