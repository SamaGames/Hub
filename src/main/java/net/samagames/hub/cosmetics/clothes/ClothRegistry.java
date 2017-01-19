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
        // Cochonou
        this.registerSet(273, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 274, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 275, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 276, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 277, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Vampire
        this.registerSet(278, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 279, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 280, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 281, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 282, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Link
        this.registerSet(283, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 284, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 285, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 286, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 287, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // TNT
        this.registerSet(288, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 289, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 290, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 291, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 292, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Zombie
        this.registerSet(293, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 294, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 295, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 296, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 297, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Cow
        this.registerSet(298, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 299, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 300, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 301, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 302, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Mushroom Cow
        this.registerSet(303, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 304, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 305, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 306, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 307, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Wolf
        this.registerSet(308, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 309, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 310, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 311, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 312, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Sheep
        this.registerSet(313, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 314, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 315, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 316, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 317, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Meow
        this.registerSet(318, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 319, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 320, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 321, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 322, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Carrot
        this.registerSet(323, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 324, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 325, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 326, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 327, ClothCosmetic.ArmorSlot.BOOTS)
        });

        // Chicken
        this.registerSet(328, new ClothCosmetic[] {
                new ClothCosmetic(this.hub, 329, ClothCosmetic.ArmorSlot.HELMET),
                new ClothCosmetic(this.hub, 330, ClothCosmetic.ArmorSlot.CHESTPLATE),
                new ClothCosmetic(this.hub, 331, ClothCosmetic.ArmorSlot.LEGGINGS),
                new ClothCosmetic(this.hub, 332, ClothCosmetic.ArmorSlot.BOOTS)
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
