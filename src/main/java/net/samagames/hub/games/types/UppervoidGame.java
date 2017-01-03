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

public class UppervoidGame extends AbstractGame
{
    public UppervoidGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "uppervoid";
    }

    @Override
    public String getName()
    {
        return "Uppervoid";
    }

    @Override
    public String getCategory()
    {
        return "Seul contre tous";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.STICK, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Affrontez les autres joueurs dans",
                "une arène. Faites-les tomber dans le",
                "vide à l'aide de vos impitoyables",
                "TNT's, mais gare à ne pas tomber",
                "à votre tour !"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "IamBlueSlime",
                "Silvanosky"
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
        return 20;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        try
        {
            ShopCategory shopCategory = new ShopCategory(this.hub, this, 96, 11);

            ShopImprovableItem shooters = new ShopImprovableItem(this.hub, 97, 13, 66);
            shooters.addLevel(67);
            shooters.addLevel(68);

            ShopImprovableItem grenades = new ShopImprovableItem(this.hub, 98, 30, 69);
            for (int i = 70; i <= 74; i++)
                grenades.addLevel(i);

            ShopImprovableItem hooks = new ShopImprovableItem(this.hub, 99, 32, 75);
            for (int i = 76; i <= 79; i++)
                hooks.addLevel(i);

            shopCategory.addContent(shooters);
            shopCategory.addContent(grenades);
            shopCategory.addContent(hooks);

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
        return new Location(this.hub.getWorld(), -7D, 83.0D, 67D, 0.0F, 0.0F);
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        List<HubLeaderboard> leaderBoards = new ArrayList<>();

        List<HubLeaderboard.HubLeaderBoardStand> leaderBoardStands1 = new ArrayList<>();
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -17, 82, 70), new Location(this.hub.getWorld(), -16.5, 85, 71.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -15, 82, 70), new Location(this.hub.getWorld(), -14.5, 85, 71.5)));
        leaderBoardStands1.add(new HubLeaderboard.HubLeaderBoardStand(new Location(this.hub.getWorld(), -17, 82, 69), new Location(this.hub.getWorld(), -17.5, 85, 69.5)));
        leaderBoards.add(new HubLeaderboard(this.hub, GamesNames.UPPERVOID, "Uppervoid", "Victoires", "wins", new Location(this.hub.getWorld(), -17, 83, 70), leaderBoardStands1));

        return leaderBoards;
    }

    @Override
    public State getState()
    {
        return State.OPENED;
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
