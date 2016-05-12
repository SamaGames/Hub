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
        this.registerElement(new BalloonCosmetic(this.hub, 100, EntityType.COW, 1));
        this.registerElement(new BalloonCosmetic(this.hub, 101, EntityType.PIG, 1));
        this.registerElement(new BalloonCosmetic(this.hub, 102, EntityType.SHEEP, 1));
        this.registerElement(new BalloonCosmetic(this.hub, 115, EntityType.SHEEP, "jeb_", 1));
        this.registerElement(new BalloonCosmetic(this.hub, 116, EntityType.SHEEP, "Grumm", 1));
        this.registerElement(new BalloonCosmetic(this.hub, 140, EntityType.MUSHROOM_COW, 1));
    }
}
