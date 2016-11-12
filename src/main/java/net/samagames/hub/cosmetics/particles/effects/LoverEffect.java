package net.samagames.hub.cosmetics.particles.effects;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

import java.util.Random;

public class LoverEffect extends Effect
{

    private Random random;

    public LoverEffect(EffectManager effectManager)
    {
        super(effectManager);
        this.type = EffectType.REPEATING;
        this.period = 4;
        this.iterations = -1;
        this.asynchronous = true;
        this.random = new Random();
    }

    @Override
    public void onRun()
    {
        double dx = this.random.nextDouble() % 0.2F;
        double dz = this.random.nextDouble() % 0.2F;
        display(ParticleEffect.HEART, this.getEntity().getLocation().add(dx, 2D, dz));
    }
}
