package net.samagames.hub.cosmetics.pets;

import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.pets.types.*;
import net.samagames.tools.GlowEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class PetRegistry extends AbstractCosmeticRegistry<PetCosmetic>
{
    @Override
    public void register()
    {
        PetChicken chickenPet = new PetChicken("chicken", "Poulet", new ItemStack(Material.MONSTER_EGG, 1, EntityType.CHICKEN.getTypeId()), new String[] {
                "Même le héros du temps ne peut",
                "pas vous attraper !"
        });
        chickenPet.buyableWithStars(900);

        PetCow cowPet = new PetCow("cow", "Vache", new ItemStack(Material.MONSTER_EGG, 1, EntityType.COW.getTypeId()), new String[] {
                "De la côte de boeuf et du bon lait",
                "du pays alpin !"
        });
        cowPet.buyableWithStars(330);

        PetIronGolem ironGolemPet = new PetIronGolem("irongolem", "Golem de Fer", new ItemStack(Material.MONSTER_EGG, 1, EntityType.GHAST.getTypeId()), new String[] {
                "Nettoyez la terre des monstres",
                "qui la peuplent !"
        });
        ironGolemPet.buyableWithStars(5000);

        PetMagmaCube magmaCubePet = new PetMagmaCube("magmacube", "Magma Cube", new ItemStack(Material.MONSTER_EGG, 1, EntityType.MAGMA_CUBE.getTypeId()), new String[] {
                "Le Magma Cube est au Slime ce que",
                "Wario est à Mario !"
        });
        magmaCubePet.permissionNeeded("pet.magmacube");

        PetPig pigPet = new PetPig("pig", "Cochon", new ItemStack(Material.MONSTER_EGG, 1, EntityType.PIG.getTypeId()), new String[] {
                "Chevauchez donc ce gros sanglier !"
        });
        pigPet.buyableWithCoins(50000);

        PetRabbit rabbitPet = new PetRabbit("rabbit", "Lapin", new ItemStack(Material.MONSTER_EGG, 1, EntityType.RABBIT.getTypeId()), new String[] {
                "Sautez jusqu'aux cieux !",
                "`Boing` `Boing` `Boing`"
        });
        rabbitPet.buyableWithStars(1500);

        PetSheep sheepPet = new PetSheep("sheep", "Mouton", new ItemStack(Material.MONSTER_EGG, 1, EntityType.SHEEP.getTypeId()), new String[] {
                "Livré avec sa palette de couleurs",
                "personnalisables !"
        });
        sheepPet.buyableWithStars(1000);

        PetSlime slimePet = new PetSlime("slime", "Slime", new ItemStack(Material.MONSTER_EGG, 1, EntityType.SLIME.getTypeId()), new String[] {
                "La monture gluante par excellence !",
                "Approuvé par le comité officiel européen",
                "des Slimes (COES) !"
        });
        slimePet.permissionNeeded("pet.slime");

        PetWolf wolfPet = new PetWolf("wolf", "Loup", new ItemStack(Material.MONSTER_EGG, 1, EntityType.WOLF.getTypeId()), new String[] {
                "Montrez que vous ne vous laissez",
                "pas domestiquer !"
        });
        wolfPet.buyableWithStars(750);

        PetEnderman endermanPet = new PetEnderman("enderman", "Enderman", new ItemStack(Material.MONSTER_EGG, 1, EntityType.ENDERMAN.getTypeId()), new String[] {
                "Il vous fera voyager à la vitesse",
                "de *plop* (Téléportation) !"
        });
        endermanPet.buyableWithStars(1500);

        PetBlackHorse blackHorsePet = new PetBlackHorse("blackhorse", "Cheval Noir", new ItemStack(Material.MONSTER_EGG, 1, EntityType.HORSE.getTypeId()), new String[] {
                "Rapide et fier, il vous mènera à",
                "l'aventure !"
        });
        blackHorsePet.buyableWithStars(1700);

        PetBrownHorse brownHorsePet = new PetBrownHorse("brownhorse", "Cheval Marron", new ItemStack(Material.MONSTER_EGG, 1, EntityType.HORSE.getTypeId()), new String[] {
                "Ce fidèle destrier vous guidera",
                "jusqu'à la victoire !"
        });
        brownHorsePet.buyableWithStars(1400);

        PetSkeletonHorse skeletonHorsePet = new PetSkeletonHorse("skeletonhorse", "Cheval Skelette", GlowEffect.addGlow(new ItemStack(Material.MONSTER_EGG, 1, EntityType.HORSE.getTypeId())), new String[] {
                "Restes de Findus, notre fidèle",
                "destrier... RIP :'("
        });
        brownHorsePet.permissionNeeded("pet.skeletonhorse");

        PetWhiteHorse whiteHorsePet = new PetWhiteHorse("whitehorse", "Cheval Blanc", new ItemStack(Material.MONSTER_EGG, 1, EntityType.HORSE.getTypeId()), new String[] {
                "Plus blanc que blanc, notre fidèle",
                "destrier tient à sa réputation !"
        });
        brownHorsePet.buyableWithStars(2100);

        PetZombieHorse zombieHorsePet = new PetZombieHorse("zombiehorse", "Cheval Zombie", new ItemStack(Material.MONSTER_EGG, 1, EntityType.HORSE.getTypeId()), new String[] {
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
        this.registerElement(endermanPet);
        this.registerElement(brownHorsePet);
        this.registerElement(skeletonHorsePet);
        this.registerElement(whiteHorsePet);
        this.registerElement(zombieHorsePet);
    }
}
