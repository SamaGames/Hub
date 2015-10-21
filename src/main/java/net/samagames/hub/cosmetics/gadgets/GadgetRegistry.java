package net.samagames.hub.cosmetics.gadgets;

import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.gadgets.displayers.*;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class GadgetRegistry extends AbstractCosmeticRegistry<GadgetCosmetic>
{
    @Override
    public void register()
    {
        GadgetCosmetic discoBombGadget = new GadgetCosmetic("discobomb", "Bombe Disco", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.PURPLE.getData()), new String[] {
                "Exprimez vos talents de danseur sous",
                "les feux de votre Bombe Disco !"
        }, DiscoBombDisplayer.class, 60);
        discoBombGadget.buyableWithStars(2500);

        GadgetCosmetic moutMout2000Gadget = new GadgetCosmetic("moutmout2000", "MoutMout 2000", new ItemStack(Material.WOOL, 1), new String[] {
                "Faites attention à lui, il ne",
                "sait absolument pas s'arrêter."
        }, MoutMout2000Displayer.class, 60);
        moutMout2000Gadget.buyableWithStars(2500);

        GadgetCosmetic expressBotaniaGadget = new GadgetCosmetic("expressbotania", "Apprenti botaniste", new ItemStack(Material.RED_ROSE, 1), new String[] {
                "Les fleurs, ce n'est pas",
                "que pour les filles.",
                "Profitez-en :)"
        }, ExpressBotaniaDisplayer.class, 60);
        expressBotaniaGadget.buyableWithStars(2500);

        GadgetCosmetic trampoSlimeGadget = new GadgetCosmetic("tramposlime", "TrampoSlime", new ItemStack(Material.SLIME_BLOCK, 1), new String[] {
                "Qu'est-ce que c'est distrayant",
                "de faire des bonds sur des",
                "restes de slimes compactés."
        }, TrampoSlimeDisplayer.class, 60);
        trampoSlimeGadget.buyableWithStars(3500);

        GadgetCosmetic nukeGadget = new GadgetCosmetic("nuke", "Bombe atomique", new ItemStack(Material.TNT, 1), new String[] {
                ChatColor.MAGIC + "L'avenir du monde" + ChatColor.RESET + ChatColor.GRAY + " meow " + ChatColor.MAGIC + "entre",
                ChatColor.MAGIC + "vos mains !"
        }, NukeDisplayer.class, 60);
        nukeGadget.permissionNeeded("hub.gadgets.nuke");

        GadgetCosmetic fakeCakeGadget = new GadgetCosmetic("cake", "Gâteau fantôme", new ItemStack(Material.CAKE, 1), new String[] {
                "La légende raconte que ce gâteau",
                "n'apparait que pendant la pleine",
                "lune, personne ne l'a jamais vu..."
        }, CakeDisplayer.class, 60);
        fakeCakeGadget.buyableWithStars(2500);

        GadgetCosmetic perchedCatGadget = new GadgetCosmetic("catrun", "Chat perché !", new ItemStack(Material.MONSTER_EGG, 1, EntityType.OCELOT.getTypeId()), new String[] {
                "Défiez en duel un joueur et faites",
                "en sorte d'échapper à son imparable",
                "poing qui vous ferait perdre le duel !"
        }, PerchedCatDisplayer.class, 240);
        perchedCatGadget.buyableWithStars(7500);

        GadgetCosmetic enderSwapGadget = new GadgetCosmetic("enderswap", "EnderSwap", new ItemStack(Material.ENDER_PEARL, 1), new String[] {
                "Il est impossible de prédire l'effet de",
                "ce gadget ni l'endroit où il vous ammenera.",
                "A vous de prendre le risque !",
                "Inutile donc Indispensable !"
        }, EnderSwapDisplayer.class, 60);
        trampoSlimeGadget.buyableWithStars(2000);

        GadgetCosmetic stargateGadget = new GadgetCosmetic("stargate-staff", "Stargate", new ItemStack(Material.ENDER_PORTAL_FRAME, 1), new String[] {
                "Ce gadget créera une faille spacio-temporelle",
                "qui vous téléportera dans un monde parallèle",
                "Minecraftien doté de joueurs identiques au monde",
                "où vous êtes actuellement. Fiction ou Science ?",
                "",
                ChatColor.RED + "Attention: La faille spacio-temporelle",
                ChatColor.RED + "aspire tous les joueurs autour de celle-ci !"
        }, StargateDisplayer.class, 120);
        stargateGadget.permissionNeeded("staff.member");

        GadgetCosmetic animalChestGadget = new GadgetCosmetic("secretchest", "Coffre super secret", new ItemStack(Material.CHEST, 1), new String[] {
        		"Nos chercheurs n'ont jamais réussis à déterminer",
        		"le contenu de ce coffre. Saurez-vous nous aider ?"
        }, AnimalChestDisplayer.class, 60);
        animalChestGadget.buyableWithStars(2500);

        GadgetCosmetic holyCreeper = new GadgetCosmetic("holycreeper", "Mon ami le Creeper", new ItemStack(Material.SULPHUR, 1), new String[] {
                "Mais qu'il est mignon ce Creeper !",
                "Avec un peu de chance vous arriverez à l'apprivoiser...",
                "Enfin, on espère pour vous !"
        }, HolyCreeperDisplayer.class, 40);
        holyCreeper.buyableWithStars(1500);
        
        this.registerElement(discoBombGadget);
        this.registerElement(moutMout2000Gadget);
        this.registerElement(expressBotaniaGadget);
        this.registerElement(trampoSlimeGadget);
        this.registerElement(nukeGadget);
        this.registerElement(fakeCakeGadget);
        this.registerElement(perchedCatGadget);
        this.registerElement(enderSwapGadget);
        this.registerElement(stargateGadget);
        this.registerElement(animalChestGadget);
        this.registerElement(holyCreeper);
    }
}
