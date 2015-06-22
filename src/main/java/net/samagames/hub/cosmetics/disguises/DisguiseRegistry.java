package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DisguiseRegistry extends AbstractCosmeticRegistry<DisguiseCosmetic>
{
    @Override
    public void register()
    {
        DisguiseCosmetic creeperDisguise = new DisguiseCosmetic("creeper", "Creeper", new ItemStack(Material.SULPHUR), new String[] {
                "Réveillez la peur ancestrale des joueurs !"
        }, DisguiseType.CREEPER);
        creeperDisguise.permissionNeeded("disguise.creeper");

        DisguiseCosmetic zombieDisguise = new DisguiseCosmetic("zombie", "Zombie", new ItemStack(Material.ROTTEN_FLESH), new String[] {
                "Nourissez vous de chair fraiche !"
        }, DisguiseType.ZOMBIE);
        zombieDisguise.permissionNeeded("disguise.zombie");

        DisguiseCosmetic pigmanDisguise = new DisguiseCosmetic("pigman", "Pigman", new ItemStack(Material.GOLD_NUGGET), new String[] {
                "Cochon ? Zombie ? Impossible de savoir..."
        }, DisguiseType.PIG_ZOMBIE);
        pigmanDisguise.permissionNeeded("disguise.pigman");

        DisguiseCosmetic ironGolemDisguise = new DisguiseCosmetic("golem", "Golem de Fer" , new ItemStack(Material.IRON_BLOCK), new String[] {
                "Montrez à tous votre force et",
                "votre puissance !"
        }, DisguiseType.IRON_GOLEM);
        ironGolemDisguise.buyableWithStars(5000);

        DisguiseCosmetic giantDisguise = new DisguiseCosmetic("giant", "Géant", new ItemStack(Material.SKULL_ITEM, 1, (short) 2), new String[] {
                "Imposez vous par votre hauteur !"
        }, DisguiseType.GIANT);
        giantDisguise.buyableWithStars(8000);

        DisguiseCosmetic villagerDisguise = new DisguiseCosmetic("villager", "Villageois", new ItemStack(Material.EMERALD), new String[] {
                "Arnaquez les joueurs avec vos",
                "échanges hors de prix !"
        }, DisguiseType.VILLAGER);
        villagerDisguise.buyableWithStars(2500);

        DisguiseCosmetic witchDisguise = new DisguiseCosmetic("witch", "Sorcière", new ItemStack(Material.POTION), new String[] {
                "Abracadabra !"
        }, DisguiseType.WITCH);
        witchDisguise.buyableWithStars(1000);

        DisguiseCosmetic caveSpiderDisguise = new DisguiseCosmetic("cavespider", "Araignée bleue", new ItemStack(Material.SPIDER_EYE), new String[] {
                "Terrée dans les mineshafts, vous terrorisez les ",
                "joueurs par la menace de votre morsure..."
        }, DisguiseType.CAVE_SPIDER);
        caveSpiderDisguise.buyableWithStars(350);

        DisguiseCosmetic rabbitDisguise = new DisguiseCosmetic("rabbit", "Lappin", new ItemStack(Material.RABBIT_FOOT), new String[] {
                "Devenez un petit lapin trop mignon !"
        }, DisguiseType.RABBIT);
        rabbitDisguise.buyableWithStars(1500);

        this.registerElement(creeperDisguise);
        this.registerElement(zombieDisguise);
        this.registerElement(pigmanDisguise);
        this.registerElement(ironGolemDisguise);
        this.registerElement(giantDisguise);
        this.registerElement(villagerDisguise);
        this.registerElement(witchDisguise);
        this.registerElement(caveSpiderDisguise);
        this.registerElement(rabbitDisguise);
    }
}
