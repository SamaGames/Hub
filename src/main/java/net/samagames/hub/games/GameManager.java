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
    private final HashMap<String, IGame> games;

    public GameManager(Hub hub)
    {
        super(hub);

        this.games = new HashMap<>();

        this.registerGame(new OneWayGame("beta_vip", Material.DIAMOND, new Location(this.hub.getHubWorld(), 2003.5, 55, 70.5, -180, 0)));
        this.registerGame(new OneWayGame("beta_staff", Material.COOKIE, new Location(this.hub.getHubWorld(), -2016.5, 51, 92.5, -180, 0)));

        this.registerGame(new UpperVoidGame(false));
        this.registerGame(new UHCGame(false));
        this.registerGame(new UHCRunGame(false));
        this.registerGame(new QuakeGame(true));
        this.registerGame(new DimensionsGame(false));
        this.registerGame(new CoquelicotGame());
        this.registerGame(new HeroBattleGame(true));
    }

    public void registerGame(IGame game)
    {
        if(!this.games.containsKey(game.getCodeName()))
        {
            this.games.put(game.getCodeName(), game);
            this.hub.log(this, Level.INFO, "Registered game '" + game.getCodeName() + "'");
        }
    }

    public IGame getGameByIdentifier(String identifier)
    {
        if(this.games.containsKey(identifier))
            return this.games.get(identifier);
        else
            return null;
    }

    public HashMap<String, IGame> getGames()
    {
        return this.games;
    }

    @Override
    public String getName() { return "GameManager"; }
}
