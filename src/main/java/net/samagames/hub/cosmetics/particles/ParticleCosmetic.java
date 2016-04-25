package net.samagames.hub.cosmetics.particles;

import de.slikey.effectlib.Effect;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.inventory.ItemStack;

class ParticleCosmetic extends AbstractCosmetic
{
    private final Class<? extends Effect> particleEffect;

    ParticleCosmetic(Hub hub, String key, String displayName, ItemStack icon, int stars, CosmeticRarity rarity, CosmeticAccessibility accessibility, String[] description, Class<? extends Effect> particleEffect)
    {
        super(hub, "particle", key, displayName, icon, stars, rarity, accessibility, description);

        this.particleEffect = particleEffect;
    }

    public Class<? extends Effect> getParticleEffect()
    {
        return this.particleEffect;
    }
}
