package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftItem;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExpressBotaniaDisplayer extends AbstractDisplayer
{
    private BukkitTask loopTask;

    public ExpressBotaniaDisplayer(Hub hub, Player player)
    {
        super(hub, player);
    }

    @Override
    public void display()
    {
        List<ItemStack> flowers = new ArrayList<>();
        flowers.add(new ItemStack(Material.YELLOW_FLOWER, 1));
        flowers.add(new ItemStack(Material.LEAVES, 1));
        flowers.add(new ItemStack(Material.LONG_GRASS, 1));

        for (byte i = 0; i < 9; i++)
            flowers.add(new ItemStack(Material.RED_ROSE, 1, i));

        Color green = Color.fromRGB(100, 165, 120);
        this.player.getInventory().setHelmet(this.colorArmor(new ItemStack(Material.LEATHER_HELMET, 1), green));
        this.player.getInventory().setLeggings(this.colorArmor(new ItemStack(Material.LEATHER_LEGGINGS, 1), green));
        this.player.getInventory().setBoots(this.colorArmor(new ItemStack(Material.LEATHER_BOOTS, 1), green));

        this.loopTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, new Runnable()
        {
            int timer = 0;

            @Override
            public void run()
            {
                this.timer++;

                final Firework fw = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.GREEN).withFade(Color.LIME).with(FireworkEffect.Type.BURST).trail(false).build();
                fwm.addEffect(effect);
                fwm.setPower(2);
                fw.setFireworkMeta(fwm);

                hub.getServer().getScheduler().scheduleSyncDelayedTask(hub, () ->
                {
                    Location fwLocation = fw.getLocation();
                    fw.detonate();

                    for (int i = 0; i < 32; i++)
                    {
                        ItemStack flower = flowers.get(RANDOM.nextInt(flowers.size())).clone();
                        Item item = player.getWorld().dropItemNaturally(fwLocation, flower);

                        item.setMetadata("flower-swag-level", new FixedMetadataValue(hub, UUID.randomUUID().toString()));

                        try
                        {
                            GadgetManager.AGE_FIELD.set((((CraftItem) item).getHandle()), 5860);
                        }
                        catch (IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, 20L);

                if (this.timer == 10)
                {
                    callback();
                    end();
                }
            }
        }, 20L, 20L);
    }

    @Override
    public void handleInteraction(Entity who, Entity with) {}

    @Override
    public boolean isInteractionsEnabled()
    {
        return false;
    }

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        this.loopTask.cancel();

        this.player.getInventory().setHelmet(null);
        this.player.getInventory().setLeggings(null);
        this.player.getInventory().setBoots(null);
    }

    private ItemStack colorArmor(ItemStack stack, Color color)
    {
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);

        return stack;
    }
}
