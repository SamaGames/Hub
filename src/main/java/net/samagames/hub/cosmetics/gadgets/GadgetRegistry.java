package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_9_R1.EntitySheep;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.cosmetics.gadgets.displayers.*;
import net.samagames.tools.MojangShitUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
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

        GadgetCosmetic holyCreeperGadget = new GadgetCosmetic(this.hub, "holycreeper", "Mon ami le Creeper", new ItemStack(Material.SULPHUR, 1), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Mais qu'il est mignon ce Creeper !",
                "Avec un peu de chance vous arriverez à l'apprivoiser...",
                "Enfin, on espère pour vous !"
        }, HolyCreeperDisplayer.class, 40);

        GadgetCosmetic magicCakeGadget = new GadgetCosmetic(this.hub, "cake", "Gâteau fantôme", new ItemStack(Material.CAKE, 1), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "La légende raconte que ce gâteau",
                "n'apparait que pendant la pleine",
                "lune, personne ne l'a jamais vu..."
        }, MagicCakeDisplayer.class, 60);

        GadgetCosmetic moutMout2000Gadget = new GadgetCosmetic(this.hub, "moutmout2000", "MoutMout 2000", new ItemStack(Material.WOOL, 1), 900, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Faites attention à lui, il ne",
                "sait absolument pas s'arrêter."
        }, MoutMout2000Displayer.class, 60);

        GadgetCosmetic trampoSlimeGadget = new GadgetCosmetic(this.hub, "tramposlime", "TrampoSlime", new ItemStack(Material.SLIME_BLOCK, 1), 900, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Qu'est-ce que c'est distrayant",
                "de faire des bonds sur des",
                "restes de slimes compactés."
        }, TrampoSlimeDisplayer.class, 60);

        GadgetCosmetic discoBombGadget = new GadgetCosmetic(this.hub, "discobomb", "Bombe Disco", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.PURPLE.getData()), 700, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Exprimez vos talents de danseur sous",
                "les feux de votre Bombe Disco !"
        }, DiscoBombDisplayer.class, 60);

        GadgetCosmetic enderSwapGadget = new GadgetCosmetic(this.hub, "enderswap", "EnderSwap", new ItemStack(Material.ENDER_PEARL, 1), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[] {
                "Il est impossible de prédire l'effet de",
                "ce gadget ni l'endroit où il vous ammenera.",
                "A vous de prendre le risque !",
                "Inutile donc Indispensable !"
        }, EnderSwapDisplayer.class, 60);

        GadgetCosmetic animalChestGadget = new GadgetCosmetic(this.hub, "secretchest", "Coffre super secret", new ItemStack(Material.CHEST, 1), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIPPLUS, new String[] {
                "Nos chercheurs n'ont jamais réussis à",
                "déterminer le contenu de ce coffre.",
                "Saurez-vous nous aider ?"
        }, AnimalChestDisplayer.class, 60);

        GadgetCosmetic perchedCatGadget = new GadgetCosmetic(this.hub, "catrun", "Chat perché !", MojangShitUtils.getMonsterEgg(EntityType.OCELOT), 1100, CosmeticRarity.LEGENDARY, CosmeticAccessibility.VIPPLUS, new String[] {
                "Défiez en duel un joueur et faites",
                "en sorte d'échapper à son imparable",
                "poing qui vous ferait perdre le duel !"
        }, PerchedCatDisplayer.class, 240);

        GadgetCosmetic nukeGadget = new GadgetCosmetic(this.hub, "nuke", "Bombe atomique", new ItemStack(Material.TNT, 1), 42000, CosmeticRarity.ADMIN, CosmeticAccessibility.ADMIN, new String[] {
                ChatColor.MAGIC + "L'avenir du monde" + ChatColor.RESET + ChatColor.GRAY + " meow " + ChatColor.MAGIC + "entre",
                ChatColor.MAGIC + "vos mains !"
        }, NukeDisplayer.class, 60);

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
