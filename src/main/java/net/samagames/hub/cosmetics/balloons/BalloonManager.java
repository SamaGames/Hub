package net.samagames.hub.cosmetics.balloons;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.entity.Player;

import javax.lang.model.type.NullType;

/**
 * Created by Rigner for project Hub.
 */
public class BalloonManager extends AbstractCosmeticManager<BalloonCosmetic>
{
    public BalloonManager(Hub hub)
    {
        super(hub, new BalloonRegistry(hub));
    }

    @Override
    public void enableCosmetic(Player player, BalloonCosmetic cosmetic, NullType useless)
    {
        cosmetic.spawn(player);
    }

    @Override
    public void disableCosmetic(Player player, boolean logout, NullType useless)
    {
        BalloonCosmetic balloonCosmetic = this.getEquippedCosmetic(player);
        if (balloonCosmetic != null)
            balloonCosmetic.remove(player);
    }

    @Override
    public void update() { /** Not needed **/ }
}
