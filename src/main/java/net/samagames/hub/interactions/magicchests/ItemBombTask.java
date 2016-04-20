package net.samagames.hub.interactions.magicchests;

import net.samagames.hub.Hub;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

class ItemBombTask extends BukkitRunnable
{
    private final Hub hub;
    private final Location location;
    private final Random random;
    private int i;

    ItemBombTask(Hub hub, Location location)
    {
        this.hub = hub;
        this.location = location;
        this.random = new Random();
        this.i = 0;

        this.runTaskTimer(this.hub, 0L, 4L);
    }

    @Override
    public void run()
    {
        ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(this.random.nextLong() + "");

        itemStack.setItemMeta(meta);

        Item item = this.hub.getWorld().dropItem(this.location.clone().add(0.5, 1, 0.5), itemStack);
        item.setVelocity(new Vector((0.5 - this.random.nextDouble()) * 0.5, this.random.nextDouble() * 0.5, (0.5 - this.random.nextDouble()) * 0.5));

        item.setPickupDelay(1000000);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, item::remove, 60L);
        this.i++;

        if(this.i >= 10)
            this.cancel();
    }
}
