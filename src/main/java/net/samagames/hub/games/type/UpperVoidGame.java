package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shop.ShopConfiguration;
import org.bukkit.Material;

public class UpperVoidGame extends AbstractGame
{
    public UpperVoidGame(boolean locked)
    {
        super(
                "uppervoid",
                "UpperVoid",
                Material.STICK,
                new String[] {
                    "Affrontez les autres joueurs dans une arène.",
                    "Faites-les tomber dans le vide à l'aide de vos",
                    "impitoyables TNT's, mais gare à ne pas tomber à",
                    "votre tour !"
                },
                20,
                Hub.getInstance().getPlayerManager().getLobbySpawn(),
                new ShopConfiguration(
                        11
                ),
                locked
        );
    }
}
