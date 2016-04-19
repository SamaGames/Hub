package net.samagames.hub.cosmetics.pets;

import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.utils.EggUtils;
import net.samagames.tools.GlowEffect;
import org.bukkit.entity.EntityType;

class PetRegistry extends AbstractCosmeticRegistry<PetCosmetic>
{
    PetRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register()
    {
        PetCosmetic skeletonHorsePet = new PetCosmetic(this.hub, "skeleton-horse", "Cheval Squelette", EggUtils.getMonsterEgg(EntityType.HORSE), 700, CosmeticRarity.RARE, CosmeticAccessibility.VIPPLUS, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.SKELETON);

        PetCosmetic magmaCubePet = new PetCosmetic(this.hub, "magma-cube", "Cube de magma", EggUtils.getMonsterEgg(EntityType.MAGMA_CUBE), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIPPLUS, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.MAGMACUBE, PetData.MEDIUM);

        PetCosmetic slimePet = new PetCosmetic(this.hub, "slime", "Slime", EggUtils.getMonsterEgg(EntityType.SLIME), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SLIME, PetData.MEDIUM);

        PetCosmetic zombieHorsePet = new PetCosmetic(this.hub, "zombie-horse", "Cheval zombie", EggUtils.getMonsterEgg(EntityType.HORSE), 700, CosmeticRarity.RARE, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.ZOMBIE);

        PetCosmetic chickenPet = new PetCosmetic(this.hub, "chicken", "Poulet", EggUtils.getMonsterEgg(EntityType.CHICKEN), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.CHICKEN);

        PetCosmetic wolfPet = new PetCosmetic(this.hub, "wolf", "Wolf", EggUtils.getMonsterEgg(EntityType.WOLF), 700, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.WOLF);

        PetCosmetic brownHorsePet = new PetCosmetic(this.hub, "brown-horse", "Chaval marron", EggUtils.getMonsterEgg(EntityType.HORSE), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.BROWN);

        PetCosmetic cowPet = new PetCosmetic(this.hub, "cow", "Vache", EggUtils.getMonsterEgg(EntityType.COW), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.COW);

        PetCosmetic pigPet = new PetCosmetic(this.hub, "pig", "Cochon", EggUtils.getMonsterEgg(EntityType.PIG), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.PIG);

        PetCosmetic whiteHorsePet = new PetCosmetic(this.hub, "white-horse", "Cheval blanc", EggUtils.getMonsterEgg(EntityType.HORSE), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.WHITE);

        PetCosmetic rabbitPet = new PetCosmetic(this.hub, "rabbit", "Lapin", EggUtils.getMonsterEgg(EntityType.RABBIT), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.RABBIT);

        PetCosmetic ironGolemPet = new PetCosmetic(this.hub, "golem", "Golem de fer", EggUtils.getMonsterEgg(EntityType.GHAST), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.IRONGOLEM);

        PetCosmetic endermanPet = new PetCosmetic(this.hub, "enderman", "Enderman", EggUtils.getMonsterEgg(EntityType.ENDERMAN), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.ENDERMAN);

        PetCosmetic whiteSheepPet = new PetCosmetic(this.hub, "white-sheep", "Mouton blanc", EggUtils.getMonsterEgg(EntityType.SHEEP), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.WHITE);

        PetCosmetic orangeSheepPet = new PetCosmetic(this.hub, "orange-sheep", "Mouton orange", EggUtils.getMonsterEgg(EntityType.SHEEP), 700, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.ORANGE);

        PetCosmetic purpleSheepPet = new PetCosmetic(this.hub, "purple-sheep", "Mouton violet", EggUtils.getMonsterEgg(EntityType.SHEEP), 900, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.PURPLE);

        PetCosmetic pinkSheepPet = new PetCosmetic(this.hub, "pink-sheep", "Mouton rose", EggUtils.getMonsterEgg(EntityType.SHEEP), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.PINK);

        PetCosmetic redSheepPet = new PetCosmetic(this.hub, "red-sheep", "Mouton rouge", EggUtils.getMonsterEgg(EntityType.SHEEP), 900, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.RED);

        PetCosmetic blackSheepPet = new PetCosmetic(this.hub, "black-sheep", "Mouton noir", EggUtils.getMonsterEgg(EntityType.SHEEP), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.BLACK);

        PetCosmetic yellowSheepPet = new PetCosmetic(this.hub, "yellow-sheep", "Mouton jaune", EggUtils.getMonsterEgg(EntityType.SHEEP), 700, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.YELLOW);

        PetCosmetic mushroomCowPet = new PetCosmetic(this.hub, "mushroom-cow", "Vache champignon", EggUtils.getMonsterEgg(EntityType.MUSHROOM_COW), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.MUSHROOMCOW);

        PetCosmetic donkeyPet = new PetCosmetic(this.hub, "donkey", "Âne", EggUtils.getMonsterEgg(EntityType.HORSE), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.DONKEY);

        PetCosmetic slavePet = new PetCosmetic(this.hub, "slave", "Esclave", EggUtils.getMonsterEgg(EntityType.VILLAGER), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.VILLAGER);

        this.registerElement(skeletonHorsePet);
        this.registerElement(magmaCubePet);
        this.registerElement(slimePet);
        this.registerElement(zombieHorsePet);
        this.registerElement(chickenPet);
        this.registerElement(wolfPet);
        this.registerElement(brownHorsePet);
        this.registerElement(cowPet);
        this.registerElement(pigPet);
        this.registerElement(whiteHorsePet);
        this.registerElement(rabbitPet);
        this.registerElement(ironGolemPet);
        this.registerElement(endermanPet);
        this.registerElement(whiteSheepPet);
        this.registerElement(orangeSheepPet);
        this.registerElement(purpleSheepPet);
        this.registerElement(pinkSheepPet);
        this.registerElement(redSheepPet);
        this.registerElement(blackSheepPet);
        this.registerElement(yellowSheepPet);
        this.registerElement(mushroomCowPet);
        this.registerElement(donkeyPet);
        this.registerElement(slavePet);
    }
}
