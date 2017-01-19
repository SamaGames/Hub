package net.samagames.hub.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 15/01/2017
 */
public class ClothRegistry extends AbstractCosmeticRegistry<ClothCosmetic>
{
    private final Map<Integer, ClothingSet> sets;

    ClothRegistry(Hub hub)
    {
        super(hub);

        this.sets = new HashMap<>();
    }

    @Override
    public void register() throws Exception
    {
        this.registerSet(273, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 274, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 275, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 276, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 277, ClothCosmetic.ArmorSlot.BOOTS)
        });
    }

    private void registerSet(int storageId, ClothCosmetic[] set) throws Exception
    {
        for (ClothCosmetic cosmetic : set)
            this.registerElement(cosmetic);

        this.sets.put(storageId, new ClothingSet(this.hub, storageId, set));
    }

    public ClothingSet getClothingSetByStorageId(int storageId)
    {
        if (this.sets.containsKey(storageId))
            return this.sets.get(storageId);
        else
            return null;
    }

    public Map<Integer, ClothingSet> getClothingSets()
    {
        return this.sets;
    }
}
