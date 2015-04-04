package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shop.ShopConfiguration;
import org.bukkit.Material;

public class QuakeGame extends AbstractGame
{
    public QuakeGame(boolean locked)
    {
        super(
                "quake",
                "Quake Reborn",
                Material.DIAMOND_HOE,
                new String[] {
                        ""
                },
                24,
                Hub.getInstance().getPlayerManager().getLobbySpawn(),
                new QuakeGameShopConfiguration(),
                locked
        );
    }

    private static class QuakeGameShopConfiguration extends ShopConfiguration
    {
        public QuakeGameShopConfiguration()
        {
            super(15);
        }
    }
}
