package net.samagames.hub.cosmetics.particles;

import de.slikey.effectlib.effect.EntityEffect;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.inventory.ItemStack;

public class ParticleCosmetic extends AbstractCosmetic
{
    private final Class<? extends EntityEffect> particleEffect;

    public ParticleCosmetic(String databaseName, String displayName, ItemStack icon, String[] description, Class<? extends EntityEffect> particleEffect)
    {
        super("particle." + databaseName, displayName, icon, description);
        this.particleEffect = particleEffect;
    }

    public Class<? extends EntityEffect> getParticleEffect()
    {
        return this.particleEffect;
    }
}
