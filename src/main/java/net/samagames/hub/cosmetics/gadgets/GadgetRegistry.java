package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_10_R1.EntitySheep;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.gadgets.displayers.*;

class GadgetRegistry extends AbstractCosmeticRegistry<GadgetCosmetic>
{
    GadgetRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register() throws Exception
    {
        GadgetCosmetic expressBotaniaGadget = new GadgetCosmetic(this.hub, 9, ExpressBotaniaDisplayer.class, 60);
        GadgetCosmetic holyCreeperGadget = new GadgetCosmetic(this.hub, 10, HolyCreeperDisplayer.class, 40);
        GadgetCosmetic magicCakeGadget = new GadgetCosmetic(this.hub, 11, MagicCakeDisplayer.class, 60);
        GadgetCosmetic moutMout2000Gadget = new GadgetCosmetic(this.hub, 12, MoutMout2000Displayer.class, 60);
        GadgetCosmetic trampoSlimeGadget = new GadgetCosmetic(this.hub, 13, TrampoSlimeDisplayer.class, 60);
        GadgetCosmetic discoBombGadget = new GadgetCosmetic(this.hub, 14, DiscoBombDisplayer.class, 60);
        GadgetCosmetic enderSwapGadget = new GadgetCosmetic(this.hub, 15, EnderSwapDisplayer.class, 60);
        GadgetCosmetic animalChestGadget = new GadgetCosmetic(this.hub, 16, AnimalChestDisplayer.class, 60);
        GadgetCosmetic perchedCatGadget = new GadgetCosmetic(this.hub, 17, PerchedCatDisplayer.class, 240);
        GadgetCosmetic nukeGadget = new GadgetCosmetic(this.hub, 18, NukeDisplayer.class, 60);

        this.registerElement(expressBotaniaGadget);
        this.registerElement(holyCreeperGadget);
        this.registerElement(magicCakeGadget);
        this.registerElement(moutMout2000Gadget);
        this.registerElement(trampoSlimeGadget);
        this.registerElement(discoBombGadget);
        this.registerElement(enderSwapGadget);
        this.registerElement(animalChestGadget);
        this.registerElement(perchedCatGadget);
        this.registerElement(nukeGadget);

        this.hub.getEntityManager().registerEntity("MoutMout2000", 91, EntitySheep.class, MoutMout2000Displayer.MoutMout2000Sheep.class);
    }
}
