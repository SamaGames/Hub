package net.samagames.hub.cosmetics.particles;

import de.slikey.effectlib.Effect;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.inventory.ItemStack;

public class ParticleCosmetic extends AbstractCosmetic
{
    private final Class<? extends Effect> particleEffect;

    public ParticleCosmetic(String key, String displayName, ItemStack icon, String[] description, Class<? extends Effect> particleEffect)
    {
        super("particle", key, displayName, icon, description);
        this.particleEffect = particleEffect;
    }

    public Class<? extends Effect> getParticleEffect()
    {
        return this.particleEffect;
    }
}
