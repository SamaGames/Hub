package net.samagames.hub.cosmetics.pets;

import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.pets.types.*;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PetRegistry extends AbstractCosmeticRegistry<PetCosmetic>
{
    @Override
    public void register()
    {
        PetChicken chickenPet = new PetChicken("chicken", "Poulet", new ItemStack(Material.FEATHER), new String[] {
                "Même le héros du temps ne peut",
                "pas vous attraper !"
        });
        chickenPet.buyableWithStars(900);

        PetCow cowPet = new PetCow("cow", "Vache", new ItemStack(Material.LEATHER), new String[] {
                "De la côte de boeuf et du bon lait",
                "du pays alpin !"
        });
        cowPet.buyableWithStars(330);

        PetIronGolem ironGolemPet = new PetIronGolem("irongolem", "Golem de Fer", new ItemStack(Material.IRON_BLOCK), new String[] {
                "Nettoyez la terre des monstres",
                "qui la peuplent !"
        });
        ironGolemPet.buyableWithStars(5000);

        PetMagmaCube magmaCubePet = new PetMagmaCube("magmacube", "Magma Cube", new ItemStack(Material.MAGMA_CREAM), new String[] {
                "Le Magma Cube est au Slime ce que",
                "Wario est à Mario !"
        });
        magmaCubePet.permissionNeeded("pet.magmacube");

        PetPig pigPet = new PetPig("pig", "Cochon", new ItemStack(Material.GRILLED_PORK), new String[] {
                "Chevauchez donc ce gros sanglier !"
        });
        pigPet.buyableWithCoins(50000);

        PetRabbit rabbitPet = new PetRabbit("rabbit", "Lapin", new ItemStack(Material.RABBIT_FOOT), new String[] {
                "Sautez jusqu'aux cieux !",
                "`Boing` `Boing` `Boing`"
        });
        rabbitPet.buyableWithStars(1500);

        PetSheep sheepPet = new PetSheep("sheep", "Mouton", new ItemStack(Material.WOOL), new String[] {
                "Livré avec sa palette de couleurs",
                "personnalisables !"
        });
        sheepPet.buyableWithStars(1000);

        PetSlime slimePet = new PetSlime("slime", "Slime", new ItemStack(Material.SLIME_BALL), new String[] {
                "La monture gluante par excellance !",
                "Approuvé par le comité officiel européen",
                "des Slimes (COES) !"
        });
        slimePet.permissionNeeded("pet.slime");

        PetWolf wolfPet = new PetWolf("wolf", "Loup", new ItemStack(Material.BONE), new String[] {
                "Montrez que vous ne vous laissez",
                "pas domestiquer !"
        });
        wolfPet.buyableWithStars(750);

        PetBlackHorse blackHorsePet = new PetBlackHorse("blackhorse", "Cheval Noir", new ItemStack(Material.INK_SACK, 1, DyeColor.BLACK.getDyeData()), new String[] {
                "Rapide et fier, il vous menera à",
                "l'aventure !"
        });
        blackHorsePet.buyableWithStars(1700);

        PetBrownHorse brownHorsePet = new PetBrownHorse("brownhorse", "Cheval Marron", new ItemStack(Material.INK_SACK, 1, DyeColor.BROWN.getDyeData()), new String[] {
                "Ce fidèle destrier vous guidera",
                "jusqu'à la victoire !"
        });
        brownHorsePet.buyableWithStars(1400);

        PetSkeletonHorse skeletonHorsePet = new PetSkeletonHorse("skeletonhorse", "Cheval Skelette", new ItemStack(Material.SKULL_ITEM), new String[] {
                "Restes de Findus, notre fidèle",
                "destrier... RIP :'("
        });
        brownHorsePet.permissionNeeded("pet.skeletonhorse");

        PetWhiteHorse whiteHorsePet = new PetWhiteHorse("whitehorse", "Cheval Blanc", new ItemStack(Material.INK_SACK, 1, DyeColor.WHITE.getDyeData()), new String[] {
                "Plus blanc que blanc, notre fidèle",
                "destrier tient à sa réputation !"
        });
        brownHorsePet.buyableWithStars(2100);

        PetZombieHorse zombieHorsePet = new PetZombieHorse("zombiehorse", "Cheval Zombie", new ItemStack(Material.SKULL_ITEM, 1, (short) 2), new String[] {
                "Notice d'utilisation :",
                "Ne pas exposer au soleil sous peine",
                "de brulures superficielles !"
        });
        zombieHorsePet.permissionNeeded("pet.zombiehorse");

        this.registerElement(chickenPet);
        this.registerElement(cowPet);
        this.registerElement(ironGolemPet);
        this.registerElement(magmaCubePet);
        this.registerElement(pigPet);
        this.registerElement(rabbitPet);
        this.registerElement(sheepPet);
        this.registerElement(slimePet);
        this.registerElement(wolfPet);
        this.registerElement(brownHorsePet);
        this.registerElement(skeletonHorsePet);
        this.registerElement(whiteHorsePet);
        this.registerElement(zombieHorsePet);
    }
}
