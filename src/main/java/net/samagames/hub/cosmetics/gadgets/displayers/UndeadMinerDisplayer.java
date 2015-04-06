package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.lobbyutils.Plugin;
import net.samagames.lobbyutils.utils.ParticleEffect;
import net.samagames.lobbyutils.utils.SimpleBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftItem;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class UndeadMinerDisplayer extends AbstractDisplayer
{
    private int loopId;

    public UndeadMinerDisplayer(Player player)
    {
        super(player);
    }

    @Override
    public void display()
    {
        final ArrayList<Material> ores = new ArrayList<>();
        ores.add(Material.COAL_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.DIAMOND_ORE);
        ores.add(Material.EMERALD_ORE);

        this.loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.instance, new Runnable()
        {
            int ticks = 0;
            int timer = 0;

            @Override
            public void run()
            {
                this.ticks += 2;

                Location temp = player.getLocation();
                boolean defaultSubstract = true;
                double toSubstract = 0.0D;

                for(int i = 0; i < 8; i++)
                {
                    toSubstract++;

                    if(temp.clone().subtract(0.0D, toSubstract, 0.0D).getBlock().getType() != Material.AIR)
                    {
                        defaultSubstract = false;
                        break;
                    }
                }

                if(!defaultSubstract)
                {
                    final Location actual = player.getLocation().subtract(0.0D, toSubstract, 0.0D);

                    if (this.ticks == 20)
                    {
                        this.ticks = 0;
                        this.timer++;

                        actual.getWorld().playSound(actual, Sound.ANVIL_LAND, 1.0F, 1.0F);
                    }

                    ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
                    Item item = player.getWorld().dropItemNaturally(player.getLocation(), pickaxe);

                    try
                    {
                        Plugin.gadgetMachine.ageField.set((((CraftItem) item).getHandle()), 5970);
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }

                    if (!isBlockUsed(actual) && Plugin.gadgetMachine.canPlaceBlockAt(actual))
                    {
                        final Material ore = ores.get(new Random().nextInt(ores.size()));
                        addBlockToUse(actual, new SimpleBlock(ore));
                        actual.getBlock().setType(ore);

                        Bukkit.getScheduler().runTaskLater(Plugin.instance, new Runnable() {
                            @Override
                            public void run() {
                                ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(ore, (byte) 0), 0.5F, 0.5F, 0.5F, 0.25F, 3, actual, 160.0);
                                actual.getWorld().playSound(actual, Sound.DIG_STONE, 1.0F, 1.0F);
                                restore(actual);
                            }
                        }, 20L * 8);
                    }

                    if (this.timer == 20 && this.ticks == 0)
                    {
                        end();
                        callback();
                    }
                }
            }
        }, 2L, 2L);
    }

    @Override
    public boolean canUse()
    {
        return true;
    }

    private void callback() {
        Bukkit.getScheduler().cancelTask(this.loopId);
    }
}
