package net.samagames.hub.common.tasks;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.utils.ProximityUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TaskManager extends AbstractManager
{
    private final CirclesTask circlesTask;
    private final BirthdayTask birthdayTask;

    private final BukkitTask secretChamberProximityTask;
    private final BukkitTask pantheonProximityTask;
    private final BukkitTask bathProximityTask;

    public TaskManager(Hub hub)
    {
        super(hub);

        this.circlesTask = new CirclesTask(hub);
        this.birthdayTask = new BirthdayTask(hub);

        ArmorStand secretChamberDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), 88.5, 20.0D, 44.5D), ArmorStand.class);
        this.prepareProximityDetection(secretChamberDetectionEntity, "secret_chamber_proximity");
        this.secretChamberProximityTask = ProximityUtils.onNearbyOf(hub, secretChamberDetectionEntity, 5.0D, 5.0D, 5.0D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> SamaGamesAPI.get().getAchievementManager().getAchievementByID(7).unlock(player.getUniqueId())));

        ArmorStand pantheonDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), -174.5D, 82.0D, 27.5D), ArmorStand.class);
        this.prepareProximityDetection(pantheonDetectionEntity, "pantheon_proximity");
        this.pantheonProximityTask = ProximityUtils.onNearbyOf(hub, pantheonDetectionEntity, 3.0D, 5.0D, 3.0D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> SamaGamesAPI.get().getAchievementManager().getAchievementByID(10).unlock(player.getUniqueId())));

        ArmorStand bathDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), -148.5, 70.0D, 26.5D), ArmorStand.class);
        this.prepareProximityDetection(bathDetectionEntity, "bath_proximity");
        this.bathProximityTask = ProximityUtils.onNearbyOf(hub, bathDetectionEntity, 5.0D, 3.0D, 5.0D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> SamaGamesAPI.get().getAchievementManager().getAchievementByID(12).unlock(player.getUniqueId())));
    }

    @Override
    public void onDisable()
    {
        this.circlesTask.cancel();
        this.birthdayTask.cancel();

        this.secretChamberProximityTask.cancel();
        this.pantheonProximityTask.cancel();
        this.bathProximityTask.cancel();
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }

    public CirclesTask getCirclesTask()
    {
        return this.circlesTask;
    }

    private void prepareProximityDetection(ArmorStand armorStand, String customName)
    {
        armorStand.setCustomName(customName);
        armorStand.setCustomNameVisible(false);
        armorStand.getNearbyEntities(2.0, 2.0, 2.0).stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).filter(entity -> entity.getCustomName().equals(armorStand.getCustomName())).forEach(entity -> entity.remove());
    }
}
