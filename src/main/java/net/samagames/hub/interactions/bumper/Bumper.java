package net.samagames.hub.interactions.bumper;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.hub.utils.RestrictedVersion;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.ProximityUtils;
import net.samagames.tools.Titles;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

class Bumper extends AbstractInteraction implements Listener
{
    private final Location bumperLocation;
    private final BukkitTask startTask;
    private final ArmorStand startBeacon;
    private final Map<UUID, BukkitTask> flyTasks;
    private final Map<UUID, Integer> disclaimerCooldowns;
    private final List<UUID> flyingPlayers;
    private final double power;

    private static final double g = 18; //Minecraft constant for gravity (9.8 for earth)

    Bumper(Hub hub, String location) throws Exception
    {
        super(hub);

        String[] args = location.split(", ");

        this.bumperLocation = LocationUtils.str2loc(location);
        this.power = Double.parseDouble(args[6]);
        this.flyTasks = new HashMap<>();
        this.disclaimerCooldowns = new HashMap<>();
        this.flyingPlayers = new ArrayList<>();

        this.startBeacon = this.bumperLocation.getWorld().spawn(this.bumperLocation.clone().add(new Vector(
                Math.cos(this.bumperLocation.getYaw() * Math.PI * 2D / 180D),
                Math.cos(this.bumperLocation.getPitch() * Math.PI * 2D / 180D),
                Math.sin(this.bumperLocation.getYaw() * Math.PI * 2D / 180D)
        )), ArmorStand.class);

        this.startBeacon.setVisible(false);
        this.startBeacon.setGravity(false);

        Optional<Entity> enderCrystal = this.bumperLocation.getWorld().getNearbyEntities(this.bumperLocation, 5.0D, 5.0D, 5.0D).stream().filter(entity -> entity.getType() == EntityType.ENDER_CRYSTAL).findFirst();

        if (!enderCrystal.isPresent())
            throw new Exception("Failed to find the end crystal of this bumper.");

        ((EnderCrystal) enderCrystal.get()).setBeamTarget(this.startBeacon.getLocation());

        this.startTask = ProximityUtils.onNearbyOf(hub, this.startBeacon, 1D, 1D, 1D, Player.class, this::play);
    }

    @Override
    public void play(Player player)
    {
        if (this.flyingPlayers.contains(player.getUniqueId()))
            return;

        if (this.hub.getPlayerManager().isBusy(player) || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if (!RestrictedVersion.isLoggedInPost19(player))
        {
            if (!this.disclaimerCooldowns.containsKey(player.getUniqueId()))
            {
                player.sendMessage(ChatColor.RED + "Veuillez vous connecter avec une version supérieure ou égale à Minecraft 1.9 pour utiliser les Bumpers.");
                this.disclaimerCooldowns.put(player.getUniqueId(), 20);
            }
            else
            {
                int cooldown = this.disclaimerCooldowns.get(player.getUniqueId());
                cooldown--;

                this.disclaimerCooldowns.remove(player.getUniqueId());

                if (cooldown > 0)
                {
                    this.disclaimerCooldowns.put(player.getUniqueId(), cooldown);
                }
            }

            return;
        }

        this.hub.getServer().getScheduler().runTask(this.hub, () -> SamaGamesAPI.get().getAchievementManager().getAchievementByID(6).unlock(player.getUniqueId()));

        this.flyingPlayers.add(player.getUniqueId());

        Vector vec = this.bumperLocation.getDirection().multiply(this.power);
        long flyTime = (long) (((vec.getY()) / g) * 20.0);

        BukkitTask run = new BukkitRunnable()
        {
            double x = vec.getX() / 2;
            double y = vec.getY() / 2;
            double z = vec.getZ() / 2;

            @Override
            public void run()
            {
                ((CraftPlayer)player).getHandle().motX = x;
                ((CraftPlayer)player).getHandle().motY = y;
                ((CraftPlayer)player).getHandle().motZ = z;
                ((CraftPlayer)player).getHandle().velocityChanged = true;
            }
        }.runTaskTimer(this.hub, 0L, flyTime/2L);

        this.flyTasks.put(player.getUniqueId(), this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            ItemStack stack = new ItemStack(Material.ELYTRA);

            ItemMeta meta = stack.getItemMeta();
            meta.spigot().setUnbreakable(true);

            stack.setItemMeta(meta);

            player.getInventory().setChestplate(stack);
            ((CraftPlayer)player).getHandle().setFlag(7, true);

            this.hub.getPlayerManager().getStaticInventory().setInventoryToPlayer(player);
            this.hub.getServer().getPluginManager().callEvent(new EntityToggleGlideEvent(player, true));

            Titles.sendTitle(player, 10, 40, 10, "", ChatColor.GOLD + "Bon vol !");

            this.stop(player);
            run.cancel();
        }, flyTime));
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return (this.flyingPlayers.contains(player.getUniqueId()));
    }

    @Override
    public void onDisable()
    {
        this.startBeacon.remove();
        this.startTask.cancel();

        this.flyTasks.forEach(((uuid, bukkitTask) -> bukkitTask.cancel()));
    }

    @Override
    public void stop(Player player)
    {
        this.flyingPlayers.remove(player.getUniqueId());
        this.flyTasks.remove(player.getUniqueId());
    }
}
