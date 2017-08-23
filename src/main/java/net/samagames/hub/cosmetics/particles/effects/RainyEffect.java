package net.samagames.hub.cosmetics.particles.effects;

import java.util.Random;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

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
public class RainyEffect extends Effect
{
    private final Random random = new Random();

    public RainyEffect(EffectManager effectManager)
    {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = -1;
        this.asynchronous = true;
    }

    @Override
    public void onRun()
    {
        // Cloud
        for (int i = 0; i < 20; i++)
            display(ParticleEffect.CLOUD, getEntity().getLocation().add(random.nextDouble() - 0.5, 2.75 + random.nextDouble() / 4, random.nextDouble() - 0.5));

        // Water drops
        if (random.nextBoolean())
            display(ParticleEffect.DRIP_WATER, getEntity().getLocation().add(random.nextDouble() * 0.8 - 0.4, 2.75 + random.nextDouble() / 4, random.nextDouble() * 0.8 - 0.4), 7, 0);
    }

}