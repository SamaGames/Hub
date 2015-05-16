package net.samagames.hub.cosmetics.particles.types;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by Geekpower14 on 16/05/2015.
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public class WitchHatEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.REDSTONE;

    /**
     * Current angle. Works as counter
     */
    protected int angle = 0;

    protected double height = 1;

    protected double precision = 4;

    protected double marge = 0.5;

    protected double numberCirclePlate = 2;

    protected double plateWidth = 0.8;

    public WitchHatEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = -1;
        color = Color.PURPLE;
        this.asynchronous = true;
    }

    @Override
    public void onRun() {
        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () -> {
            Location location = getLocation().clone().add(0, 0.5, 0);
            for(int l = 0; l < 10; l++)
            {
                if(angle > 360)
                {
                    angle = 0;
                }

                for(int x = 1; x <= numberCirclePlate; x++)
                {
                    Vector v = getCircle(angle, (x/numberCirclePlate)*plateWidth);
                    v = normalizeFromHead(v);
                    display(particle, location.clone().add(v));
                    //particle.display(new Vector(0, -1, 0), 0F, location.clone().add(v), 50);
                }

                for(int x = 0; x <= precision; x++)
                {
                    Vector v = getCircle(angle, (x*(plateWidth-marge))/precision);
                    v = normalizeFromHead(v);
                    Vector hauteur = normalizeFromHead(new Vector(0, (precision-x)*(height/precision), 0));
                    display(particle, location.clone().add(v).add(hauteur));
                    //particle.display(new Vector(0,-1,0), 0F, location.clone().add(v).add(hauteur), 50);
                }

                angle+=20;
            }
        });
    }

    public Vector getCircle(int angle, double radius)
    {
        return new Vector(Math.cos(Math.toRadians(angle))*radius, 0, Math.sin(Math.toRadians(angle))*radius);
    }

    public Vector normalizeFromHead(Vector v)
    {
        VectorUtils.rotateAroundAxisX(v, this.getEntity().getLocation().getPitch() * MathUtils.degreesToRadians);
        VectorUtils.rotateAroundAxisY(v, -this.getEntity().getLocation().getYaw() * MathUtils.degreesToRadians);
        return v;
    }
}
