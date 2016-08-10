package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
                "\u2B29 Bomberman"
        };
    }

    @Override
    public String[] getDevelopers()
    {
        return null;
    }

    @Override
    public RulesBook[] getRulesBooks()
    {
        RulesBook[] books = new RulesBook[2];

        books[0] = new RulesBook("PacMan").addOwner("Azuxul")
                .addPage("But du jeu", new String[] {
                        "Vous devez récupérer",
                        "le plus de gommes",
                        "possible avant la fin",
                        "de la partie. (Plus",
                        "de gommes ou temps",
                        "écoulé)"
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

        books[1] = new RulesBook("BomberMan").addOwner("Azuxul").addContributor("LordFinn")
                .addPage("But du jeu", new String[] {
                        "Vous devez faire",
                        "exploser les autres",
                        "joueurs et d'être le",
                        "dernier à être en",
                        "vie ! La partie se",
                        "termine quand il ne",
                        "reste qu'un seul",
                        "joueur ou quand le",
                        "compte à rebours",
                        "arrive à 0."
                }, true)
                .addPage("Bombes", new String[] {
                        "Vous commencez tous",
                        "avec une seule bombe.",
                        "Mais vous pouvez en",
                        "récupérer via des",
                        "bonus pour l'",
                        "augmenter tout",
                        "comme la force de",
                        "l'explosion. Les",
                        "bombes cassent le",
                        "premier bloc qu'elles",
                        "rencontrent et",
                        "explosent en chaine !"
                }, true)
                .addPage("Bonus", new String[] {
                        "Durant la partie",
                        "certains bonus",
                        "pourront apparaître :",
                        "positifs comme",
                        "négatifs. Il est",
                        "infini jusqu'à que",
                        "vous en preniez un",
                        "autre."
                }, true)
                .addPage("Bonus", new String[] {
                        "Attention néanmoins,",
                        "vous ne pouvez",
                        "récupérer des bonus",
                        "en étant en sneak."
                }, false)
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
    public int getOnlinePlayers()
    {
        int players = 0;

        players += this.hub.getGameManager().getGameByIdentifier("craftmything").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("witherparty").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("hangovergames").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("pacman").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("burnthatchicken").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("timberman").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("bomberman").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("flyring").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("partygames").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("casino").getOnlinePlayers();
        players += this.hub.getGameManager().getGameByIdentifier("samabox").getOnlinePlayers();

        return players;
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards()
    {
        return null;
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
}
