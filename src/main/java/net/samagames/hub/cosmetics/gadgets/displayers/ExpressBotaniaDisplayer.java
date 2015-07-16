package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftItem;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Random;

public class ExpressBotaniaDisplayer extends AbstractDisplayer
{
    private int loopId;

    public ExpressBotaniaDisplayer(Player player)
    {
        super(player);
    }

    @Override
    public void display()
    {
        final ArrayList<ItemStack> flowers = new ArrayList<>();
        flowers.add(new ItemStack(Material.YELLOW_FLOWER, 1));
        flowers.add(new ItemStack(Material.LEAVES, 1));
        flowers.add(new ItemStack(Material.LONG_GRASS, 1));

        for (byte i = 0; i < 9; i++)
        {
            flowers.add(new ItemStack(Material.RED_ROSE, 1, i));
        }

        Color green = Color.fromRGB(100, 165, 120);
        this.player.getInventory().setHelmet(this.colorArmor(new ItemStack(Material.LEATHER_HELMET, 1), green));
        this.player.getInventory().setLeggings(this.colorArmor(new ItemStack(Material.LEATHER_LEGGINGS, 1), green));
        this.player.getInventory().setBoots(this.colorArmor(new ItemStack(Material.LEATHER_BOOTS, 1), green));

        this.loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Hub.getInstance(), new Runnable()
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

                Bukkit.getScheduler().scheduleSyncDelayedTask(Hub.getInstance(), () ->
                {
                    Location fwLocation = fw.getLocation();
                    fw.detonate();

                    for (int i = 0; i < 32; i++)
                    {
                        ItemStack flower = flowers.get(new Random().nextInt(flowers.size())).clone();
                        Item item = player.getWorld().dropItemNaturally(fwLocation, flower);

                        try
                        {
                            Hub.getInstance().getCosmeticManager().getGadgetManager().ageField.set((((CraftItem) item).getHandle()), 5860);
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
    public void handleInteraction(Entity with) {}

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        Bukkit.getScheduler().cancelTask(this.loopId);

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
