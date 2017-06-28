package net.samagames.hub.cosmetics.clothes;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.cameras.Camera;
import net.samagames.tools.npc.nms.CustomNPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 17/01/2017
 */
class ClothPreviewTask extends BukkitRunnable
{
    private static final double RADIUS = 5.0D;

    private final Location center;
    private final Camera camera;
    private final CustomNPC fakePlayer;
    private double i;

    ClothPreviewTask(Hub hub, Player player, ItemStack[] armorContent)
    {
        this.center = player.getLocation().clone();

        this.camera = SamaGamesAPI.get().getCameraManager().createCamera(this.center, false);
        this.camera.addViewer(player);

        this.fakePlayer = SamaGamesAPI.get().getNPCManager().createNPC(this.center, player.getUniqueId(), null, false);
        this.fakePlayer.getBukkitEntity().getInventory().setHelmet(armorContent[0]);
        this.fakePlayer.getBukkitEntity().getInventory().setChestplate(armorContent[1]);
        this.fakePlayer.getBukkitEntity().getInventory().setLeggings(armorContent[2]);
        this.fakePlayer.getBukkitEntity().getInventory().setBoots(armorContent[3]);

        SamaGamesAPI.get().getNPCManager().sendNPC(player, this.fakePlayer);

        this.runTaskTimer(hub, 1L, 1L);
    }

    @Override
    public void run()
    {
        Location location = new Location(this.center.getWorld(), this.center.getX() + Math.cos(this.i) * RADIUS, this.center.getY() + 1D, this.center.getZ() + Math.sin(this.i) * RADIUS);
        location.setDirection(this.center.clone().subtract(location).toVector().setY(location.getY()));
        location.setPitch(0.0F);

        this.camera.move(location);

        this.i += 0.025D;

        if (this.i > Math.PI * 2.0D)
            this.i = 0.0D;
    }

    public void stop()
    {
        SamaGamesAPI.get().getCameraManager().removeCamera(this.camera, this.center);
        SamaGamesAPI.get().getNPCManager().removeNPC(this.fakePlayer);

        this.cancel();
    }

    public Location getCenter()
    {
        return this.center;
    }
}
