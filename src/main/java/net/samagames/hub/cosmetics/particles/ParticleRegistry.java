package net.samagames.hub.cosmetics.particles;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.cosmetics.particles.types.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

class ParticleRegistry extends AbstractCosmeticRegistry<ParticleCosmetic>
{
    ParticleRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register()
    {
        ParticleCosmetic fireParticle = new ParticleCosmetic(this.hub, getTodoShop(), "Fulminant", new ItemStack(Material.FLINT_AND_STEEL, 1), 900, CosmeticRarity.EPIC, CosmeticAccessibility.VIPPLUS, new String[] {
                    "Vous brûlez d'une rage intense !",
                    "",
                    ChatColor.YELLOW + "Particules de feu"
        }, FireEffect.class);

        ParticleCosmetic rainParticle = new ParticleCosmetic(this.hub, getTodoShop(), "Pluvieux", new ItemStack(Material.SPONGE, 1, (byte)1), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[]{
                    "Un nuage s'abat sur vous !",
                    "",
                    ChatColor.YELLOW + "Particules de pluie"
        }, RainyEffect.class);

        ParticleCosmetic snowParticle = new ParticleCosmetic(this.hub, getTodoShop(), "Neigeux", new ItemStack(Material.SNOW_BLOCK, 1), 700, CosmeticRarity.RARE, CosmeticAccessibility.VIP, new String[] {
                    "Un nuage s'abat sur vous !",
                    "",
                    ChatColor.YELLOW + "Particules de neige"
        }, SnowyEffect.class);

        ParticleCosmetic stepParticle = new ParticleCosmetic(this.hub, getTodoShop(), "Empreintes", new ItemStack(Material.STONE, 1), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[] {
                    "Montrez votre supériorité !",
                    "",
                    ChatColor.YELLOW + "Particules d'empreintes"
        }, StepEffect.class);

        ParticleCosmetic loverParticle = new ParticleCosmetic(this.hub, getTodoShop(), "Amoureux", new ItemStack(Material.RED_ROSE), 900, CosmeticRarity.EPIC, CosmeticAccessibility.VIPPLUS, new String[]{
                    "Montrez votre amour !",
                    "",
                    ChatColor.YELLOW + "Particules de coeurs"
        }, LoverEffect.class);

        ParticleCosmetic nervousParticle = new ParticleCosmetic(this.hub, getTodoShop(), "Nerveux", new ItemStack(Material.BLAZE_POWDER), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[]{
                    "Montrez votre frustration !",
                    "",
                    ChatColor.YELLOW + "Particules d'éclairs"
        }, NervousEffect.class);

        ParticleCosmetic noteParticle = new ParticleCosmetic(this.hub, getTodoShop(), "Musical", new ItemStack(Material.JUKEBOX), 500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP, new String[]{
                    "Ecoutez votre musique !",
                    "",
                    ChatColor.YELLOW + "Particules de notes de musique"
        }, MusicEffect.class);

        ParticleCosmetic enchantParticle = new ParticleCosmetic(this.hub, getTodoShop(), "Enchanté", new ItemStack(Material.ENCHANTMENT_TABLE), 700, CosmeticRarity.RARE, CosmeticAccessibility.VIP, new String[]{
                    "Votre culture vous envahit !",
                    "",
                    ChatColor.YELLOW + "Particules de table d'enchantement"
        }, MusicEffect.class);

        this.registerElement(fireParticle);
        this.registerElement(rainParticle);
        this.registerElement(snowParticle);
        this.registerElement(stepParticle);
        this.registerElement(loverParticle);
        this.registerElement(nervousParticle);
        this.registerElement(noteParticle);
        this.registerElement(enchantParticle);
    }
}
