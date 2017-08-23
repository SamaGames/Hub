package net.samagames.hub.cosmetics.gadgets.displayers;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_12_R1.*;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.tools.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        craftbukkitSheep.setPosition(this.player.getLocation().getX(), this.player.getLocation().getY(), this.player.getLocation().getZ());
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
                    craftbukkitSheep.lastDamager = ((CraftPlayer) player).getHandle();

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
        MoutMout2000Sheep(World world)
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
                this.goalSelector.a(0, new PathfinderGoalFloat(this));
                this.goalSelector.a(1, new PathfinderGoalPanic(this, 3.0D));
            }
            catch (ReflectiveOperationException ignored) {}
        }

        @Override
        public boolean cV()
        {
            return true;
        }
    }
}
