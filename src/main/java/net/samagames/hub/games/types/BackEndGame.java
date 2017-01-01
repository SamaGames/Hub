package net.samagames.hub.games.types;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.tools.RulesBook;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BackEndGame extends AbstractGame
{
    private final String codeName;
    private final String publicName;
    private final Location spawn;
    private final boolean hasResourcesPack;

    public BackEndGame(Hub hub, String codeName, String publicName, Location spawn, boolean hasResourcesPack)
    {
        super(hub);

        this.codeName = codeName;
        this.publicName = publicName;
        this.spawn = spawn;
        this.hasResourcesPack = hasResourcesPack;
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
    public RulesBook[] getRulesBooks()
    {
        return new RulesBook[]
                {
                        new RulesBook("WitherParty").addOwner("IamBlueSlime")
                                .addPage("Comment jouer ?",
                                        " Ecoutez la mélodie\n jouée par" +
                                        " le wither\n et reproduisez la\n" +
                                        " avec les têtes\n devant vous !"),

                        new RulesBook("AgarMC").addOwner("Rigner").addOwner("6infinity8")
                                .addPage("Comment jouer ?",
                                        " Vous incarnez une\n petite" +
                                                " cellule qui doit\n grandir" +
                                                " avec le temps\n\n Mangez d'autres\n" +
                                                " cellules plus petites\n pour" +
                                                " augmenter votre\n taille !")
                                .addPage("Comment jouer ?",
                                        " Mais attention à vos\n adversaires" +
                                                " qui\n peuvent vous manger\n\n Soyez" +
                                                " intelligents et\n restez gros !", false)
                                .addPage("Objectifs",
                                        " &lMode &6&lFFA &0 :\n" +
                                                " Chacun pour soi,\n devenez le" +
                                                " meilleur\n de tous !")
                                .addPage("Objectifs",
                                        " &lMode &6&lTeams &0 :\n" +
                                                " Gagnez le plus de\n points" +
                                                " pour votre\n équipe (&c&lRouge&0," +
                                                " &2&lVert&0\n ou &1&lBleu&0) !", false)
                                .addPage("Objectifs",
                                        " &lMode &6&lHardcore &0 :\n" +
                                                " Vous voulez un peu\n de challenge ? Alors\n retrouvez" +
                                                " le FFA\n avec le mode\n   &lHardcore&0 !", false),

                        new RulesBook("HangoverGames").addOwner("IamBlueSlime")
                                .addPage("Comment jouer ?",
                                        " Votre objectif est\n d'arriver à une\n" +
                                                " consommation d'alcool\n de 25 g/L de" +
                                                " sang.\n\n Ramassez des\n verres dans les\n" +
                                                " chaudrons, et volez\n ceux de vos\n" +
                                                " adversaires en les\n frappant !\n")
                                .addPage("",
                                "&4&l /!\\ Attention /!\\&0\n\n" +
                                        " Boire est dangereux\n    pour la santé !", false)
                };
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
    public boolean isGroup()
    {
        return false;
    }
}
