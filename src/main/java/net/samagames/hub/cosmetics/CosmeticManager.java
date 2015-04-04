package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.particles.ParticleManager;

public class CosmeticManager extends AbstractManager
{
    private ParticleManager particleManager;

    public CosmeticManager(Hub hub)
    {
        super(hub);

        this.particleManager = new ParticleManager(hub);
    }

    public ParticleManager getParticleManager() { return this.particleManager; }

    @Override
    public String getName() { return "CosmeticManager"; }
}
