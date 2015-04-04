package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.Material;

public class UHCGame extends AbstractGame
{
    public UHCGame(boolean locked)
    {
        super(
                "uhc",
                "UHC",
                Material.GOLDEN_APPLE,
                new String[] {
                        "Créez-vous votre stuff, tuez vos ennemis et",
                        "soyez le dernier survivant ! Attention, il n'y",
                        "a pas de régénération naturelle des coeurs."
                },
                21,
                Hub.getInstance().getPlayerManager().getLobbySpawn(),
                null,
                locked
        );
    }
}
