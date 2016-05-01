package net.samagames.hub.cosmetics.particles.types;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.ParticleEffect;

import java.util.Random;

public class EnchantedEffect extends Effect {

    private Random random;

    public EnchantedEffect(EffectManager effectManager) {
        super(effectManager);
        this.period = 3;
        this.iterations = 4;
        this.random = new Random();
    }

    @Override
    public void onRun() {
        double dx = this.random.nextDouble() % 1.5F;
        double dy = this.random.nextDouble() % 2F;
        double dz = this.random.nextDouble() % 1.5F;
        display(ParticleEffect.ENCHANTMENT_TABLE, this.getEntity().getLocation().add(dx, dy, dz));
    }
}
