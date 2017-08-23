package net.samagames.hub.cosmetics.particles.effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

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
public class FireEffect extends Effect
{
    /**
     * ParticleType of spawned particle
     */
    private ParticleEffect particle = ParticleEffect.FLAME;

    public FireEffect(EffectManager effectManager)
    {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 4;
        iterations = 2;
    }

    @Override
    public void onRun()
    {
        Location location = this.getEntity().getLocation().add(0,1.5,0);

        //Reactor Left
        Vector relativePosL = new Vector(0.15, -0.15, -0.1);
        Vector reactorLeft = new Vector(0, -0.5, 0);
        VectorUtils.rotateAroundAxisX(reactorLeft, 35.0 * MathUtils.degreesToRadians);
        VectorUtils.rotateAroundAxisZ(reactorLeft, 15.0 * MathUtils.degreesToRadians);
        VectorUtils.rotateAroundAxisY(reactorLeft, -location.getYaw() * MathUtils.degreesToRadians);


        VectorUtils.rotateAroundAxisY(relativePosL, -location.getYaw() * MathUtils.degreesToRadians);
        particle.display(reactorLeft, 0.3F, location.clone().add(relativePosL), 50);

        //Reactor Right
        Vector relativePosR = new Vector(-0.15, -0.15, -0.1);
        Vector reactorRight = new Vector(0, -0.5, 0);
        VectorUtils.rotateAroundAxisX(reactorRight, 35.0 * MathUtils.degreesToRadians);
        VectorUtils.rotateAroundAxisZ(reactorRight, -15.0 * MathUtils.degreesToRadians);
        VectorUtils.rotateAroundAxisY(reactorRight, -location.getYaw() * MathUtils.degreesToRadians);


        VectorUtils.rotateAroundAxisY(relativePosR, -location.getYaw() * MathUtils.degreesToRadians);
        particle.display(reactorRight, 0.3F, location.clone().add(relativePosR), 50);
    }
}