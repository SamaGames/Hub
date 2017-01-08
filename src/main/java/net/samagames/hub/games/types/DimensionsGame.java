package net.samagames.hub.games.types;

import net.samagames.api.games.GamesNames;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopImprovableItem;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DimensionsGame extends AbstractGame
{
    public DimensionsGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "dimensions";
    }

    @Override
    public String getName()
    {
        return "Dimensions";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.EYE_OF_ENDER, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Téléportez-vous d'un monde parallèle",
                "à un autre et récupérez le maximum de",
                "coffres. Ensuite, le combat pourra",
                "débuter !"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "Silvanosky",
                "zyuiop"
        };
    }

    @Override
    public RulesBook[] getRulesBooks()
    {
        return new RulesBook[]
                {
                        new RulesBook("Dimensions").addOwner("Silvanosky").addOwner("zyuiop")
                                .addPage("Objectifs",
                                        " Explorez la map et\n récupérez votre\n" +
                                        " équipement dans des\n coffres. Après\n" +
                                        " quelques minutes tuez\n vos adversaires\n" +
                                        " et survivez !\n Des bonus sont dans\n" +
                                        " la boutique pour vous\n aider si besoin.")
                                .addPage("Dimensions", " Deux dimensions sont\n" +
                                        " disponibles pour\n jouer, utilisez les\n" +
                                        " à bon escient !\n\n Attention aux dégats\n" +
                                        " que cette 2e\n dimension peut vous\n engendrer ;)")
                };
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 21;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        try
        {
            ShopCategory shopCategory = new ShopCategory(this.hub, this, 142, 0);

            ShopImprovableItem teleport = new ShopImprovableItem(this.hub, "Temps de téléportation", 143, 11, 117);
            for (int i = 118; i <= 121; i++)
                teleport.addLevel(i);

            ShopImprovableItem healAtKill = new ShopImprovableItem(this.hub, "Effet au meurtre", 144, 30, 122);
            for (int i = 123; i <= 126; i++)
                healAtKill.addLevel(i);

            ShopImprovableItem healAtStrike = new ShopImprovableItem(this.hub, "Effet au combat", 145, 32, 127);
            for (int i = 128; i <= 133; i++)
                healAtStrike.addLevel(i);

            ShopImprovableItem strengthAtKill = new ShopImprovableItem(this.hub, "Effet au meurtre", 146, 15, 134);
            for (int i = 135; i <= 139; i++)
                strengthAtKill.addLevel(i);

            shopCategory.addContent(teleport);
            shopCategory.addContent(healAtKill);
            shopCategory.addContent(healAtStrike);
            shopCategory.addContent(strengthAtKill);

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
        return new Location(this.hub.getWorld(), -13D, 87.0D, -51D, 180.0F, 0.0F);
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        List<HubLeaderboard> leaderBoards = new ArrayList<>();

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands1 = new ArrayList<>();
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -26, 86, -45), new Location(this.hub.getWorld(), -25.5, 88, -43.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -24, 86, -45), new Location(this.hub.getWorld(), -23.5, 88, -43.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -26, 86, -46), new Location(this.hub.getWorld(), -26.5, 88, -45.5)));
        leaderBoards.add(new HubLeaderboard(this.hub, GamesNames.DIMENSION, "Dimensions", "Victoires", "wins", new Location(this.hub.getWorld(), -26, 87, -45), leaderBoardStands1));

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands2 = new ArrayList<>();
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -26, 86, -53), new Location(this.hub.getWorld(), -25.5, 88, -53.5)));
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -26, 86, -52), new Location(this.hub.getWorld(), -26.5, 88, -51.5)));
        leaderBoardStands2.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -24, 86, -53), new Location(this.hub.getWorld(), -23.5, 88, -53.5)));
        leaderBoards.add(new HubLeaderboard(this.hub, GamesNames.DIMENSION, "Dimensions", "Meurtres", "kills", new Location(this.hub.getWorld(), -26, 87, -53), leaderBoardStands2));

        return leaderBoards;
    }

    @Override
    public State getState()
    {
        return State.SOON;
    }

    @Override
    public boolean hasResourcesPack()
    {
        return false;
    }

    @Override
    public boolean isGroup()
    {
        return false;
    }
}
