package net.samagames.hub.games;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.logging.Level;

public class GameManager extends AbstractManager
{
    private final HashMap<String, Game> games;

    public GameManager(Hub hub)
    {
        super(hub);

        this.games = new HashMap<>();

        this.registerGame("beta_vip", new Game("beta_vip", Material.DIAMOND, new String[] {}, -1, new Location(this.hub.getHubWorld(), 2003.5, 55, 70.5, -180, 0)));
        this.registerGame("beta_staff", new Game("beta_staff", Material.COOKIE, new String[] {}, -1, new Location(this.hub.getHubWorld(), -2016.5, 51, 92.5, -180, 0)));

        this.registerGame("uppervoid", new Game("Uppervoid", Material.STICK, new String[] {
                "Faites tomber les autres grâce",
                "à votre impitoyable TNT !"
        }, 20, new Location(this.hub.getHubWorld(), -19, 51, 89)));

        this.registerGame("uhc", new Game("UHC", Material.GOLDEN_APPLE, new String[] {
                "Un stuff à créer et des joueurs à",
                "tuer. Mais attention : aucun retour",
                "de vie !"
        }, 21, new Location(this.hub.getHubWorld(), -19, 51, 89)));

        this.registerGame("dimensions", new Game("Dimensions", Material.EYE_OF_ENDER, new String[] {
                "Deux mondes parallèles, des coffres",
                "et des joueurs à combattre !"
        }, 22, new Location(this.hub.getHubWorld(), -19, 51, 89)));

        this.registerGame("uhcrun", new Game("UHCRun", Material.WATCH, new String[] {
                "Vous aimes les UHC mais vous n'avez",
                "pas le temps ? Ce jeu est fait pour vous !"
        }, 23, new Location(this.hub.getHubWorld(), -19, 51, 89)));

        this.registerGame("quake", new Game("Quake Reborn", Material.DIAMOND_HOE, new String[] {
                "Tirez sur tout ce qui bouge ou",
                "c'est ce qui bouge qui vous tuera !"
        }, 24, new Location(this.hub.getHubWorld(), -19, 51, 89)));

        this.registerGame("coquelicot", new Game("Coquelicot", Material.SKULL_ITEM, new String[] {
                "Il est prévu qu'une invasion de zombie",
                "survienne, serez-vous le premier à",
                "vous échappez de cet enfer ?"
        }, 13, new Location(this.hub.getHubWorld(), -19, 51, 89)));
    }

    public void registerGame(String identifier, Game game)
    {
        if(!this.games.containsKey(identifier))
        {
            this.games.put(identifier, game);
            this.hub.log(this, Level.INFO, "Registered game '" + identifier + "'");
        }
    }

    public Game getGameByIdentifier(String identifier)
    {
        if(this.games.containsKey(identifier))
            return this.games.get(identifier);
        else
            return null;
    }

    public HashMap<String, Game> getGames()
    {
        return this.games;
    }

    @Override
    public String getName() { return "GameManager"; }
}
