package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.minecraft.server.v1_9_R1.Items;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

class DisguiseRegistry extends AbstractCosmeticRegistry<DisguiseCosmetic>
{
    DisguiseRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register()
    {
        DisguiseCosmetic zombieDisguise = new DisguiseCosmetic(this.hub, "zombie", "Zombie", getMonsterEgg(EntityType.ZOMBIE), 600, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.ZOMBIE);

        DisguiseCosmetic giantDisguise = new DisguiseCosmetic(this.hub, "giant", "Géant", getMonsterEgg(EntityType.ZOMBIE), 2000, CosmeticRarity.LEGENDARY, CosmeticAccessibility.VIPPLUS, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.GIANT);

        DisguiseCosmetic zombiePigmanDisguise = new DisguiseCosmetic(this.hub, "pigman", "Zombie cochon", getMonsterEgg(EntityType.PIG_ZOMBIE), 800, CosmeticRarity.RARE, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.PIG_ZOMBIE);

        DisguiseCosmetic creeperDisguise = new DisguiseCosmetic(this.hub, "creeper", "Creeper", getMonsterEgg(EntityType.CREEPER), 600, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.CREEPER);

        DisguiseCosmetic chickenDisguise = new DisguiseCosmetic(this.hub, "chicken", "Poulet", getMonsterEgg(EntityType.CHICKEN), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.CHICKEN);

        DisguiseCosmetic villagerDisguise = new DisguiseCosmetic(this.hub, "villager", "Villageois", getMonsterEgg(EntityType.VILLAGER), 800, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.VILLAGER);

        DisguiseCosmetic pigDisguise = new DisguiseCosmetic(this.hub, "pig", "Cochon", getMonsterEgg(EntityType.PIG), 600, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.PIG);

        DisguiseCosmetic ironGolemDisguise = new DisguiseCosmetic(this.hub, "golem", "Golem de fer", getMonsterEgg(EntityType.GHAST), 2000, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.IRON_GOLEM);

        DisguiseCosmetic rabbitDisguise = new DisguiseCosmetic(this.hub, "rabbit", "Lapin", getMonsterEgg(EntityType.RABBIT), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.RABBIT);

        DisguiseCosmetic witchDisguise = new DisguiseCosmetic(this.hub, "witch", "Sorcière", getMonsterEgg(EntityType.WITCH), 600, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.WITCH);

        DisguiseCosmetic squidDisguise = new DisguiseCosmetic(this.hub, "squid", "Poulpe", getMonsterEgg(EntityType.SQUID), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.SQUID);

        DisguiseCosmetic sheepDisguise = new DisguiseCosmetic(this.hub, "sheep", "Mouton", getMonsterEgg(EntityType.SHEEP), 800, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.SHEEP);

        DisguiseCosmetic caveSpiderDisguise = new DisguiseCosmetic(this.hub, "cave-spider", "Araignée bleue", getMonsterEgg(EntityType.CAVE_SPIDER), 600, CosmeticRarity.COMMON, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.CAVE_SPIDER);

        DisguiseCosmetic blazeDisguise = new DisguiseCosmetic(this.hub, "blaze", "Blaze", getMonsterEgg(EntityType.BLAZE), 1000, CosmeticRarity.EPIC, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.BLAZE);

        DisguiseCosmetic witherSkeletonDisguise = new DisguiseCosmetic(this.hub, "wither-skeleton", "Wither Squelette", getMonsterEgg(EntityType.ENDERMAN), 1500, CosmeticRarity.LEGENDARY, CosmeticAccessibility.ALL, new String[] {
                "Ceci est une description des plus",
                "magnifiques !"
        }, DisguiseType.WITHER_SKELETON);

        DisguiseCosmetic batDisguise = new DisguiseCosmetic(this.hub, "bat", "Chauve-souris", getMonsterEgg(EntityType.BAT), 800, CosmeticRarity.RARE, CosmeticAccessibility.ALL, new String[] {
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

    private static ItemStack getMonsterEgg(EntityType entityType)
    {
        net.minecraft.server.v1_9_R1.ItemStack egg = new net.minecraft.server.v1_9_R1.ItemStack(Items.SPAWN_EGG, 1);

        NBTTagCompound tag = new NBTTagCompound();

        NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("id", entityType.getName());

        tag.set("EntityTag", entityTag);

        egg.setTag(tag);

        return CraftItemStack.asBukkitCopy(egg);
    }
}
