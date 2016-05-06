package net.samagames.hub.cosmetics.pets;

import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.tools.MojangShitUtils;
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
        PetCosmetic skeletonHorsePet = new PetCosmetic(this.hub, TODO_SHOP, "Cheval Squelette", MojangShitUtils.getMonsterEgg(EntityType.HORSE), 700, CosmeticRarity.RARE, CosmeticAccessibility.VIPPLUS, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.SKELETON);

        PetCosmetic magmaCubePet = new PetCosmetic(this.hub, TODO_SHOP, "Cube de magma", MojangShitUtils.getMonsterEgg(EntityType.MAGMA_CUBE), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIPPLUS, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.MAGMACUBE, PetData.MEDIUM);

        PetCosmetic slimePet = new PetCosmetic(this.hub, TODO_SHOP, "Slime", MojangShitUtils.getMonsterEgg(EntityType.SLIME), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SLIME, PetData.MEDIUM);

        PetCosmetic zombieHorsePet = new PetCosmetic(this.hub, TODO_SHOP, "Cheval zombie", MojangShitUtils.getMonsterEgg(EntityType.HORSE), 700, CosmeticRarity.RARE, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.ZOMBIE);

        PetCosmetic chickenPet = new PetCosmetic(this.hub, TODO_SHOP, "Poulet", MojangShitUtils.getMonsterEgg(EntityType.CHICKEN), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.CHICKEN);

        PetCosmetic wolfPet = new PetCosmetic(this.hub, TODO_SHOP, "Wolf", MojangShitUtils.getMonsterEgg(EntityType.WOLF), 700, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.WOLF);

        PetCosmetic brownHorsePet = new PetCosmetic(this.hub, TODO_SHOP, "Cheval marron", MojangShitUtils.getMonsterEgg(EntityType.HORSE), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.BROWN);

        PetCosmetic cowPet = new PetCosmetic(this.hub, TODO_SHOP, "Vache", MojangShitUtils.getMonsterEgg(EntityType.COW), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.COW);

        PetCosmetic pigPet = new PetCosmetic(this.hub, TODO_SHOP, "Cochon", MojangShitUtils.getMonsterEgg(EntityType.PIG), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.PIG);

        PetCosmetic whiteHorsePet = new PetCosmetic(this.hub, TODO_SHOP, "Cheval blanc", MojangShitUtils.getMonsterEgg(EntityType.HORSE), 900, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.WHITE);

        PetCosmetic rabbitPet = new PetCosmetic(this.hub, TODO_SHOP, "Lapin", MojangShitUtils.getMonsterEgg(EntityType.RABBIT), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.RABBIT);

        PetCosmetic ironGolemPet = new PetCosmetic(this.hub, TODO_SHOP, "Golem de fer", MojangShitUtils.getMonsterEgg(EntityType.GHAST), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.IRONGOLEM);

        PetCosmetic endermanPet = new PetCosmetic(this.hub, TODO_SHOP, "Enderman", MojangShitUtils.getMonsterEgg(EntityType.ENDERMAN), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.ENDERMAN);

        PetCosmetic whiteSheepPet = new PetCosmetic(this.hub, TODO_SHOP, "Mouton blanc", MojangShitUtils.getMonsterEgg(EntityType.SHEEP), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.WHITE);

        PetCosmetic orangeSheepPet = new PetCosmetic(this.hub, TODO_SHOP, "Mouton orange", MojangShitUtils.getMonsterEgg(EntityType.SHEEP), 700, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.ORANGE);

        PetCosmetic purpleSheepPet = new PetCosmetic(this.hub, TODO_SHOP, "Mouton violet", MojangShitUtils.getMonsterEgg(EntityType.SHEEP), 900, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.PURPLE);

        PetCosmetic pinkSheepPet = new PetCosmetic(this.hub, TODO_SHOP, "Mouton rose", MojangShitUtils.getMonsterEgg(EntityType.SHEEP), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.PINK);

        PetCosmetic redSheepPet = new PetCosmetic(this.hub, TODO_SHOP, "Mouton rouge", MojangShitUtils.getMonsterEgg(EntityType.SHEEP), 900, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.RED);

        PetCosmetic blackSheepPet = new PetCosmetic(this.hub, TODO_SHOP, "Mouton noir", MojangShitUtils.getMonsterEgg(EntityType.SHEEP), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.BLACK);

        PetCosmetic yellowSheepPet = new PetCosmetic(this.hub, TODO_SHOP, "Mouton jaune", MojangShitUtils.getMonsterEgg(EntityType.SHEEP), 700, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.SHEEP, PetData.YELLOW);

        PetCosmetic mushroomCowPet = new PetCosmetic(this.hub, TODO_SHOP, "Vache champignon", MojangShitUtils.getMonsterEgg(EntityType.MUSHROOM_COW), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.MUSHROOMCOW);

        PetCosmetic donkeyPet = new PetCosmetic(this.hub, TODO_SHOP, "Âne", MojangShitUtils.getMonsterEgg(EntityType.HORSE), 500, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, PetType.HORSE, PetData.DONKEY);

        PetCosmetic slavePet = new PetCosmetic(this.hub, TODO_SHOP, "Esclave", MojangShitUtils.getMonsterEgg(EntityType.VILLAGER), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
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
