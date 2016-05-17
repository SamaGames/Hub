package net.samagames.hub.cosmetics.gadgets.displayers;

import de.slikey.effectlib.effect.HeartEffect;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftItem;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class HolyCreeperDisplayer extends AbstractDisplayer
{
    private BukkitTask lovingTask;

    public HolyCreeperDisplayer(Hub hub, Player player)
    {
        super(hub, player);
    }

    @Override
    public void display()
    {
        Creeper creeper = this.baseLocation.getWorld().spawn(this.baseLocation.clone().add(0.0D, 5.0D, 0.0D), Creeper.class);
        creeper.setPowered(true);
        creeper.setMetadata("owner-id", new FixedMetadataValue(this.hub, this.player.getUniqueId().toString()));

        this.lovingTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, () ->
        {
            if(!creeper.isDead())
                ParticleEffect.HEART.display(0.25F, 0.5F, 0.25F, 1.0F, 6, creeper.getLocation(), 20.0D);
        }, 5L, 5L);
    }

    @Override
    public void handleInteraction(Entity who, Entity with)
    {
        if(with instanceof Creeper)
            this.explode((Creeper) with);
    }

    private void explode(Creeper creeper)
    {
        this.lovingTask.cancel();

        Location flowerSpawnLocation = creeper.getLocation().clone().add(0.0D, 1.5D, 0.0D);

        for(int i = 0; i < 64; i++)
        {
            ItemStack redDye = new ItemStack(Material.INK_SACK, 1, (short) 1);
            Item item = this.player.getWorld().dropItemNaturally(flowerSpawnLocation, redDye);
            item.setVelocity(new Vector(GadgetManager.RANDOM.nextInt(2) - 1, 1.5, GadgetManager.RANDOM.nextInt(2) - 1));

            try
            {
                GadgetManager.AGE_FIELD.set((((CraftItem) item).getHandle()), 5500);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        creeper.getWorld().strikeLightningEffect(creeper.getLocation());
        creeper.getWorld().createExplosion(creeper.getLocation().getX(), (creeper.getLocation().getY() + 2.0D), creeper.getLocation().getZ(), 2.5F, false, false);
        creeper.remove();

        HeartEffect heartEffect = new HeartEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        heartEffect.particle = de.slikey.effectlib.util.ParticleEffect.HEART;
        heartEffect.setLocation(flowerSpawnLocation.clone().add(0.0D, 1.5D, 0.0D));
        heartEffect.start();

        this.end();
    }

    @Override
    public boolean isInteractionsEnabled()
    {
        return false;
    }

    @Override
    public boolean canUse()
    {
        return true;
    }
}
