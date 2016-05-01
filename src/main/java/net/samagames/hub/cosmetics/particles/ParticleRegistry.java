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
        ParticleCosmetic fireParticle = new ParticleCosmetic(this.hub, "fire", "Fulminant",
                new ItemStack(Material.FLINT_AND_STEEL, 1),
                900, CosmeticRarity.EPIC, CosmeticAccessibility.VIPPLUS,
                new String[] {
                    "Vous brûlez d'une rage intense !",
                    "",
                    ChatColor.YELLOW + "Particules de feu"
                }, FireEffect.class);
        fireParticle.permissionNeededToView("particle.fire");

        ParticleCosmetic rainParticle = new ParticleCosmetic(this.hub, "rain", "Pluvieux",
                new ItemStack(Material.SPONGE, 1, (byte)1),
                500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP,
                new String[]{
                    "Un nuage s'abat sur vous !",
                    "",
                    ChatColor.YELLOW + "Particules de pluie"
                }, RainyEffect.class);
        rainParticle.permissionNeededToView("particle.rain");

        ParticleCosmetic snowParticle = new ParticleCosmetic(this.hub, "snow", "Neigeux",
                new ItemStack(Material.SNOW_BLOCK, 1),
                700, CosmeticRarity.RARE, CosmeticAccessibility.VIP,
                new String[] {
                    "Un nuage s'abat sur vous !",
                    "",
                    ChatColor.YELLOW + "Particules de neige"
                }, SnowyEffect.class);
        snowParticle.permissionNeededToView("particle.snow");

        ParticleCosmetic stepParticle = new ParticleCosmetic(this.hub, "step", "Empreintes",
                new ItemStack(Material.STONE, 1),
                500, CosmeticRarity.COMMON, CosmeticAccessibility.VIP,
                new String[] {
                    "Montrez votre supériorité !",
                    "",
                    ChatColor.YELLOW + "Particules d'empreintes"
                }, StepEffect.class);
        stepParticle.permissionNeededToView("particle.step");

        this.registerElement(fireParticle);
        this.registerElement(rainParticle);
        this.registerElement(snowParticle);
        this.registerElement(stepParticle);
    }
}
