package net.samagames.hub.cosmetics.particles.types;

import java.util.Random;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

/**
 * Creates a cloud above the player which drops water.
 * @author 6infinity8
 */
public class RainyEffect extends Effect {
	
	private final Random random = new Random();
	
	public RainyEffect(EffectManager effectManager) {
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

		// Water drops
		if(random.nextBoolean())
			display(ParticleEffect.DRIP_WATER, getEntity().getLocation().add(random.nextDouble() * 0.8 - 0.4, 2.75 + random.nextDouble() / 4, random.nextDouble() * 0.8 - 0.4), 7, 0);
	}

}
