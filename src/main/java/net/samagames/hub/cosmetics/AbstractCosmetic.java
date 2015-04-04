package net.samagames.hub.cosmetics;

import net.samagames.api.SamaGamesAPI;
import org.bukkit.entity.Player;

public abstract class AbstractCosmetic
{
    private final String databaseName;

    public AbstractCosmetic(String databaseName)
    {
        this.databaseName = databaseName;
    }

    public abstract void handleBuy(Player player);
    public abstract void handleUse(Player player);

    public void handleClick(Player player)
    {
        if(this.isOwned(player))
            this.handleUse(player);
        else
            this.handleBuy(player);
    }

    public boolean isOwned(Player player)
    {
        return SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).contains("cosmetics." + this.databaseName);
    }
}
