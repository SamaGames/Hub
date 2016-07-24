package net.samagames.hub.games.types;

import net.samagames.api.games.GamesNames;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UHCZoneGame extends AbstractGame
{
    public UHCZoneGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "uhczone";
    }

    @Override
    public String getName()
    {
        return "Zone UHC";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.GOLDEN_APPLE, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "La Zone où tous nos jeux Ultra Hard",
                "Core sont réunis !",
                "",
                "\u2B29 UHC",
                "\u2B29 UHCRun",
                "\u2B29 DoubleRunner",
                "\u2B29 UHCRandom",
                "\u2B29 RandomRun"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "IamBlueSlime",
                "Rigner",
                "Thog"
        };
    }

    @Override
    public RulesBook[] getRulesBooks()
    {
        return null;
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 13;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), 28.5D, 105.0D, 35.5D, -90.0F, 0.0F);
    }

    @Override
    public int getOnlinePlayers()
    {
        int players = 0;

        players += this.hub.getGameManager().getGameByIdentifier("uhc").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("uhcrun").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("switchrun").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("doublerunner").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("uhcrandom").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("randomrun").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("ultraflagkeeper").getOnlinePlayers();

        return players;
    }

    @Override
    public boolean isGroup()
    {
        return true;
    }

    @Override
    public boolean isLocked()
    {
        return false;
    }

    @Override
    public boolean isNew()
    {
        return false;
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        List<HubLeaderboard> leaderBoards = new ArrayList<>();

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands1 = new ArrayList<>();
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 36, 102, 44), new Location(this.hub.getWorld(), 37.5, 105, 44.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 36, 102, 43), new Location(this.hub.getWorld(), 37.5, 104, 43.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 36, 102, 45), new Location(this.hub.getWorld(), 37.5, 104, 45.5)));
        leaderBoards.add(new HubLeaderboard(this.hub, GamesNames.UHCRUN, "Meurtres", "kills", new Location(this.hub.getWorld(), 36, 103, 44), leaderBoardStands1));

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands2 = new ArrayList<>();
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 34, 102, 46), new Location(this.hub.getWorld(), 34.5, 105, 47.5)));
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 35, 102, 46), new Location(this.hub.getWorld(), 35.5, 104, 47.5)));
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), 33, 102, 46), new Location(this.hub.getWorld(), 33.5, 104, 47.5)));
        leaderBoards.add(new HubLeaderboard(this.hub, GamesNames.UHCRUN, "Victoires", "wins", new Location(this.hub.getWorld(), 34, 103, 46), leaderBoardStands2));

        return leaderBoards;
    }
}
