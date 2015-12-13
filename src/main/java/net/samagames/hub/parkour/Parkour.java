package net.samagames.hub.parkour;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.ColorUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Parkour
{
    private final ConcurrentHashMap<UUID, Long> parkouring;
    private final ConcurrentHashMap<UUID, ArrayList<Location>> checkpoints;
    private final ConcurrentHashMap<UUID, Integer> tries;
    private final String parkourName;
    private final Location spawn;
    private final Location end;
    private final Location teleportFail;
    private final ArrayList<Material> whitelist;
    private final String achievementName;

    public Parkour(String parkourName, Location spawn, Location end, Location teleportFail, ArrayList<Material> whitelist, String achievementName)
    {
        this.parkouring = new ConcurrentHashMap<>();
        this.checkpoints = new ConcurrentHashMap<>();
        this.tries = new ConcurrentHashMap<>();

        this.parkourName = parkourName;
        this.spawn = spawn;
        this.end = end;
        this.teleportFail = teleportFail;
        this.whitelist = whitelist;
        this.achievementName = achievementName;

        whitelist.add(Material.AIR);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), () ->
        {
            for (UUID uuid : this.parkouring.keySet())
            {
                Player player = Bukkit.getPlayer(uuid);

                if (player == null || !player.isOnline())
                {
                    this.removePlayer(uuid);
                }
                else
                {
                    Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

                    if (block.getType() != Material.GOLD_PLATE && block.getType() != Material.IRON_PLATE)
                    {
                        if (!inWhitelist(block.getType()) && block.getType().isSolid())
                        {
                            this.failPlayer(player);
                        }
                    }
                }
            }
        }, 20L, 20L);
    }

    public void checkpoint(Player player, Location location)
    {
        ArrayList<Location> checkpoints = new ArrayList<>(this.checkpoints.get(player.getUniqueId()));
        Location checkpointFormatted = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if(checkpoints.contains(checkpointFormatted))
            return;

        checkpoints.add(checkpointFormatted);

        this.checkpoints.put(player.getUniqueId(), checkpoints);
        this.tries.put(player.getUniqueId(), (this.tries.get(player.getUniqueId()) + 3));

        player.sendMessage(Hub.getInstance().getParkourManager().getTag() + ChatColor.DARK_AQUA + "Checkpoint !");
    }

    public void addPlayer(Player player)
    {
        ArrayList<Location> checkpoints = new ArrayList<>();
        checkpoints.add(this.spawn);

        this.parkouring.put(player.getUniqueId(), System.currentTimeMillis());
        this.checkpoints.put(player.getUniqueId(), checkpoints);
        this.tries.put(player.getUniqueId(), 0);

        player.sendMessage(Hub.getInstance().getParkourManager().getTag() + ChatColor.DARK_AQUA + "Vous commencez le " + ChatColor.AQUA + parkourName + ChatColor.DARK_AQUA + ". Bonne chance !");
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setWalkSpeed(0.2F);

        if (Hub.getInstance().getCosmeticManager().getPetManager().hadPet(player))
            Hub.getInstance().getCosmeticManager().getPetManager().disableCosmetic(player, false);
    }

    public void winPlayer(final Player player)
    {
        UUID playerId = player.getUniqueId();
        long begin = parkouring.get(playerId);
        double duration = Math.floor((System.currentTimeMillis() - begin) / 100) / 10;

        Bukkit.broadcastMessage(Hub.getInstance().getParkourManager().getTag() + ChatColor.GREEN + player.getName() + ChatColor.DARK_AQUA + " a réussi le " + ChatColor.AQUA + parkourName + ChatColor.DARK_AQUA + " en " + duration + " secondes " + ChatColor.DARK_AQUA + "!");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Hub.getInstance(), new Runnable()
        {
            int count = 0;

            public void run()
            {
                if (count >= 20)
                    return;

                Firework fw = (Firework) player.getWorld().spawnEntity(player.getPlayer().getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                Random r = new Random();

                int rt = r.nextInt(4) + 1;
                FireworkEffect.Type type = FireworkEffect.Type.BALL;
                if (rt == 1)
                    type = FireworkEffect.Type.BALL;
                if (rt == 2)
                    type = FireworkEffect.Type.BALL_LARGE;
                if (rt == 3)
                    type = FireworkEffect.Type.BURST;
                if (rt == 4)
                    type = FireworkEffect.Type.CREEPER;
                if (rt == 5)
                    type = FireworkEffect.Type.STAR;

                int r1i = r.nextInt(17) + 1;
                int r2i = r.nextInt(17) + 1;
                Color c1 = ColorUtils.getColor(r1i);
                Color c2 = ColorUtils.getColor(r2i);

                FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
                fwm.addEffect(effect);

                int rp = r.nextInt(2) + 1;
                fwm.setPower(rp);

                fw.setFireworkMeta(fwm);

                this.count++;
            }

        }, 5L, 5L);

        this.parkouring.remove(playerId);

        if(this.achievementName != null)
            if(!SamaGamesAPI.get().getAchievementManager().isUnlocked(player, this.achievementName))
                SamaGamesAPI.get().getAchievementManager().getAchievementByID(this.achievementName).unlock(player);

        player.setFlySpeed(0.3F);
        player.setWalkSpeed(0.3F);

        if (player.hasPermission("hub.fly"))
            player.setAllowFlight(true);
    }

    public void failPlayer(final Player player)
    {
        int now = this.tries.get(player.getUniqueId()) - 1;
        this.tries.put(player.getUniqueId(), now);

        if(now > 0)
        {
            player.sendMessage(Hub.getInstance().getParkourManager().getTag() + ChatColor.DARK_AQUA + "Il vous reste plus que " + now + " essai" + (now == 1 ? "" : "s") + " !");
            player.teleport(this.checkpoints.get(player.getUniqueId()).get((this.checkpoints.get(player.getUniqueId()).size() - 1)));
        }
        else
        {
            player.sendMessage(Hub.getInstance().getParkourManager().getTag() + ChatColor.DARK_AQUA + "Vous avez échoué :'(");
            player.teleport(this.teleportFail);

            Bukkit.getScheduler().runTask(Hub.getInstance(), () ->
            {
                player.setFlySpeed(0.3F);
                player.setWalkSpeed(0.3F);
            });

            if (player.hasPermission("hub.fly"))
                Bukkit.getScheduler().runTask(Hub.getInstance(), () -> player.setAllowFlight(true));

            this.parkouring.remove(player.getUniqueId());
        }
    }

    public void removePlayer(UUID player)
    {
        this.parkouring.remove(player);
    }

    public Location getSpawn()
    {
        return this.teleportFail;
    }

    public Location getEnd()
    {
        return this.end;
    }

    public Location getBegin()
    {
        return this.spawn;
    }

    public boolean isParkouring(UUID player)
    {
        return this.parkouring.containsKey(player);
    }

    public boolean inWhitelist(Material material)
    {
        return this.whitelist.contains(material);
    }
}
