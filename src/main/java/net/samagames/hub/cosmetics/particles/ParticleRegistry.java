package net.samagames.hub.cosmetics.particles;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.particles.effects.*;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
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
