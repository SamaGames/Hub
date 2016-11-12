package net.samagames.hub.cosmetics.particles.effects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;

public class StepEffect extends Effect
{
    private boolean p = false;
    private Location last = null;

    public StepEffect(EffectManager effectManager)
    {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 4;
        iterations = -1;
        this.asynchronous = true;
    }

    @Override
    public void onRun()
    {
        // Prevents an excess of particles
        if (last != null && last.getX() == getEntity().getLocation().getX() && last.getZ() == getEntity().getLocation().getZ())
            return;
        last = getEntity().getLocation();

        Block block = this.getEntity().getLocation().add(0, -0.4, 0).getBlock();
        Material type = block.getType();

        // If the step should be displayed or not
        if (type.isBlock() && type.isSolid() && !type.isTransparent()) {
            Location loc = getEntity().getLocation();
            loc.setY(block.getY());
            loc = loc.add(0, 1 + Math.random() / 100, 0);
            Vector dir = VectorUtils.rotateAroundAxisY(getEntity().getLocation().getDirection().setY(0).normalize(), p ? 90 : -90).multiply(0.25);
            display(ParticleEffect.FOOTSTEP, loc.add(dir.getX(), 0, dir.getZ()), 7, 0);

            p = !p;
        }
    }

}