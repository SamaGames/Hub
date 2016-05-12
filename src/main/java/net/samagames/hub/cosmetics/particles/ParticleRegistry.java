package net.samagames.hub.cosmetics.particles;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.particles.types.*;

class ParticleRegistry extends AbstractCosmeticRegistry<ParticleCosmetic>
{
    ParticleRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register() throws Exception
    {
        ParticleCosmetic fireParticle = new ParticleCosmetic(this.hub, 35, FireEffect.class);
        ParticleCosmetic rainParticle = new ParticleCosmetic(this.hub, 36, RainyEffect.class);
        ParticleCosmetic snowParticle = new ParticleCosmetic(this.hub, 37, SnowyEffect.class);
        ParticleCosmetic stepParticle = new ParticleCosmetic(this.hub, 38, StepEffect.class);
        ParticleCosmetic loverParticle = new ParticleCosmetic(this.hub, 39, LoverEffect.class);
        ParticleCosmetic nervousParticle = new ParticleCosmetic(this.hub, 40, NervousEffect.class);
        ParticleCosmetic noteParticle = new ParticleCosmetic(this.hub, 41, MusicEffect.class);
        //ParticleCosmetic enchantParticle = new ParticleCosmetic(this.hub, 42, EnchantedEffect.class);

        this.registerElement(fireParticle);
        this.registerElement(rainParticle);
        this.registerElement(snowParticle);
        this.registerElement(stepParticle);
        this.registerElement(loverParticle);
        this.registerElement(nervousParticle);
        this.registerElement(noteParticle);
        //this.registerElement(enchantParticle);
    }
}
