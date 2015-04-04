package net.samagames.hub.games.type;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shop.ShopConfiguration;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HeroBattleGame extends AbstractGame
{
    public HeroBattleGame(boolean locked)
    {
        super(
                "herobattle",
                "HeroBattle",
                new ItemStack(Material.INK_SACK, 1, (short) DyeColor.RED.ordinal()),
                new String[] {
                        ""
                },
                31,
                Hub.getInstance().getPlayerManager().getLobbySpawn(),
                new ShopConfiguration(
                        14
                ),
                locked
        );
    }
}
