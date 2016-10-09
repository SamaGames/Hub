package net.samagames.hub.common.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.permissions.IPermissionsEntity;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.utils.RestrictedVersion;
import net.samagames.tools.Area;
import net.samagames.tools.InventoryUtils;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.PlayerUtils;
import net.samagames.tools.chat.ActionBarAPI;
import net.samagames.tools.chat.fanciful.FancyMessage;
import net.samagames.tools.teamspeak.TeamSpeakAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class PlayerManager extends AbstractManager
{
    public static final String SETTINGS_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Paramêtres" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String MODERATING_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Modération" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String SHOPPING_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Boutique" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String COSMETICS_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Cosmétique" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    public static final String RULES_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Règles" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;

    public static final Area VIP_ZONE = new Area(new Location(null, 98.0D, 255.0D, 412.0D), new Location(null, -68.0D, 0.0D, 546.0D));

    public static final float WALK_SPEED = 0.20F;
    public static final float FLY_SPEED = 0.15F;

    private final Map<UUID, Location> selections;
    private final Map<UUID, BukkitTask> rulesBookTasks;
    private final List<UUID> hiders;
    private final StaticInventory staticInventory;
    private final Location spawn;

    private boolean canBuild;

    public PlayerManager(Hub hub)
    {
        super(hub);

        this.selections = new HashMap<>();
        this.rulesBookTasks = new HashMap<>();
        this.hiders = new ArrayList<>();
        this.staticInventory = new StaticInventory(hub);
        this.spawn = LocationUtils.str2loc(hub.getConfig().getString("spawn"));
        this.canBuild = false;

        this.spawn.getWorld().setSpawnLocation(this.spawn.getBlockX(), this.spawn.getBlockY(), this.spawn.getBlockZ());
    }

    @Override
    public void onDisable()
    {
        this.selections.clear();
        this.hiders.clear();

        this.rulesBookTasks.values().forEach(BukkitTask::cancel);
        this.rulesBookTasks.clear();
    }

    @Override
    public void onLogin(Player player)
    {
        this.log(Level.INFO, "Handling login from '" + player.getUniqueId() + "'...");

        this.hub.getScheduledExecutorService().execute(() ->
        {
            this.hub.getServer().getScheduler().runTask(this.hub, () ->
            {
                player.setGameMode(GameMode.ADVENTURE);
                player.setWalkSpeed(WALK_SPEED);
                player.setFlySpeed(FLY_SPEED);
                player.setAllowFlight(false);
                player.setFoodLevel(20);
                player.setHealth(20.0D);
                InventoryUtils.cleanPlayer(player);

                Jedis jedis = SamaGamesAPI.get().getBungeeResource();
                String key = "lastgame:" + player.getUniqueId().toString();

                if (jedis != null && jedis.exists(key))
                {
                    String lastGame = jedis.get(key);
                    jedis.del(key);

                    if (this.hub.getGameManager().getGameByIdentifier(lastGame) != null)
                        player.teleport(this.hub.getGameManager().getGameByIdentifier(lastGame).getLobbySpawn());
                    else
                        player.teleport(this.spawn);
                }
                else
                {
                    player.teleport(this.spawn);
                }

                if (jedis != null)
                    jedis.close();

                player.getInventory().clear();

                this.staticInventory.setInventoryToPlayer(player);

                if (RestrictedVersion.isLoggedInPost19(player))
                {
                    try
                    {
                        IPermissionsEntity permissionsEntity = SamaGamesAPI.get().getPermissionsManager().getPlayer(player.getUniqueId());

                        if (permissionsEntity.hasPermission("network.vipplus") && SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isElytraActivated())
                        {
                            ItemStack elytra = new ItemStack(Material.ELYTRA);
                            ItemMeta meta = elytra.getItemMeta();
                            meta.spigot().setUnbreakable(true);
                            elytra.setItemMeta(meta);

                            player.getInventory().setChestplate(elytra);
                        }
                    }
                    catch (NullPointerException ignored)
                    {
                        player.sendMessage(ChatColor.RED + "Une erreur a été détectée lors du chargement de votre joueur, vous devrez peut-être vous reconnecter.");
                    }
                }

                this.updateHiders(player);

                if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.fly"))
                    this.hub.getServer().getScheduler().runTask(this.hub, () -> player.setAllowFlight(true));

                if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.announce") && !SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasNickname())
                    this.hub.getServer().broadcastMessage(PlayerUtils.getFullyFormattedPlayerName(player) + ChatColor.YELLOW + " a rejoint le hub !");

                this.checkAchievements(player);

                if (player.getUniqueId().equals(UUID.fromString("568046c8-6045-4c59-a255-28027aac8c33")))
                    ActionBarAPI.sendMessage(player, ChatColor.RED + "\u2764");
            });

            this.hub.getScheduledExecutorService().schedule(() ->
            {
                if (!player.isOnline())
                    return;

                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HAT, 1.0F, 0.8F);
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                FancyMessage toolBox = new FancyMessage("En cliquant ici, vous rejoignez automatiquement la fil d'attente pour gagner du temps.").color(ChatColor.GOLD).style(ChatColor.ITALIC);
                new FancyMessage("Hey ! Venez tester notre nouveau jeu Run4Flag !").color(ChatColor.GOLD).send(player);
                new FancyMessage("[Cliquez ici] ").color(ChatColor.GREEN).style(ChatColor.BOLD).command("/join ultraflagkeeper ultraflagkeeper_t2").formattedTooltip(toolBox).send(player);

                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            }, 10, TimeUnit.SECONDS);
        });
    }

    @Override
    public void onLogout(Player player)
    {
        this.log(Level.INFO, "Handling logout from '" + player.getUniqueId() + "'...");

        this.hub.getScheduledExecutorService().execute(() ->
        {
            if (this.selections.containsKey(player.getUniqueId()))
                this.selections.remove(player.getUniqueId());
        });
    }

    public void addHider(Player hider)
    {
        this.hiders.add(hider.getUniqueId());

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
                this.hub.getServer().getOnlinePlayers().stream().filter(player -> !SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.announce") && !SamaGamesAPI.get().getFriendsManager().areFriends(hider.getUniqueId(), player.getUniqueId())).forEach(player ->
                        this.hub.getServer().getScheduler().runTask(this.hub, () -> hider.hidePlayer(player))));
    }

    public void removeHider(Player player)
    {
        if (this.hiders.contains(player.getUniqueId()))
            this.hiders.remove(player.getUniqueId());

        this.hub.getServer().getScheduler().runTask(this.hub, () -> this.hub.getServer().getOnlinePlayers().forEach(player::showPlayer));
    }

    private void updateHiders(Player newConnected)
    {
        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            List<UUID> hidersUUIDList = new ArrayList<>();
            hidersUUIDList.addAll(this.hiders);

            for (UUID hiderUUID : hidersUUIDList)
            {
                Player hider = this.hub.getServer().getPlayer(hiderUUID);

                if (hider != null && !hider.equals(newConnected))
                    if (!SamaGamesAPI.get().getPermissionsManager().hasPermission(newConnected, "hub.announce") && !SamaGamesAPI.get().getFriendsManager().areFriends(newConnected.getUniqueId(), hiderUUID))
                        this.hub.getServer().getScheduler().runTask(this.hub, () -> hider.hidePlayer(newConnected));
            }
        });
    }

    public void setSelection(Player player, Location selection)
    {
        this.selections.put(player.getUniqueId(), selection);
    }

    public void setRulesBookTask(Player player, BukkitTask bukkitTask)
    {
        if (this.rulesBookTasks.containsKey(player.getUniqueId()))
        {
            this.rulesBookTasks.get(player.getUniqueId()).cancel();
            this.rulesBookTasks.remove(player.getUniqueId());
        }

        this.rulesBookTasks.put(player.getUniqueId(), bukkitTask);
    }

    public void removeRulesBookTask(Player player)
    {
        if (this.rulesBookTasks.containsKey(player.getUniqueId()))
        {
            this.rulesBookTasks.get(player.getUniqueId()).cancel();
            this.rulesBookTasks.remove(player.getUniqueId());
        }
    }

    public void setBuild(boolean canBuild)
    {
        this.canBuild = canBuild;
    }

    public Location getSpawn()
    {
        return this.spawn;
    }

    public StaticInventory getStaticInventory()
    {
        return this.staticInventory;
    }

    public Location getSelection(Player player)
    {
        return this.selections.containsKey(player.getUniqueId()) ? this.selections.get(player.getUniqueId()) : null;
    }

    public boolean isBusy(Player player)
    {
        if (this.hub.getGuiManager().getPlayerGui(player.getUniqueId()) != null)
            return true;
        else if (this.hub.getParkourManager().getPlayerParkour(player.getUniqueId()) != null)
            return true;
        else if (this.hub.getInteractionManager().isInteracting(player))
            return true;
        else if (this.hub.getCosmeticManager().getGadgetManager().hasGadget(player))
            return true;
        else if (this.hub.getCosmeticManager().getGadgetManager().isInteracting(player))
            return true;

        return false;
    }

    public boolean canBuild()
    {
        return this.canBuild;
    }

    private void checkAchievements(Player player)
    {
        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            SamaGamesAPI.get().getAchievementManager().getAchievementByID(1).unlock(player.getUniqueId());

            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "network.vip"))
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(3).unlock(player.getUniqueId()));

            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "network.vipplus"))
                this.hub.getExecutorMonoThread().submit(() ->
                    SamaGamesAPI.get().getAchievementManager().getAchievementByID(4).unlock(player.getUniqueId()));

            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "network.staff"))
                this.hub.getExecutorMonoThread().submit(() ->
                    SamaGamesAPI.get().getAchievementManager().getAchievementByID(52).unlock(player.getUniqueId()));

            if (TeamSpeakAPI.isLinked(player.getUniqueId()))
                this.hub.getExecutorMonoThread().submit(() ->
                    SamaGamesAPI.get().getAchievementManager().getAchievementByID(18).unlock(player.getUniqueId()));

            // --

            int friendsCount = SamaGamesAPI.get().getFriendsManager().uuidFriendsList(player.getUniqueId()).size();

            if (friendsCount >= 1)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(45).unlock(player.getUniqueId()));

            if (friendsCount >= 10)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 46, 10));

            if (friendsCount >= 50)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 47, 50));

            if (friendsCount >= 75)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 48, 75));

            if (friendsCount >= 100)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 49, 100));

            // --

            long coins = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getCoins();

            if (coins >= 100)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 30, 100));

            if (coins >= 500)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 31, 500));

            if (coins >= 2500)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 32, 2500));

            if (coins >= 10000)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 33, 10000));

            if (coins >= 50000)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 34, 50000));

            // --

            int woots = SamaGamesAPI.get().getStatsManager().getPlayerStats(player.getUniqueId()).getJukeBoxStatistics().getWoots();

            if (woots >= 1)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(40).unlock(player.getUniqueId()));

            if (woots >= 100)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 41, 100));

            if (woots >= 750)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 42, 750));

            if (woots >= 1500)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 43, 1500));

            if (woots >= 5000)
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().incrementAchievement(player.getUniqueId(), 44, 5000));

            // --

            //
            // This stop the current Thread, has to be put at the end!
            //

            if (TeamSpeakAPI.isLinked(player.getUniqueId()))
                this.hub.getExecutorMonoThread().submit(() ->
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(18).unlock(player.getUniqueId()));
        });
    }
}
