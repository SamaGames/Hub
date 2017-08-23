package net.samagames.hub.cosmetics.balloons;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.utils.EntityUtils;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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
class BalloonCosmetic extends AbstractCosmetic
{
    private Map<UUID, LivingEntity[]> balloons;
    private Map<UUID, BukkitTask> destroyTasks;

    private EntityType entityType;
    private int count;
    private Random random;
    private String name;

    BalloonCosmetic(Hub hub, int storageId, EntityType entityType, String name, int count) throws Exception
    {
        super(hub, "Ballon", storageId);

        this.entityType = entityType;
        this.count = count;
        this.balloons = new HashMap<>();
        this.random = new Random();
        this.destroyTasks = new HashMap<>();
        this.name = name;
    }

    BalloonCosmetic(Hub hub, int storageId, EntityType entityType, int count) throws Exception
    {
        this(hub, storageId, entityType, null, count);
    }

    public void spawn(Player player)
    {
        LivingEntity[] livingEntities = new LivingEntity[this.count];

        for (int i = 0; i < this.count; i++)
        {
            livingEntities[i] = (LivingEntity)player.getWorld().spawnEntity(player.getLocation().add(random.nextDouble() % 2 - 1, 2.5D, random.nextDouble() % 2 - 1), this.entityType);
            livingEntities[i].addPotionEffect(PotionEffectType.LEVITATION.createEffect(Integer.MAX_VALUE, 2));
            ((CraftLivingEntity)livingEntities[i]).getHandle().setInvisible(true);
            EntityUtils.freezeEntity(livingEntities[i]);
            livingEntities[i].setLeashHolder(player);

            if (this.name != null)
                livingEntities[i].setCustomName(this.name);
        }

        this.destroyTasks.put(player.getUniqueId(), this.hub.getServer().getScheduler().runTaskTimer(this.hub, () -> this.autoCheck(player), 2L, 2L));
        this.balloons.put(player.getUniqueId(), livingEntities);
    }

    public void remove(Player player)
    {
        BukkitTask bukkitTask = this.destroyTasks.get(player.getUniqueId());

        if (bukkitTask != null)
            bukkitTask.cancel();

        LivingEntity[] livingEntities = this.balloons.get(player.getUniqueId());

        if (livingEntities == null)
            return;

        this.balloons.remove(player.getUniqueId());

        for (LivingEntity livingEntity : livingEntities)
            livingEntity.remove();
    }

    private void autoCheck(Player player)
    {
        LivingEntity[] livingEntities = this.balloons.get(player.getUniqueId());

        if (livingEntities == null)
            return;

        for (LivingEntity livingEntity : livingEntities)
        {
            if (livingEntity.isDead() || !livingEntity.isLeashed())
            {
                this.hub.getCosmeticManager().getBalloonManager().disableCosmetic(player, this, false, false);
                break ;
            }
        }
    }
}
