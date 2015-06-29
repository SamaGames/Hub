package net.samagames.hub.cosmetics.gadgets;

import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.gadgets.displayers.*;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GadgetRegistry extends AbstractCosmeticRegistry<GadgetCosmetic>
{
    @Override
    public void register()
    {
        GadgetCosmetic discoBombGadget = new GadgetCosmetic("discobomb", "Bombe Disco", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.PURPLE.getData()), new String[] {
                "Exprimez vos talents de danseur sous",
                "les feux de votre Bombe Disco !",
                "",
                ChatColor.RESET + "" + ChatColor.RED + "Ne fonctionne qu'en présence",
                ChatColor.RESET + "" + ChatColor.RED + "de musique !"
        }, DiscoBombDisplayer.class, 30);
        discoBombGadget.buyableWithStars(2500);

        GadgetCosmetic moutMout2000Gadget = new GadgetCosmetic("moutmout2000", "MoutMout 2000", new ItemStack(Material.WOOL, 1), new String[] {
                "Faites attention à lui, il ne",
                "sait absolument pas s'arrêter."
        }, MoutMout2000Displayer.class, 30);
        moutMout2000Gadget.buyableWithStars(2500);

        GadgetCosmetic expressBotaniaGadget = new GadgetCosmetic("expressbotania", "Apprenti botaniste", new ItemStack(Material.RED_ROSE, 1), new String[] {
                "Les fleurs, ce n'est pas",
                "que pour les filles.",
                "Profitez-en :)"
        }, ExpressBotaniaDisplayer.class, 30);
        expressBotaniaGadget.buyableWithStars(2500);

        GadgetCosmetic trampoSlimeGadget = new GadgetCosmetic("tramposlime", "TrampoSlime", new ItemStack(Material.SLIME_BLOCK, 1), new String[] {
                "Qu'est-ce que c'est distrayant",
                "de faire des bonds sur des",
                "restes de slimes compactés."
        }, TrampoSlimeDisplayer.class, 60);
        trampoSlimeGadget.buyableWithStars(2500);

        GadgetCosmetic nukeGadget = new GadgetCosmetic("nuke", "Bombe atomique", new ItemStack(Material.TNT, 1), new String[] {
                ChatColor.MAGIC + "L'avenir du monde" + ChatColor.RESET + ChatColor.GRAY + " meow " + ChatColor.MAGIC + "entre",
                ChatColor.MAGIC + "vos mains !"
        }, NukeDisplayer.class, 60);
        nukeGadget.buyableWithStars(Integer.MAX_VALUE);
        nukeGadget.permissionNeededToView("hub.gadgets.nuke");

        GadgetCosmetic fakeCakeGadget = new GadgetCosmetic("cake", "Gâteau fantôme", new ItemStack(Material.CAKE, 1), new String[] {
                "La légende raconte que ce gâteau",
                "n'apparait que pendant la pleine",
                "lune, personne ne l'a jamais vu..."
        }, CakeDisplayer.class, 30);
        fakeCakeGadget.buyableWithStars(2500);

        this.registerElement(discoBombGadget);
        this.registerElement(moutMout2000Gadget);
        this.registerElement(expressBotaniaGadget);
        this.registerElement(trampoSlimeGadget);
        this.registerElement(nukeGadget);
        this.registerElement(fakeCakeGadget);
    }
}
