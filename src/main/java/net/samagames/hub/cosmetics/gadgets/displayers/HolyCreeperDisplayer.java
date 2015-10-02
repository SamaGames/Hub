package net.samagames.hub.cosmetics.gadgets.displayers;

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

    public void explode(Location location)
    {
        this.lovingTask.cancel();

        Location flowerSpawnLocation = location.clone().add(0.0D, 0.5D, 0.0D);

        for(int i = 0; i < 64; i++)
        {
            ItemStack redDye = new ItemStack(Material.INK_SACK, 1, (short) 1);
            Item item = this.player.getWorld().dropItemNaturally(flowerSpawnLocation, redDye);
            item.setVelocity(new Vector(new Random().nextInt(6) - 3, 2, new Random().nextInt(6) - 3));

            try
            {
                Hub.getInstance().getCosmeticManager().getGadgetManager().ageField.set((((CraftItem) item).getHandle()), 5750);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleInteraction(Entity who, Entity with)
    {
        Creeper creeper = (Creeper) with;
        creeper.getWorld().createExplosion(creeper.getLocation().getX(), creeper.getLocation().getY(), creeper.getLocation().getZ(), 1.0F, false, false);

        this.explode(creeper.getLocation());
    }

    @Override
    public boolean canUse()
    {
        return true;
    }
}
