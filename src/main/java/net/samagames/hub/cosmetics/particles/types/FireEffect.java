package net.samagames.hub.cosmetics.particles.types;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;


/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by Geekpower14 on 16/05/2015.
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public class FireEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    public FireEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 4;
        iterations = 2;
    }

    @Override
    public void onRun() {
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
