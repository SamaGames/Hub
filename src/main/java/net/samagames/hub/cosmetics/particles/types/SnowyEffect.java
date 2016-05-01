package net.samagames.hub.cosmetics.particles.types;

import java.util.Random;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class SnowyEffect extends Effect {

    private final Random random = new Random();

    public SnowyEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = -1;
        this.asynchronous = true;
    }

    @Override
    public void onRun() {
        // Cloud
        for(int i = 0; i < 20; i++)
            display(ParticleEffect.CLOUD, getEntity().getLocation().add(random.nextDouble() - 0.5, 2.75 + random.nextDouble() / 4, random.nextDouble() - 0.5));

        // Snow drops
        if(random.nextBoolean())
            display(ParticleEffect.SNOW_SHOVEL, getEntity().getLocation().add(random.nextDouble() * 0.8 - 0.4, 2.75 + random.nextDouble() / 4, random.nextDouble() * 0.8 - 0.4), 7, 0);
    }
}