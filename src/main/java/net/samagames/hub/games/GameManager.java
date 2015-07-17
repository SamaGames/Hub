package net.samagames.hub.games;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.type.*;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.logging.Level;

public class GameManager extends AbstractManager
{
    private final HashMap<String, AbstractGame> games;

    public GameManager(Hub hub)
    {
        super(hub);

        this.games = new HashMap<>();

        this.registerGame(new OneWayGame("beta_vip", Material.DIAMOND, new Location(this.hub.getHubWorld(), 2003.5, 55, 70.5, -180, 0)));
        this.registerGame(new OneWayGame("beta_staff", Material.COOKIE, new Location(this.hub.getHubWorld(), -2016.5, 51, 92.5, -180, 0)));

        this.registerGame(new UppervoidGame());
        this.registerGame(new UHCGame());
        this.registerGame(new UHCRunGame());
        this.registerGame(new QuakeGame());
        this.registerGame(new DimensionsGame());
        this.registerGame(new HeroBattleGame());
        this.registerGame(new ArcadeGame());

        //this.registerGame(new CoquelicotGame());
    }

    public void registerGame(AbstractGame game)
    {
        if(!this.games.containsKey(game.getCodeName()))
        {
            this.games.put(game.getCodeName(), game);
            this.hub.log(this, Level.INFO, "Registered game '" + game.getCodeName() + "'");
        }
    }

    public AbstractGame getGameByIdentifier(String identifier)
    {
        if(this.games.containsKey(identifier.toLowerCase()))
            return this.games.get(identifier.toLowerCase());
        else
            return null;
    }

    public HashMap<String, AbstractGame> getGames()
    {
        return this.games;
    }

    @Override
    public String getName() { return "GameManager"; }
}
