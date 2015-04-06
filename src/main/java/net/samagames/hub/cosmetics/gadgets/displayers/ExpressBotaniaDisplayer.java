package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.lobbyutils.Plugin;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

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

        for (byte i = 0; i < 9; i++)
        {
            flowers.add(new ItemStack(Material.RED_ROSE, 1, i));
        }

        this.loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.instance, new Runnable()
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

                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.instance, new Runnable() {
                    @Override
                    public void run() {
                        Location fwLocation = fw.getLocation();
                        fw.detonate();

                        for (int i = 0; i < 32; i++)
                        {
                            ItemStack flower = flowers.get(new Random().nextInt(flowers.size())).clone();
                            Item item = player.getWorld().dropItemNaturally(fwLocation, flower);

                            try
                            {
                                Plugin.gadgetMachine.ageField.set((((CraftItem) item).getHandle()), 5860);
                            }
                            catch (IllegalAccessException e)
                            {
                                e.printStackTrace();
                            }
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

    public boolean canUse()
    {
        return true;
    }

    private void callback() {
        Bukkit.getScheduler().cancelTask(this.loopId);
    }
}
