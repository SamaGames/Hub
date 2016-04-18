package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.utils.EggUtils;
import org.bukkit.entity.EntityType;

class DisguiseRegistry extends AbstractCosmeticRegistry<DisguiseCosmetic>
{
    DisguiseRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register()
    {
        DisguiseCosmetic zombieDisguise = new DisguiseCosmetic(this.hub, "zombie", "Zombie", EggUtils.getMonsterEgg(EntityType.ZOMBIE), 600, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.ZOMBIE);

        DisguiseCosmetic giantDisguise = new DisguiseCosmetic(this.hub, "giant", "Géant", EggUtils.getMonsterEgg(EntityType.ZOMBIE), 2000, CosmeticRarity.LEGENDARY, CosmeticAccessibility.VIPPLUS, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.GIANT);

        DisguiseCosmetic zombiePigmanDisguise = new DisguiseCosmetic(this.hub, "pigman", "Zombie cochon", EggUtils.getMonsterEgg(EntityType.PIG_ZOMBIE), 800, CosmeticRarity.RARE, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.PIG_ZOMBIE);

        DisguiseCosmetic creeperDisguise = new DisguiseCosmetic(this.hub, "creeper", "Creeper", EggUtils.getMonsterEgg(EntityType.CREEPER), 600, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.CREEPER);

        DisguiseCosmetic chickenDisguise = new DisguiseCosmetic(this.hub, "chicken", "Poulet", EggUtils.getMonsterEgg(EntityType.CHICKEN), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.CHICKEN);

        DisguiseCosmetic villagerDisguise = new DisguiseCosmetic(this.hub, "villager", "Villageois", EggUtils.getMonsterEgg(EntityType.VILLAGER), 800, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.VILLAGER);

        DisguiseCosmetic pigDisguise = new DisguiseCosmetic(this.hub, "pig", "Cochon", EggUtils.getMonsterEgg(EntityType.PIG), 600, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.PIG);

        DisguiseCosmetic ironGolemDisguise = new DisguiseCosmetic(this.hub, "golem", "Golem de fer", EggUtils.getMonsterEgg(EntityType.GHAST), 2000, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.IRON_GOLEM);

        DisguiseCosmetic rabbitDisguise = new DisguiseCosmetic(this.hub, "rabbit", "Lapin", EggUtils.getMonsterEgg(EntityType.RABBIT), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.RABBIT);

        DisguiseCosmetic witchDisguise = new DisguiseCosmetic(this.hub, "witch", "Sorcière", EggUtils.getMonsterEgg(EntityType.WITCH), 600, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.WITCH);

        DisguiseCosmetic squidDisguise = new DisguiseCosmetic(this.hub, "squid", "Poulpe", EggUtils.getMonsterEgg(EntityType.SQUID), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.SQUID);

        DisguiseCosmetic sheepDisguise = new DisguiseCosmetic(this.hub, "sheep", "Mouton", EggUtils.getMonsterEgg(EntityType.SHEEP), 800, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.SHEEP);

        DisguiseCosmetic caveSpiderDisguise = new DisguiseCosmetic(this.hub, "cave-spider", "Araignée bleue", EggUtils.getMonsterEgg(EntityType.CAVE_SPIDER), 600, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.CAVE_SPIDER);

        DisguiseCosmetic blazeDisguise = new DisguiseCosmetic(this.hub, "blaze", "Blaze", EggUtils.getMonsterEgg(EntityType.BLAZE), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.BLAZE);

        DisguiseCosmetic witherSkeletonDisguise = new DisguiseCosmetic(this.hub, "wither-skeleton", "Wither Squelette", EggUtils.getMonsterEgg(EntityType.ENDERMAN), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.WITHER_SKELETON);

        DisguiseCosmetic batDisguise = new DisguiseCosmetic(this.hub, "bat", "Chauve-souris", EggUtils.getMonsterEgg(EntityType.BAT), 800, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.BAT);

        this.registerElement(zombieDisguise);
        this.registerElement(giantDisguise);
        this.registerElement(zombiePigmanDisguise);
        this.registerElement(creeperDisguise);
        this.registerElement(chickenDisguise);
        this.registerElement(villagerDisguise);
        this.registerElement(pigDisguise);
        this.registerElement(ironGolemDisguise);
        this.registerElement(rabbitDisguise);
        this.registerElement(witchDisguise);
        this.registerElement(squidDisguise);
        this.registerElement(sheepDisguise);
        this.registerElement(caveSpiderDisguise);
        this.registerElement(blazeDisguise);
        this.registerElement(witherSkeletonDisguise);
        this.registerElement(batDisguise);
    }
}
