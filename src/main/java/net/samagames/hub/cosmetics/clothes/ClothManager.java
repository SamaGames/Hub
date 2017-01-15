package net.samagames.hub.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.entity.Player;

import javax.lang.model.type.NullType;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 15/01/2017
 */
public class ClothManager extends AbstractCosmeticManager<ClothCosmetic>
{
    public ClothManager(Hub hub)
    {
        super(hub, new ClothRegistry(hub));
    }

    @Override
    public void enableCosmetic(Player player, ClothCosmetic cosmetic, NullType useless)
    {

    }

    @Override
    public void disableCosmetic(Player player, boolean logout, NullType useless)
    {

    }

    @Override
    public void update()
    {

    }
}
