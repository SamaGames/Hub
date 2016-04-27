package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArcadeGame extends AbstractGame
{
    public ArcadeGame(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getCodeName()
    {
        return "arcade";
    }

    @Override
    public String getName()
    {
        return "Arcade";
    }

    @Override
    public String getCategory()
    {
        return "Arcade";
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Material.SLIME_BALL, 1);
    }

    @Override
    public String[] getDescription()
    {
        return new String[] {
                "Tout un tas de mini-jeux les plus",
                "improbables les uns que les autres !",
                "",
                "\u2B29 HangoverGames",
                "\u2B29 WitherParty",
                "\u2B29 BurnThatChicken",
                "\u2B29 PacMan",
                "\u2B29 Timberman",
                "\u2B29 Bomberman",
                "\u2B29 FlyRing"
        };
    }

    @Override
    public String[] getDeveloppers()
    {
        return null;
    }

    @Override
    public RulesBook[] getRulesBooks()
    {
        RulesBook[] books = new RulesBook[1];

        books[0] = new RulesBook("PacMan").addOwner("Azuxul")
                .addPage("But du jeu", new String[] {
                        "Vous devez récupérer",
                        "le plus de gommes",
                        "possible avant la",
                        "fin de la partie.",
                        "(Plus de gommes ou",
                        "temps écoulé)"
                }, true)
                .addPage("PvP ?", new String[] {
                        "Vous pouvez taper",
                        "vos adversaires",
                        "pour leur faire",
                        "perdre des gommes !",
                        "Quand un joueur",
                        "meurt, il lâche",
                        "20% de ses gommes."
                }, true)
                .addPage("Bonus", new String[] {
                        "Durant la partie,",
                        "certains bonus",
                        "pourront apparaître !",
                        "Attrapez-les tous et",
                        "découvrez leurs",
                        "effets ;)"
                }, true)
        ;

        return books;
    }

    @Override
    public int getSlotInMainMenu()
    {
        return 22;
    }

    @Override
    public ShopCategory getShopConfiguration()
    {
        return null;
    }

    @Override
    public Location getLobbySpawn()
    {
        return new Location(this.hub.getWorld(), -79.5D, 102.0D, -2.5D, 90.0F, 0.0F);
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
        return true;
    }
}
