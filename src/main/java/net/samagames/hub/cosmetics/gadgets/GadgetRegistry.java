package net.samagames.hub.cosmetics.gadgets;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.cosmetics.gadgets.displayers.ExpressBotaniaDisplayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

class GadgetRegistry extends AbstractCosmeticRegistry<GadgetCosmetic>
{
    GadgetRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register()
    {
        GadgetCosmetic expressBotaniaGadget = new GadgetCosmetic(this.hub, "expressbotania", "Apprenti botaniste", new ItemStack(Material.RED_ROSE, 1), 700, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Les fleurs, ce n'est pas que",
                "pour les filles.",
                "Profitez-en :)"
        }, ExpressBotaniaDisplayer.class, 60);

        this.registerElement(expressBotaniaGadget);
    }
}
