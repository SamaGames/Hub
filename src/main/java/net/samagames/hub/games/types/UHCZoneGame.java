package net.samagames.hub.games.types;

import net.samagames.api.games.GamesNames;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.leaderboards.RotatingLeaderboard;
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
                "\u2B29 RandomRun",
                "\u2B29 Run4Flag"
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
        return new RulesBook[]
                {
                        new RulesBook("UHCRun")
                        .addPage("UHCRun", " L'UHCRun est un mini\n jeu ayant pour même\n but que l'UHC normal\n mais qui n'excède pas\n les 30 minutes de jeu.\n" +
                                "\n Pour cela, il existe 2\n phases principales, la\n préparation (20min)\n et le meetup (10min).")
                        .addPage("Préparation", " 20 minutes vous sont\n dédiées afin de vous\n créer un bon\n équipement.\n" +
                                "\n Plusieurs avantages\n apparaissent pour\n vous faire gagner\n du temps, comme\n pouvoir casser un\n")
                        .addPage("Préparation", " arbre en un coup de\n main ou ne plus\n avoir à se servir\n des fours.", false)
                        .addPage("Meetup", " A la fin de la phase\n précédente, vous\n êtes téléportés à la\n surface et avez\n 10 minutes pour\n éliminer tout vos\n opposants.\n" +
                                "\n La grandeur de la\n carte ne cesse de\n réduire durant\n cette période.", false)
                        .addOwner("IamBlueSlime")
                };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 23;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), -58.5D, 108.0D, -3.5D, 90.0F, 0.0F);
    }

    @Override
    public int getOnlinePlayers()
    {
        int players = 0;

        players += this.hub.getGameManager().getGameByIdentifier("uhc").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("uhcrun").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("doublerunner").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("uhcrandom").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("randomrun").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("ultraflagkeeper").getOnlinePlayers();

        return players;
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        List<HubLeaderboard> leaderBoards = new ArrayList<>();

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands1 = new ArrayList<>();
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -78, 112, 0), new Location(this.hub.getWorld(), -77.5, 114, 1.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -76, 111, -1), new Location(this.hub.getWorld(), -75.5, 113, 0.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -80, 111, 0), new Location(this.hub.getWorld(), -79.5, 113, 1.5)));

        List<RotatingLeaderboard.RotatingLeaderboardFrame> frames1 = new ArrayList<>();
        frames1.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.UHCRUN, "UHCRun", "Meurtres", "kills"));
        frames1.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.DOUBLERUNNER, "DoubleRunner", "Meurtres", "kills"));
        //frames1.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.UHCORIGINAL, "UHC", "Meurtres", "kills"));
        frames1.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.RANDOMRUN, "RandomRun", "Meurtres", "kills"));
        frames1.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.UHCRANDOM, "UHCRandom", "Meurtres", "kills"));
        frames1.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.ULTRAFLAGKEEPER, "Run4Flag", "Meurtres", "kills"));

        leaderBoards.add(new RotatingLeaderboard(this.hub, new Location(this.hub.getWorld(), -78, 111, 0), leaderBoardStands1, frames1));

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands2 = new ArrayList<>();
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -84, 112, -1), new Location(this.hub.getWorld(), -83.5, 114, 0.5)));
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -82, 111, 0), new Location(this.hub.getWorld(), -81.5, 113, 1.5)));
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -86, 111, -2), new Location(this.hub.getWorld(), -85.5, 113, -0.5)));

        List<RotatingLeaderboard.RotatingLeaderboardFrame> frames2 = new ArrayList<>();
        frames2.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.UHCRUN, "UHCRun", "Victoires", "wins"));
        frames2.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.DOUBLERUNNER, "DoubleRunner", "Victoires", "wins"));
        //frames2.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.UHCORIGINAL, "UHC", "Victoires", "wins"));
        frames2.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.RANDOMRUN, "RandomRun", "Victoires", "wins"));
        frames2.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.UHCRANDOM, "UHCRandom", "Victoires", "wins"));
        frames2.add(new RotatingLeaderboard.RotatingLeaderboardFrame(GamesNames.ULTRAFLAGKEEPER, "Run4Flag", "Victoires", "wins"));

        leaderBoards.add(new RotatingLeaderboard(this.hub, new Location(this.hub.getWorld(), -84, 111, -1), leaderBoardStands2, frames2));

        return leaderBoards;
    }

    @Override
    public State getState()
    {
        return State.POPULAR;
    }

    @Override
    public boolean hasResourcesPack()
    {
        return false;
    }

    @Override
    public boolean isGroup()
    {
        return true;
    }
}
