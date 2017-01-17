package net.samagames.hub.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 15/01/2017
 */
class ClothRegistry extends AbstractCosmeticRegistry<ClothCosmetic>
{
    ClothRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register() throws Exception
    {
        ClothCosmetic test = new ClothCosmetic(this.hub, 19, ClothCosmetic.ArmorSlot.LEGGINGS, new ItemStack(Material.DIAMOND_LEGGINGS, 1));

        this.registerElement(test);
    }
}
