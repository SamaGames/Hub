package net.samagames.hub.cosmetics.balloons;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import org.bukkit.entity.EntityType;

/**
 * Created by Rigner for project Hub.
 */
class BalloonRegistry extends AbstractCosmeticRegistry<BalloonCosmetic>
{
    BalloonRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register() throws Exception
    {
        //this.registerElement(new BalloonCosmetic(this.hub, 100, EntityType.SHULKER, 2));
        this.registerElement(new BalloonCosmetic(this.hub, 101, EntityType.PIG, 2));
        //this.registerElement(new BalloonCosmetic(this.hub, 102, EntityType.SLIME, 2));
    }
}
