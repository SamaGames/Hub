package net.samagames.hub.parkours;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.tools.ColorUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Parkour
{
    private static final String TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Parcours" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    
    private final Hub hub;

    private final Map<UUID, Long> playersIn;
    private final Map<UUID, List<Location>> playersCheckpoints;
    private final Map<UUID, Integer> tries;
    private final String parkourName;
    private final Location spawn;
    private final Location end;
    private final Location fail;
    private final List<Material> whitelist;
    private final String achievementName;
    private final String stars;
    private final int minimalHeight;
    private final ArmorStand startTooltip;
    private final ArmorStand endTooltip;
    private final List<ArmorStand> checkpointsTooltips;
    private final ParkourBackend backend;

    public Parkour(Hub hub, String parkourName, Location spawn, Location end, Location fail, int minimalHeight, List<Location> checkpoints, List<Material> whitelist, int difficulty, String achievementName)
    {
        this.hub = hub;

        this.playersIn = new ConcurrentHashMap<>();
        this.playersCheckpoints = new ConcurrentHashMap<>();
        this.tries = new ConcurrentHashMap<>();

        this.parkourName = parkourName;
        this.spawn = spawn;
        this.end = end;
        this.fail = fail;
        this.whitelist = whitelist;
        this.achievementName = achievementName;

        String temporaryStars = "";

        for (int i = 0; i < difficulty; i++)
            temporaryStars += ChatColor.GOLD + "\u272F";

        for (int i = difficulty; i < 5; i++)
            temporaryStars += ChatColor.DARK_GRAY + "\u272F";

        this.stars = temporaryStars;
        this.minimalHeight = minimalHeight;

        whitelist.add(Material.AIR);
        whitelist.add(Material.TORCH);
        whitelist.add(Material.REDSTONE_TORCH_ON);
        whitelist.add(Material.REDSTONE_TORCH_OFF);
        whitelist.add(Material.IRON_PLATE);
        whitelist.add(Material.GOLD_PLATE);
        whitelist.add(Material.STONE_PLATE);
        whitelist.add(Material.WOOD_PLATE);
        whitelist.add(Material.SIGN);
        whitelist.add(Material.SIGN_POST);
        whitelist.add(Material.WALL_SIGN);

        spawn.getWorld().getNearbyEntities(spawn, 5.0D, 5.0D, 5.0D).stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).forEach(Entity::remove);
        end.getWorld().getNearbyEntities(end, 5.0D, 5.0D, 5.0D).stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).forEach(Entity::remove);

        for (Location checkpoint : checkpoints)
            checkpoint.getWorld().getNearbyEntities(checkpoint, 5.0D, 5.0D, 5.0D).stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).forEach(Entity::remove);

        this.startTooltip = spawn.getWorld().spawn(spawn.clone().subtract(0.0D, 0.5D, 0.0D), ArmorStand.class);
        this.startTooltip.setVisible(false);
        this.startTooltip.setGravity(false);
        this.startTooltip.setCustomNameVisible(true);
        this.startTooltip.setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Départ");

        this.endTooltip = end.getWorld().spawn(end.clone().subtract(0.0D, 0.5D, 0.0D), ArmorStand.class);
        this.endTooltip.setVisible(false);
        this.endTooltip.setGravity(false);
        this.endTooltip.setCustomNameVisible(true);
        this.endTooltip.setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Arrivée");

        this.checkpointsTooltips = new ArrayList<>();

        for (Location checkpoint : checkpoints)
        {
            ArmorStand tooltip = checkpoint.getWorld().spawn(checkpoint.clone().subtract(0.0D, 1.0D, 0.0D), ArmorStand.class);
            tooltip.setVisible(false);
            tooltip.setGravity(false);
            tooltip.setCustomNameVisible(true);
            tooltip.setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Checkpoint");

            this.checkpointsTooltips.add(tooltip);
        }

        this.backend = new ParkourBackend(this.hub, this);

        this.hub.getTaskManager().getCirclesTask().addCircleAt(spawn);
        this.hub.getTaskManager().getCirclesTask().addCircleAt(end);
    }

    public void onDisable()
    {
        this.backend.stop();

        this.startTooltip.remove();
        this.endTooltip.remove();
        this.checkpointsTooltips.stream().forEach(Entity::remove);
    }

    public void checkpoint(Player player, Location location)
    {
        List<Location> playerCheckpoints = new ArrayList<>(this.playersCheckpoints.get(player.getUniqueId()));
        Location checkpointFormatted = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if(playerCheckpoints.contains(checkpointFormatted))
            return;

        playerCheckpoints.add(checkpointFormatted);

        this.playersCheckpoints.put(player.getUniqueId(), playerCheckpoints);
        this.tries.put(player.getUniqueId(), this.tries.get(player.getUniqueId()) + 3);

        player.sendMessage(TAG + ChatColor.WHITE + "Checkpoint !");
    }

    public void addPlayer(Player player)
    {
        List<Location> playerCheckpoints = new ArrayList<>();
        playerCheckpoints.add(this.spawn);

        this.playersIn.put(player.getUniqueId(), System.currentTimeMillis());
        this.playersCheckpoints.put(player.getUniqueId(), playerCheckpoints);
        this.tries.put(player.getUniqueId(), 0);

        player.sendMessage(TAG + ChatColor.WHITE + "Vous commencez le " + ChatColor.AQUA + this.parkourName + ChatColor.WHITE + ". Bonne chance !");

        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setWalkSpeed(0.2F);
        });

        // TODO: Check if a player has a pet and delete it
        // TODO: Check if a player is in a gadget, prevent fly setting
    }

    public void winPlayer(final Player player)
    {
        UUID playerId = player.getUniqueId();
        long begin = this.playersIn.get(playerId);
        double duration = Math.floor((System.currentTimeMillis() - begin) / 100) / 10;

        this.hub.getServer().broadcastMessage(TAG + ChatColor.GREEN + player.getName() + ChatColor.WHITE + " a réussi le " + ChatColor.AQUA + this.parkourName + ChatColor.WHITE + " en " + ChatColor.GREEN + duration + ChatColor.WHITE + " secondes !");

        this.hub.getServer().getScheduler().scheduleSyncRepeatingTask(this.hub, new Runnable()
        {
            int count = 0;

            @Override
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

        this.playersIn.remove(playerId);

        if(this.achievementName != null && !SamaGamesAPI.get().getAchievementManager().isUnlocked(player, this.achievementName))
            SamaGamesAPI.get().getAchievementManager().getAchievementByID(this.achievementName).unlock(player);

        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            player.setFlySpeed(PlayerManager.FLY_SPEED);
            player.setWalkSpeed(PlayerManager.WALK_SPEED);

            if (player.hasPermission("hub.fly"))
                player.setAllowFlight(true);
        });
    }

    public void failPlayer(final Player player)
    {
        int now = this.tries.get(player.getUniqueId()) - 1;
        this.tries.put(player.getUniqueId(), now);

        if(now > 0)
        {
            player.sendMessage(TAG + ChatColor.WHITE + "Il ne vous reste plus que " + now + " essai" + (now == 1 ? "" : "s") + " !");
            player.teleport(this.playersCheckpoints.get(player.getUniqueId()).get(this.playersCheckpoints.get(player.getUniqueId()).size() - 1));
        }
        else
        {
            player.sendMessage(TAG + ChatColor.WHITE + "Vous avez échoué :'(");
            player.teleport(this.fail);

            this.hub.getServer().getScheduler().runTask(this.hub, () ->
            {
                player.setFlySpeed(PlayerManager.FLY_SPEED);
                player.setWalkSpeed(PlayerManager.WALK_SPEED);

                if (player.hasPermission("hub.fly"))
                    player.setAllowFlight(true);
            });

            this.removePlayer(player.getUniqueId());
        }
    }

    public void removePlayer(UUID player)
    {
        this.playersIn.remove(player);
        this.playersCheckpoints.remove(player);
    }

    public String getParkourName()
    {
        return this.parkourName;
    }

    public Location getSpawn()
    {
        return this.spawn;
    }

    public Location getEnd()
    {
        return this.end;
    }

    public Location getFail()
    {
        return this.fail;
    }

    public String getStars()
    {
        return this.stars;
    }

    public int getMinimalHeight()
    {
        return this.minimalHeight;
    }

    public ArmorStand getStartTooltip()
    {
        return this.startTooltip;
    }

    public ArmorStand getEndTooltip()
    {
        return this.endTooltip;
    }

    public Map<UUID, Long> getPlayersIn()
    {
        return this.playersIn;
    }

    public boolean isParkouring(UUID player)
    {
        return this.playersIn.containsKey(player);
    }

    public boolean isWhitelisted(Material material)
    {
        return this.whitelist.contains(material);
    }

    public String getTag()
    {
        return TAG;
    }
}