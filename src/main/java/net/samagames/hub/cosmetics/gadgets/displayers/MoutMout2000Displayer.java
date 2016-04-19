package net.samagames.hub.cosmetics.gadgets.displayers;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_9_R1.*;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.tools.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;

public class MoutMout2000Displayer extends AbstractDisplayer
{
    private BukkitTask loopTask;

    public MoutMout2000Displayer(Hub hub, Player player)
    {
        super(hub, player);
    }

    @Override
    public void display()
    {
        World craftbukkitWorld = ((CraftWorld) this.player.getWorld()).getHandle();
        MoutMout2000Sheep craftbukkitSheep = new MoutMout2000Sheep(craftbukkitWorld);
        craftbukkitSheep.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        craftbukkitWorld.addEntity(craftbukkitSheep, CreatureSpawnEvent.SpawnReason.CUSTOM);

        Sheep sheep = (Sheep) craftbukkitSheep.getBukkitEntity();
        sheep.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "MoutMout 2000");
        sheep.setCustomNameVisible(true);
        sheep.setColor(DyeColor.WHITE);

        this.loopTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, new Runnable()
        {
            int timer = 0;
            int ticks = 0;

            @Override
            public void run()
            {
                this.ticks++;

                if (ticks == 10)
                {
                    int r1i = GadgetManager.RANDOM.nextInt(17) + 1;
                    int r2i = GadgetManager.RANDOM.nextInt(17) + 1;
                    Color c1 = ColorUtils.getColor(r1i);
                    Color c2 = ColorUtils.getColor(r2i);

                    sheep.setColor(DyeColor.values()[GadgetManager.RANDOM.nextInt(DyeColor.values().length)]);
                    craftbukkitSheep.b(((CraftPlayer) player).getHandle());

                    FireworkEffect effect = FireworkEffect.builder().flicker(GadgetManager.RANDOM.nextBoolean()).withColor(c1).withFade(c2).with(FireworkEffect.Type.BURST).trail(GadgetManager.RANDOM.nextBoolean()).build();
                    FireworkUtils.launchfw(hub, sheep.getLocation(), effect);
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

                bField.set(this.goalSelector, Sets.newLinkedHashSet());
                bField.set(this.targetSelector, Sets.newLinkedHashSet());
                cField.set(this.goalSelector, Sets.newLinkedHashSet());
                cField.set(this.targetSelector, Sets.newLinkedHashSet());

                ((Navigation) getNavigation()).a(true);
                this.goalSelector.a(0, new PathfinderGoalPanic(this, 3.0D));
                this.goalSelector.a(1, new PathfinderGoalFloat(this));
            }
            catch (ReflectiveOperationException ignored) {}
        }

        @Override
        public boolean cK()
        {
            return true;
        }
    }
}
