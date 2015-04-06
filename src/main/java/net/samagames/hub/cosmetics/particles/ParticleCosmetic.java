package net.samagames.hub.cosmetics.particles;

import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.tools.ParticleEffect;
import org.bukkit.inventory.ItemStack;

public class ParticleCosmetic extends AbstractCosmetic
{
    private final ParticleEffect particleEffect;

    public ParticleCosmetic(String databaseName, String displayName, ItemStack icon, String[] description, ParticleEffect particleEffect)
    {
        super("particle." + databaseName, displayName, icon, description);
        this.particleEffect = particleEffect;
    }

    public ParticleEffect getParticleEffect()
    {
        return this.particleEffect;
    }
}
