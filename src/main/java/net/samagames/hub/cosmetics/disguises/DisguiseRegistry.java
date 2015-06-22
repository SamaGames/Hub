package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DisguiseRegistry extends AbstractCosmeticRegistry<DisguiseCosmetic>
{
    @Override
    public void register()
    {
        DisguiseCosmetic creeperDisguise = new DisguiseCosmetic("Creeper", "creeper", new ItemStack(Material.SULPHUR), new String[] {
                "Réveillez la peur ancestrale des joueurs !"
        }, new MobDisguise(DisguiseType.CREEPER));
        creeperDisguise.permissionNeeded("disguise.creeper");

        DisguiseCosmetic zombieDisguise = new DisguiseCosmetic("Zombie", "zombie", new ItemStack(Material.ROTTEN_FLESH), new String[] {
                "Nourissez vous de chair fraiche !"
        }, new MobDisguise(DisguiseType.ZOMBIE));
        zombieDisguise.permissionNeeded("disguise.zombie");

        DisguiseCosmetic pigmanDisguise = new DisguiseCosmetic("Pigman", "pigman", new ItemStack(Material.GOLD_NUGGET), new String[] {
                "Cochon ? Zombie ? Impossible de savoir..."
        }, new MobDisguise(DisguiseType.PIG_ZOMBIE));
        pigmanDisguise.permissionNeeded("disguise.pigman");

        DisguiseCosmetic ironGolemDisguise = new DisguiseCosmetic("Golem de Fer", "golem" , new ItemStack(Material.IRON_BLOCK), new String[] {
                "Montrez à tous votre force et",
                "votre puissance !"
        }, new MobDisguise(DisguiseType.IRON_GOLEM));
        ironGolemDisguise.buyableWithStars(5000);

        DisguiseCosmetic giantDisguise = new DisguiseCosmetic("Géant", "giant", new ItemStack(Material.SKULL_ITEM, 1, (short) 2), new String[] {
                "Imposez vous par votre hauteur !"
        }, new MobDisguise(DisguiseType.GIANT));
        giantDisguise.buyableWithStars(8000);

        DisguiseCosmetic villagerDisguise = new DisguiseCosmetic("Villageois", "villager", new ItemStack(Material.EMERALD), new String[] {
                "Arnaquez les joueurs avec vos",
                "échanges hors de prix !"
        }, new MobDisguise(DisguiseType.VILLAGER));
        villagerDisguise.buyableWithStars(2500);

        DisguiseCosmetic witchDisguise = new DisguiseCosmetic("Sorcière", "witch", new ItemStack(Material.POTION), new String[] {
                "Abracadabra !"
        }, new MobDisguise(DisguiseType.WITCH));
        witchDisguise.buyableWithStars(1000);

        DisguiseCosmetic caveSpiderDisguise = new DisguiseCosmetic("Araignée bleue", "cavespider", new ItemStack(Material.SPIDER_EYE), new String[] {
                "Terrée dans les mineshafts, vous terrorisez les ",
                "joueurs par la menace de votre morsure..."
        }, new MobDisguise(DisguiseType.CAVE_SPIDER));
        caveSpiderDisguise.buyableWithStars(350);

        DisguiseCosmetic rabbitDisguise = new DisguiseCosmetic("Lapin", "rabbit", new ItemStack(Material.RABBIT_FOOT), new String[] {
                "Devenez un petit lapin trop mignon !"
        }, new MobDisguise(DisguiseType.RABBIT));
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
