package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopDependsItem;
import net.samagames.hub.games.shops.ShopItem;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChunkWarsGame extends AbstractGame
{
    public ChunkWarsGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "chunkwars";
    }

    @Override
    public String getName()
    {
        return "ChunkWars";
    }

    @Override
    public String getCategory()
    {
        return "PvP";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.ENDER_PORTAL_FRAME, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[0];
    }

    @Override
    public String[] getDevelopers()
    {
        return new String[] {
                "IamBlueSlime",
                "LordFinn",
                "6infinity8"
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
        return 22;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        try
        {
            ShopCategory shopCategory = new ShopCategory(this.hub, this, 202, 0);

            // -----------------------------------------

            // ShopCategory kitCategory = new ShopCategory(this.hub, this, 203, 21);

            // -----------------------------------------

            ShopCategory cosmeticCategory = new ShopCategory(this.hub, this, 203, 23);

            // ---------------

            int[] nacelleIds = new int[] { 208, 209, 210, 211, 212, 213, 214, 215, 216 };

            ShopCategory nacelleCosmeticCategory = new ShopCategory(this.hub, this, 204, 19);
            ShopItem littleNacelleCosmetic = new ShopItem(this.hub, "Nacelle", nacelleIds[0], 13, nacelleIds).defaultItem();
            ShopDependsItem mediumNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[1], 20, nacelleIds, littleNacelleCosmetic);
            ShopDependsItem bigNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[2], 21, nacelleIds, mediumNacelleCosmetic);
            ShopDependsItem medievalNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[3], 22, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem terraNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[4], 23, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem nisbhajaNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[5], 24, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem propellerNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[6], 30, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem baroqueNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[7], 31, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem skyNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[8], 32, nacelleIds, bigNacelleCosmetic);

            nacelleCosmeticCategory.addContent(littleNacelleCosmetic);
            nacelleCosmeticCategory.addContent(mediumNacelleCosmetic);
            nacelleCosmeticCategory.addContent(bigNacelleCosmetic);
            nacelleCosmeticCategory.addContent(medievalNacelleCosmetic);
            nacelleCosmeticCategory.addContent(terraNacelleCosmetic);
            nacelleCosmeticCategory.addContent(nisbhajaNacelleCosmetic);
            nacelleCosmeticCategory.addContent(propellerNacelleCosmetic);
            nacelleCosmeticCategory.addContent(baroqueNacelleCosmetic);
            nacelleCosmeticCategory.addContent(skyNacelleCosmetic);

            // ---------------

            ShopCategory colorCosmeticCategory = new ShopCategory(this.hub, this, 205, 21);
            ShopCategory patternCosmeticCategory = new ShopCategory(this.hub, this, 206, 23);
            ShopCategory decorationCosmeticCategory = new ShopCategory(this.hub, this, 207, 25);

            cosmeticCategory.addContent(nacelleCosmeticCategory);
            cosmeticCategory.addContent(colorCosmeticCategory);
            cosmeticCategory.addContent(patternCosmeticCategory);
            cosmeticCategory.addContent(decorationCosmeticCategory);

            // -----------------------------------------

            shopCategory.addContent(cosmeticCategory);


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
        return new Location(this.hub.getWorld(), -53.5D, 113D, 36.5D, 33.5F, 0.0F);
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        return null;
    }

    @Override
    public State getState()
    {
        return State.NEW;
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
