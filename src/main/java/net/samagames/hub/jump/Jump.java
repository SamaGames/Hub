package net.samagames.hub.jump;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.ColorUtils;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Jump
{
    private final ConcurrentHashMap<UUID, Long> jumping = new ConcurrentHashMap<>();
    private final String jumpName;
    private final Location spawn;
    private final Location end;
    private final Location teleportFail;
    private final ArrayList<Material> whitelist;
    private final String achievementName;

    public Jump(String jumpName, Location spawn, Location end, Location teleportFail, ArrayList<Material> whitelist, String achievementName)
    {
        this.jumpName = jumpName;
        this.spawn = spawn;
        this.end = end;
        this.teleportFail = teleportFail;
        this.whitelist = whitelist;
        this.achievementName = achievementName;

        Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                for (UUID uuid : jumping.keySet())
                {
                    Player player = Bukkit.getPlayer(uuid);

                    if (player == null || !player.isOnline())
                    {
                        removePlayer(uuid);
                    }
                    else
                    {
                        Material block = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();

                        if (!inWhitelist(block) && block.isSolid())
                            losePlayer(player);
                    }
                }
            }
        }, 20L * 5, 20L * 5);
    }

    public Location getSpawn() {
        return spawn;
    }

    public void addPlayer(Player player)
    {
        this.jumping.put(player.getUniqueId(), System.currentTimeMillis());

        player.sendMessage(Hub.getInstance().getJumpManager().getTag() + ChatColor.DARK_AQUA + "Vous commencez le " + ChatColor.AQUA + jumpName + ChatColor.DARK_AQUA + ". Bonne chance !");
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setWalkSpeed(0.1F);
        player.setFlySpeed(0.1F);

        /**
         * TODO: Remove pet
         *
        if (Plugin.cosmeticsManager.getPetsHandler().hadPet(player))
            Plugin.cosmeticsManager.getPetsHandler().removePet(player);
         **/
    }

    public void winPlayer(final Player player)
    {
        UUID playerId = player.getUniqueId();
        long begin = jumping.get(playerId);
        double duration = Math.floor((System.currentTimeMillis() - begin) / 100) / 10;

        Bukkit.broadcastMessage(Hub.getInstance().getJumpManager().getTag() + ChatColor.GREEN + player.getName() + ChatColor.DARK_AQUA + " a réussi le " + ChatColor.AQUA + jumpName + ChatColor.DARK_AQUA + " en " + duration + " secondes " + ChatColor.DARK_AQUA + "!");

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

                count++;
            }

        }, 5L, 5L);

        this.jumping.remove(playerId);

        if(this.achievementName != null)
        {
            if(!SamaGamesAPI.get().getAchievementManager().isUnlocked(player, this.achievementName))
            {
                SamaGamesAPI.get().getAchievementManager().getAchievementByName(this.achievementName).unlock(player);
            }
        }

        player.setFlySpeed(0.3F);
        player.setWalkSpeed(0.3F);

        if (player.hasPermission("lobby.fly"))
            player.setAllowFlight(true);
    }

    public void losePlayer(final Player player)
    {
        player.sendMessage(Hub.getInstance().getJumpManager().getTag() + ChatColor.DARK_AQUA + "Vous avez échoué :'(");
        player.teleport(this.teleportFail);

        player.setFlySpeed(0.3F);
        player.setWalkSpeed(0.3F);

        if (player.hasPermission("lobby.fly"))
            Bukkit.getScheduler().runTask(Hub.getInstance(), () -> player.setAllowFlight(true));

        this.jumping.remove(player.getUniqueId());
    }

    public void removePlayer(UUID player)
    {
        this.jumping.remove(player);
    }

    public Location getEnd()
    {
        return this.end;
    }

    public Location getBegin()
    {
        return this.spawn;
    }

    public boolean isJumping(UUID player)
    {
        return this.jumping.containsKey(player);
    }

    public boolean inWhitelist(Material material)
    {
        return this.whitelist.contains(material);
    }
}
