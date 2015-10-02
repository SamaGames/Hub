package net.samagames.hub.cosmetics.gadgets.displayers;

import de.slikey.effectlib.effect.HeartEffect;
import net.samagames.hub.Hub;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Random;

public class HolyCreeperDisplayer extends AbstractDisplayer
{
    private BukkitTask lovingTask;

    public HolyCreeperDisplayer(Player player)
    {
        super(player);
    }

    @Override
    public void display()
    {
        Creeper creeper = this.baseLocation.getWorld().spawn(this.baseLocation.clone().add(0.0D, 5.0D, 0.0D), Creeper.class);
        creeper.setPowered(true);
        creeper.setMetadata("owner-id", new FixedMetadataValue(Hub.getInstance(), this.player.getUniqueId().toString()));

        this.lovingTask = Bukkit.getScheduler().runTaskTimer(Hub.getInstance(), () ->
        {
            if(!creeper.isDead())
                ParticleEffect.HEART.display(0.25F, 0.5F, 0.25F, 1.0F, 6, creeper.getLocation(), 20.0D);
        }, 5L, 5L);
    }

    public void explode(Creeper creeper)
    {
        this.lovingTask.cancel();

        Location flowerSpawnLocation = creeper.getLocation().clone().add(0.0D, 1.5D, 0.0D);

        for(int i = 0; i < 64; i++)
        {
            ItemStack redDye = new ItemStack(Material.INK_SACK, 1, (short) 1);
            Item item = this.player.getWorld().dropItemNaturally(flowerSpawnLocation, redDye);
            item.setVelocity(new Vector(new Random().nextInt(2) - 1, 1.5, new Random().nextInt(2) - 1));

            try
            {
                Hub.getInstance().getCosmeticManager().getGadgetManager().ageField.set((((CraftItem) item).getHandle()), 5500);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        creeper.getWorld().strikeLightningEffect(creeper.getLocation());
        creeper.getWorld().createExplosion(creeper.getLocation().getX(), (creeper.getLocation().getY() + 2.0D), creeper.getLocation().getZ(), 2.5F, false, false);
        creeper.remove();

        HeartEffect heartEffect = new HeartEffect(Hub.getInstance().getCosmeticManager().getParticleManager().getEffectManager());
        heartEffect.particle = de.slikey.effectlib.util.ParticleEffect.HEART;
        heartEffect.setLocation(flowerSpawnLocation.clone().add(0.0D, 1.5D, 0.0D));
        heartEffect.start();
    }

    @Override
    public void handleInteraction(Entity who, Entity with)
    {
        this.explode((Creeper) with);
    }

    @Override
    public boolean canUse()
    {
        return true;
    }
}
