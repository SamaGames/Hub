package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.Material;

public class UHCRunGame extends AbstractGame
{

    public UHCRunGame(boolean locked)
    {
        super(
                "uhcrun",
                "UHCRun",
                Material.WATCH,
                new String[] {
                        "Vous aimes les UHC mais vous n'avez",
                        "pas le temps ? Ce jeu est fait pour vous !"
                },
                23,
                Hub.getInstance().getPlayerManager().getLobbySpawn(),
                null,
                locked
        );
    }
}
