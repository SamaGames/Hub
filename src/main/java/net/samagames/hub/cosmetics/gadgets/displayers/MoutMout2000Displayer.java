package net.samagames.hub.cosmetics.gadgets.displayers;

import net.minecraft.server.v1_8_R1.World;
import net.samagames.lobbyutils.Plugin;
import net.samagames.lobbyutils.utils.Colors;
import net.samagames.lobbyutils.utils.FireworkUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.util.UnsafeList;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.util.Random;

public class MoutMout2000Displayer extends AbstractDisplayer
{
    private int loopId;

    public MoutMout2000Displayer(Player player)
    {
        super(player);
    }

    @Override
    public void display()
    {
        World craftbukkitWorld = ((CraftWorld) player.getWorld()).getHandle();
        final MoutMout2000Sheep craftbukkitSheep = new MoutMout2000Sheep(craftbukkitWorld);
        craftbukkitSheep.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        craftbukkitWorld.addEntity(craftbukkitSheep, CreatureSpawnEvent.SpawnReason.CUSTOM);

        final Sheep sheep = (Sheep) craftbukkitSheep.getBukkitEntity();
        sheep.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "MoutMout 2000");
        sheep.setCustomNameVisible(true);
        sheep.setColor(DyeColor.WHITE);

        this.loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.instance, new Runnable()
        {
            int timer = 0;
            int ticks = 0;

            @Override
            public void run()
            {
                this.ticks++;

                if (ticks == 10)
                {
                    Random r = new Random();
                    int r1i = r.nextInt(17) + 1;
                    int r2i = r.nextInt(17) + 1;
                    Color c1 = Colors.getColor(r1i);
                    Color c2 = Colors.getColor(r2i);

                    sheep.setColor(DyeColor.values()[r.nextInt(DyeColor.values().length)]);
                    craftbukkitSheep.b(((CraftPlayer) player).getHandle());

                    FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(FireworkEffect.Type.BURST).trail(r.nextBoolean()).build();
                    FireworkUtils.launchfw(sheep.getLocation(), effect);
                }
                else if (ticks == 20)
                {
                    this.ticks = 0;
                    this.timer++;
                }

                if (timer == 15)
                {
                    sheep.getLocation().getWorld().createExplosion(sheep.getLocation().getX(), sheep.getLocation().getY(), sheep.getLocation().getZ(), 5, false, false);
                    sheep.remove();
                    end();
                    callback();
                }
            }
        }, 1L, 1L);
    }

    public boolean canUse()
    {
        return true;
    }

    private void callback()
    {
        Bukkit.getScheduler().cancelTask(this.loopId);
    }

    public static class MoutMout2000Sheep extends EntitySheep
    {

        public MoutMout2000Sheep(World world)
        {
            super(world);
            Field bField;

            try
            {
                bField = PathfinderGoalSelector.class.getDeclaredField("b");
                bField.setAccessible(true);
                Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
                cField.setAccessible(true);

                //Reset Pathfinder list
                bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
                bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
                cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
                cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());

                ((Navigation) getNavigation()).a(true);
                this.goalSelector.a(0, new PathfinderGoalPanic(this, 3.0D));
                this.goalSelector.a(1, new PathfinderGoalFloat(this));
            }
            catch (Exception ex) {}

        }

        @Override
        public void h()
        {
            super.h();
        }

        @Override
        public boolean bV()
        {
            return true;
        }
    }
}
