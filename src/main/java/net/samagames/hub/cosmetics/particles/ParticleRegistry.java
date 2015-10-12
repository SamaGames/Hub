package net.samagames.hub.cosmetics.particles;

import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.particles.types.FireEffect;
import net.samagames.hub.cosmetics.particles.types.RainyEffect;
import net.samagames.hub.cosmetics.particles.types.SnowyEffect;
import net.samagames.hub.cosmetics.particles.types.StepEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ParticleRegistry extends AbstractCosmeticRegistry<ParticleCosmetic>
{
    @Override
    public void register()
    {
        // FIXME: reactivate this
        /*ParticleCosmetic heartParticle = new ParticleCosmetic("heart", "Amoureux <3", new ItemStack(Material.RED_ROSE, 1), new String[] {
                "L'amour est la plus belle chose qui soit <3",
                "",
                ChatColor.YELLOW + "Particules en forme de coeur"
        }, ParticleEffect.HEART);
        heartParticle.buyableWithStars(400);

        ParticleCosmetic musicParticle = new ParticleCosmetic("music", "Musical", new ItemStack(Material.RECORD_12, 1), new String[] {
                "Chantons tous ensemble ♪♫",
                "",
                ChatColor.YELLOW + "Particules en forme de notes de musique"
        }, ParticleEffect.NOTE);
        musicParticle.buyableWithStars(600);

        ParticleCosmetic happyParticle = new ParticleCosmetic("happy", "Joyeux", new ItemStack(Material.EMERALD, 1), new String[] {
                "Il n'y a rien de mieux que la bonne humeur",
                "pour tout réussir ;D",
                "",
                ChatColor.YELLOW + "Particules de villageois heureux"
        }, ParticleEffect.VILLAGER_HAPPY);
        happyParticle.buyableWithStars(900);

        ParticleCosmetic enchantmentParticle = new ParticleCosmetic("enchanted", "Enchanté", new ItemStack(Material.ENCHANTMENT_TABLE, 1), new String[] {
                "Ceux qui possèdent cette humeur",
                "en sont enchantés !",
                "",
                ChatColor.YELLOW + "Particules enchantées"
        }, ParticleEffect.ENCHANTMENT_TABLE);
        enchantmentParticle.buyableWithStars(1200);*/

        ParticleCosmetic fireParticle = new ParticleCosmetic("fire", "Fulminant", new ItemStack(Material.FLINT_AND_STEEL, 1), new String[] {
                "Vous brûlez d'une rage intense !",
                "",
                ChatColor.YELLOW + "Particules de feu"
        }, FireEffect.class);
        fireParticle.permissionNeeded("particle.fire");
        
        ParticleCosmetic rainParticle = new ParticleCosmetic("rain", "Pluvieux", new ItemStack(Material.SPONGE, 1, (byte) 1), new String[] {
            "Un nuage s'abat sur vous !",
            "",
            ChatColor.YELLOW + "Particules de pluie"
        }, RainyEffect.class);
        rainParticle.permissionNeeded("particle.rain");
        
        ParticleCosmetic snowParticle = new ParticleCosmetic("snow", "Neigeux", new ItemStack(Material.SNOW_BLOCK, 1), new String[] {
            "Un nuage s'abat sur vous !",
            "",
            ChatColor.YELLOW + "Particules de neige"
        }, SnowyEffect.class);
        snowParticle.permissionNeeded("particle.snow");
        
        ParticleCosmetic stepParticle = new ParticleCosmetic("step", "Empreintes", new ItemStack(Material.STONE, 1), new String[] {
            "Montrez votre supériorité !",
            "",
            ChatColor.YELLOW + "Particules d'empreintes"
        }, StepEffect.class);
        stepParticle.permissionNeeded("particle.step");

        /*this.registerElement(heartParticle);
        this.registerElement(waterParticle);
        this.registerElement(musicParticle);
        this.registerElement(happyParticle);
        this.registerElement(enchantmentParticle);*/
        this.registerElement(fireParticle);
        this.registerElement(rainParticle);
        this.registerElement(snowParticle);
        this.registerElement(stepParticle);
    }
}
