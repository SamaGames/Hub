package net.samagames.hub.cosmetics.particles.types;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.EntityEffect;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;


/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by Geekpower14 on 16/05/2015.
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public class FireEffect extends EntityEffect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    public FireEffect(EffectManager effectManager, Entity entity) {
        super(effectManager, entity);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 1;
    }

    @Override
    public void onRun() {
        Location location = this.entity.getLocation();
        Vector vector = new Vector(0, -1, 0);
        VectorUtils.rotateAroundAxisX(vector, 45.0* MathUtils.degreesToRadians);
        VectorUtils.rotateAroundAxisY(vector, location.getYaw() * MathUtils.degreesToRadians);

        particle.display(vector, 1F, location, 50);
    }
}
