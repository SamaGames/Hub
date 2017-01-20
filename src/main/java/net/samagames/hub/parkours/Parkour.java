package net.samagames.hub.parkours;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.permissions.IPermissionsEntity;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.tools.ColorUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Parkour
{
    public static final String TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Parcours" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    private static final ItemStack QUIT_STACK;
    private static final ItemStack CHECKPOINT_STACK;

    protected final Hub hub;

    protected final Map<UUID, Long> playersIn;
    protected final Map<UUID, List<Location>> playersCheckpoints;
    protected final Map<UUID, Integer> tries;
    protected final List<UUID> locks;
    protected final String parkourName;
    protected final String prefix;
    protected final String winPrefix;
    protected final Location spawn;
    protected final Location end;
    protected final Location fail;
    protected final String stars;
    protected final int achievementId;
    protected final int minimalHeight;
    protected final int lifesOnCheckpoint;
    protected final ArmorStand startTooltip;
    protected final ArmorStand endTooltip;
    protected final List<ArmorStand> checkpointsTooltips;
    protected final ParkourBackend backend;

    public Parkour(Hub hub, String parkourName, String prefix, String winPrefix, Location spawn, Location end, Location fail, int minimalHeight, int lifesOnCheckpoint, List<Location> checkpoints, int difficulty, int achievementId)
    {
        this.hub = hub;

        this.playersIn = new ConcurrentHashMap<>();
        this.playersCheckpoints = new ConcurrentHashMap<>();
        this.tries = new ConcurrentHashMap<>();
        this.locks = new CopyOnWriteArrayList<>();

        this.parkourName = parkourName;
        this.prefix = prefix;
        this.winPrefix = winPrefix;
        this.spawn = spawn;
        this.end = end;
        this.fail = fail;
        this.achievementId = achievementId;

        String temporaryStars = "";

        for (int i = 0; i < difficulty; i++)
            temporaryStars += ChatColor.GOLD + "\u272F";

        for (int i = difficulty; i < 5; i++)
            temporaryStars += ChatColor.DARK_GRAY + "\u272F";

        this.stars = temporaryStars;
        this.minimalHeight = minimalHeight;
        this.lifesOnCheckpoint = lifesOnCheckpoint;

        spawn.getWorld().getNearbyEntities(spawn, 3.0D, 3.0D, 3.0D).stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).forEach(Entity::remove);
        end.getWorld().getNearbyEntities(end, 3.0D, 3.0D, 3.0D).stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).forEach(Entity::remove);

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
        this.checkpointsTooltips.forEach(Entity::remove);
    }

    public void checkpoint(Player player, Location location)
    {
        List<Location> playerCheckpoints = new ArrayList<>(this.playersCheckpoints.get(player.getUniqueId()));
        Location checkpointFormatted = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if (playerCheckpoints.contains(checkpointFormatted))
            return;

        playerCheckpoints.add(checkpointFormatted);

        this.playersCheckpoints.put(player.getUniqueId(), playerCheckpoints);
        this.tries.put(player.getUniqueId(), this.tries.get(player.getUniqueId()) + this.lifesOnCheckpoint);

        player.sendMessage(TAG + ChatColor.WHITE + "Checkpoint !");
    }

    public void addPlayer(Player player)
    {
        if (this.hub.getPlayerManager().isBusy(player))
        {
            player.sendMessage(TAG + ChatColor.RED + "Vous êtes actuellement occupé, veuillez réessayer plus tard. Ce message survient lors de l'utilisation d'un gadget ou d'une action à caractère prioritaire.");
            return;
        }

        List<Location> playerCheckpoints = new ArrayList<>();
        playerCheckpoints.add(this.spawn);

        this.playersIn.put(player.getUniqueId(), System.currentTimeMillis());
        this.playersCheckpoints.put(player.getUniqueId(), playerCheckpoints);
        this.tries.put(player.getUniqueId(), 0);

        player.sendMessage(TAG + ChatColor.WHITE + "Vous commencez " + this.winPrefix + " " + ChatColor.AQUA + this.parkourName + ChatColor.WHITE + ". Bonne chance !");

        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setWalkSpeed(0.2F);

            player.getInventory().clear();
            player.getInventory().setHeldItemSlot(4);

            if (this.checkpointsTooltips.isEmpty())
            {
                player.getInventory().setItem(4, QUIT_STACK);
            }
            else
            {
                player.getInventory().setItem(3, QUIT_STACK);
                player.getInventory().setItem(5, CHECKPOINT_STACK);
            }

            for (UUID playerIn : this.playersIn.keySet())
                player.hidePlayer(this.hub.getServer().getPlayer(playerIn));
        });

        if (this.hub.getCosmeticManager().getPetManager().getEquippedCosmetics(player) != null)
            this.hub.getCosmeticManager().getPetManager().disableCosmetics(player, false, false);
    }

    public void winPlayer(Player player)
    {
        UUID playerId = player.getUniqueId();
        long begin = this.playersIn.get(playerId);
        double duration = Math.floor((System.currentTimeMillis() - begin) / 100) / 10;

        this.hub.getServer().broadcastMessage(TAG + ChatColor.GREEN + player.getName() + ChatColor.WHITE + " a réussi " + this.winPrefix + " " + ChatColor.AQUA + this.parkourName + ChatColor.WHITE + " en " + ChatColor.GREEN + duration + ChatColor.WHITE + " secondes !");

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

        this.removePlayer(player);

        /*if (this.achievementName != null && !SamaGamesAPI.get().getAchievementManager().isUnlocked(player, this.achievementName))
            SamaGamesAPI.get().getAchievementManager().getAchievementByID(this.achievementName).unlock(player);*/

        this.restoreFly(player);
    }

    public void failPlayer(Player player)
    {
        if (this.locks.contains(player.getUniqueId()))
            return;

        this.locks.add(player.getUniqueId());

        int now = this.tries.get(player.getUniqueId()) - 1;
        this.tries.put(player.getUniqueId(), now);

        if (now > 0)
        {
            player.sendMessage(TAG + ChatColor.WHITE + "Il ne vous reste plus que " + now + " essai" + (now == 1 ? "" : "s") + " !");
            player.teleport(this.playersCheckpoints.get(player.getUniqueId()).get(this.playersCheckpoints.get(player.getUniqueId()).size() - 1));
        }
        else
        {
            player.sendMessage(TAG + ChatColor.WHITE + "Vous avez échoué :'(");
            player.teleport(this.fail);

            this.restoreFly(player);
            this.removePlayer(player);
        }

        this.locks.remove(player.getUniqueId());
    }

    public void quitPlayer(Player player)
    {
        player.sendMessage(TAG + ChatColor.WHITE + "Vous avez quitté le parcours :'(");
        player.teleport(this.fail);

        this.restoreFly(player);
        this.removePlayer(player);
    }

    public void removePlayer(Player player)
    {
        this.playersIn.remove(player.getUniqueId());
        this.playersCheckpoints.remove(player.getUniqueId());

        player.getInventory().clear();
        this.hub.getPlayerManager().getStaticInventory().setInventoryToPlayer(player);

        IPermissionsEntity permissionsEntity = SamaGamesAPI.get().getPermissionsManager().getPlayer(player.getUniqueId());

        if (permissionsEntity.hasPermission("network.vipplus") && SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isElytraActivated())
        {
            player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            this.hub.getPlayerManager().getStaticInventory().setInventoryToPlayer(player);
        }

        for (UUID playerIn : this.playersIn.keySet())
        {
            this.hub.getServer().getScheduler().runTask(this.hub, () ->
            {
                player.showPlayer(this.hub.getServer().getPlayer(playerIn));
                this.hub.getServer().getPlayer(playerIn).showPlayer(player);
            });
        }
    }

    public String getParkourName()
    {
        return this.parkourName;
    }

    public String getPrefix()
    {
        return this.prefix;
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

    public String getTag()
    {
        return TAG;
    }

    private void restoreFly(Player player)
    {
        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            player.setFlySpeed(PlayerManager.FLY_SPEED);
            player.setWalkSpeed(PlayerManager.WALK_SPEED);

            if (player.hasPermission("hub.fly"))
                player.setAllowFlight(true);
        });
    }

    static
    {
        QUIT_STACK = new ItemStack(Material.BARRIER, 1);

        ItemMeta quitStackMeta = QUIT_STACK.getItemMeta();
        quitStackMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Quitter le parcours");

        QUIT_STACK.setItemMeta(quitStackMeta);

        // ---

        CHECKPOINT_STACK = new ItemStack(Material.ENDER_PEARL, 1);

        ItemMeta checkpointStackMeta = CHECKPOINT_STACK.getItemMeta();
        checkpointStackMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Aller au dernier Checkpoint");

        CHECKPOINT_STACK.setItemMeta(checkpointStackMeta);
    }
}
