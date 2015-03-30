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

        this.registerGame("beta_vip", new Game(null, null, Material.DIAMOND, new String[] {}, -1, new Location(this.hub.getHubWorld(), 2003.5, 55, 70.5, -180, 0), true));
        this.registerGame("beta_staff", new Game(null, null, Material.COOKIE, new String[] {}, -1, new Location(this.hub.getHubWorld(), -2016.5, 51, 92.5, -180, 0), true));

        this.registerGame("uppervoid", new Game("uppervoid", "Uppervoid", Material.STICK, new String[] {
                "Affrontez les autres joueurs dans une arrène.",
                "Faites-les tomber dans le vide à l'aide de vos",
                "impitoyables TNT's, mais gare à ne pas tomber à",
                "votre tour !"
        }, 20, this.hub.getPlayerManager().getLobbySpawn(), false));

        this.registerGame("uhc", new Game("uhc", "UHC", Material.GOLDEN_APPLE, new String[] {
                "Créez-vous votre stuff, tuez vos ennemis et",
                "soyez le dernier survivant ! Attention, il n'y",
                "a pas de régénération naturelle des coeurs."
        }, 21, this.hub.getPlayerManager().getLobbySpawn(), false));

        this.registerGame("uhcrun", new Game("uhcrun", "UHCRun", Material.WATCH, new String[] {
                "Vous aimes les UHC mais vous n'avez",
                "pas le temps ? Ce jeu est fait pour vous !"
        }, 23, this.hub.getPlayerManager().getLobbySpawn(), false));

        this.registerGame("quake", new Game("quake", "Quake Reborn", Material.DIAMOND_HOE, new String[] {
                ""
        }, 24, this.hub.getPlayerManager().getLobbySpawn(), true));

        this.registerGame("dimensions", new Game("dimensions", "Dimensions", Material.EYE_OF_ENDER, new String[] {
                "Téléportez-vous d'un monde parallèle",
                "à un autre et récupérez le maximum de coffres.",
                "Ensuite, le combat pourra débuter !"
        }, 31, this.hub.getPlayerManager().getLobbySpawn(), false));

        this.registerGame("coquelicot", new Game("coquelicot", "Coquelicot", Material.SKULL_ITEM, new String[] {
                "Il est prévu qu'une invasion de zombie",
                "survienne, serez-vous le premier à",
                "vous échappez de cet enfer ?"
        }, 13, this.hub.getPlayerManager().getLobbySpawn(), true));
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
