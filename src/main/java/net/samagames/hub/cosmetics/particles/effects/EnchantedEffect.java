package net.samagames.hub.cosmetics.particles.effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.ParticleEffect;

import java.util.Random;

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
public class EnchantedEffect extends Effect {

    private Random random;

    public EnchantedEffect(EffectManager effectManager)
    {
        super(effectManager);
        this.period = 2;
        this.iterations = 5;
        this.random = new Random();
    }

    @Override
    public void onRun()
    {
        double dx = this.random.nextDouble() % 3D - 1.5D;
        double dy = this.random.nextDouble() % 2D;
        double dz = this.random.nextDouble() % 3D - 1.5D;
        display(ParticleEffect.ENCHANTMENT_TABLE, this.getEntity().getLocation().add(dx, dy, dz));
    }
}
