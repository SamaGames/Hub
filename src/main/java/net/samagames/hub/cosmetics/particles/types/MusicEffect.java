package net.samagames.hub.cosmetics.particles.types;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Color;

import java.util.Random;

public class MusicEffect extends Effect
{
    private Random random;

    public MusicEffect(EffectManager effectManager)
    {
        super(effectManager);
        this.type = EffectType.REPEATING;
        this.period = 4;
        this.iterations = -1;
        this.asynchronous = true;
        this.random = new Random();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRun()
    {
        double dx = this.random.nextDouble() % 0.2F;
        double dz = this.random.nextDouble() % 0.2F;
        ParticleEffect.NOTE.display(this.getEntity().getLocation().add(dx, 2D, dz), visibleRange, 0, 0, 0, .5F, 1);
    }
}
