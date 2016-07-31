package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.settings.IPlayerSettings;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.tools.Misc;
import net.samagames.tools.ParticleEffect;
import net.samagames.tools.bossbar.BossBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.lang.model.type.NullType;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class JukeboxManager extends AbstractCosmeticManager<JukeboxDiskCosmetic>
{
    public static final String JUKEBOX_TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Jukebox" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;
    private static final ParticleEffect WOOT_EFFECT = ParticleEffect.NOTE;
    private static final ParticleEffect MEH_EFFECT = ParticleEffect.VILLAGER_ANGRY;

    private final LinkedList<JukeboxSong> playlists;
    private final LinkedList<JukeboxSong> recentsPlaylists;
    private final Map<UUID, Integer> recentDJs;
    private final List<UUID> mutedPlayers;
    private JukeboxSong currentPlaylist;
    private BukkitTask barTask;
    private boolean lockFlag;
    private boolean isLocked;

    public JukeboxManager(Hub hub)
    {
        super(hub, new JukeboxRegistry(hub));

        this.playlists = new LinkedList<>();
        this.recentsPlaylists = new LinkedList<>();
        this.recentDJs = new HashMap<>();
        this.mutedPlayers = new ArrayList<>();
        this.lockFlag = false;

        hub.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            if ((this.currentPlaylist == null || !this.currentPlaylist.getPlayer().isPlaying()) && !this.lockFlag)
                this.nextSong();
            else if (this.currentPlaylist != null && this.currentPlaylist.getPlayer().isPlaying())
                this.currentPlaylist.decrese();
        }, 1, 1, TimeUnit.SECONDS);

        hub.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            if (this.currentPlaylist != null)
            {
                for (UUID wooter : this.currentPlaylist.getWooters())
                {
                    Player player = this.hub.getServer().getPlayer(wooter);

                    if (player != null)
                    {
                        Location playerLocation = player.getLocation().clone();
                        playerLocation.setY(playerLocation.getY() + 2);
                        WOOT_EFFECT.display(new ParticleEffect.NoteColor(new Random().nextInt(10)), playerLocation, 160.0);
                    }
                }

                for (UUID meher : this.currentPlaylist.getMehers())
                {
                    Player player = this.hub.getServer().getPlayer(meher);

                    if (player != null)
                    {
                        Location playerLocation = player.getLocation().clone();
                        playerLocation.setY(playerLocation.getY() + 2);
                        MEH_EFFECT.display(0F, 1F, 0F, 1, 1, playerLocation, 160.0);
                    }
                }
            }
        }, 300, 300, TimeUnit.MILLISECONDS);
    }

    @Override
    public void enableCosmetic(Player player, JukeboxDiskCosmetic cosmetic)
    {
        if (cosmetic.isOwned(player))
        {
            if (cosmetic.getAccessibility().canAccess(player))
            {
                this.play(cosmetic, player);

                AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

                if (gui != null)
                    gui.update(player);
            }
            else
            {
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous n'avez pas le grade nécessaire pour utiliser cette cosmétique.");
            }
        }
        else
        {
            cosmetic.buy(player, false);
        }
    }

    @Override
    public void enableCosmetic(Player player, JukeboxDiskCosmetic cosmetic, NullType useless) {}

    @Override
    public void disableCosmetic(Player player, boolean logout, NullType useless) { /** Not needed **/ }

    @Override
    public void restoreCosmetic(Player player) { /** Not needed **/ }

    @Override
    public void update() { /** Not needed **/ }

    public void onLogin(Player player)
    {
        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            IPlayerSettings settings = SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId());

            if(!settings.isJukeboxListen())
                this.mutedPlayers.add(player.getUniqueId());
            else
                addPlayer(player);
        });
    }

    public void onLogout(Player player)
    {
        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.removePlayer(player);
            this.mutedPlayers.remove(player.getUniqueId());
        });

    }

    public void play(JukeboxDiskCosmetic disk, Player player)
    {
        Song song = disk.getSong();
        JukeboxSong jukeboxSong = new JukeboxSong(this.hub, disk, player.getName());
        boolean canBypassLimit = SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.jukebox.limitbypass");

        if (!canBypassLimit)
        {
            if ((this.containsSongFrom(player.getName()) || (this.currentPlaylist != null && this.currentPlaylist.getPlayedBy().equals(player.getName()))))
            {
                player.sendMessage(ChatColor.RED + "La playlist contient déjà une musique de vous.");
                return;
            }
            else if (this.recentDJs.containsKey(player.getUniqueId()))
            {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez proposer un titre que dans " + Misc.formatTime(this.recentDJs.get(player.getUniqueId())) + ".");
                return;
            }
            else if ((containsSong(song.getTitle()) || (this.currentPlaylist != null && this.currentPlaylist.getSong().getTitle().equals(song.getTitle()))))
            {
                player.sendMessage(ChatColor.RED + "Cette musique est déjà dans la liste d'attente.");
                return;
            }
            else if ((wasRecentlyPlayed(song.getTitle())))
            {
                player.sendMessage(ChatColor.RED + "Cette musique a déjà été jouée récement. Merci d'attendre avant de l'ajouter à nouveau.");
                return;
            }
            else if (this.isLocked)
            {
                player.sendMessage(ChatColor.RED + "La playlist est actuellement vérrouillée.");
                return;
            }
        }

        boolean hasLimitStaff = SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.jukebox.limitstaff");

        if (hasLimitStaff)
            this.recentDJs.put(player.getUniqueId(), 300);
        else
            this.recentDJs.put(player.getUniqueId(), 600);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (recentDJs.containsKey(player.getUniqueId()))
                {
                    int now = recentDJs.get(player.getUniqueId()) - 1;

                    if (now == 0)
                    {
                        recentDJs.remove(player.getUniqueId());
                        this.cancel();
                    }
                    else
                    {
                        recentDJs.put(player.getUniqueId(), now);
                    }
                }
            }
        }.runTaskTimerAsynchronously(this.hub, 20L, 20L);

        this.playlists.addLast(jukeboxSong);

        player.sendMessage(JUKEBOX_TAG + ChatColor.GREEN + "Votre musique sera jouée prochainement. Position dans la playlist : " + ChatColor.AQUA + "#" + this.playlists.size());
    }

    public boolean nextSong()
    {
        this.lockFlag = true;

        try
        {
            if (this.currentPlaylist != null)
            {
                this.recentsPlaylists.addLast(this.currentPlaylist);

                if (this.recentsPlaylists.size() > 3)
                    this.recentsPlaylists.removeFirst();

                int woots = this.currentPlaylist.getWoots();
                int mehs = this.currentPlaylist.getMehs();

                this.hub.getServer().broadcastMessage(JUKEBOX_TAG + ChatColor.GOLD + this.currentPlaylist.getPlayedBy() + ChatColor.YELLOW + " a reçu " + ChatColor.GREEN + woots + " Woot" + ChatColor.YELLOW + " et " + ChatColor.RED + mehs + " Meh" + ChatColor.YELLOW + ".");

                UUID playerUUID = SamaGamesAPI.get().getUUIDTranslator().getUUID(this.currentPlaylist.getPlayedBy());

                if (playerUUID != null)
                {
                    try
                    {
                        SamaGamesAPI.get().getStatsManager().getPlayerStats(playerUUID).getJukeBoxStatistics().incrByWoots(woots);
                        SamaGamesAPI.get().getStatsManager().getPlayerStats(playerUUID).getJukeBoxStatistics().incrByMehs(mehs);
                    }
                    catch (NullPointerException ignored) {}
                }

                for (UUID wooter : this.currentPlaylist.getWooters())
                {
                    try
                    {
                        SamaGamesAPI.get().getStatsManager().getPlayerStats(wooter).getJukeBoxStatistics().incrByWootsGiven(1);
                    }
                    catch (NullPointerException ignored) {}
                }

                this.currentPlaylist = null;

                if(this.barTask != null)
                {
                    this.barTask.cancel();
                    this.barTask = null;

                    BossBarAPI.flushBars();
                }

                if (this.hub.getServer().getPlayer(playerUUID) != null)
                    this.hub.getCosmeticManager().getJukeboxManager().disableCosmetic(this.hub.getServer().getPlayer(playerUUID), false);
            }

            if (this.playlists.isEmpty())
                return false;

            JukeboxSong nextSong = this.playlists.pollFirst();

            if (nextSong != null)
            {
                SongPlayer player = nextSong.getPlayer();
                player.setAutoDestroy(true);
                player.setPlaying(true);

                this.currentPlaylist = nextSong;

                this.hub.getServer().getOnlinePlayers().stream().filter(p -> !this.mutedPlayers.contains(p.getUniqueId())).forEach(p -> this.currentPlaylist.getPlayer().addPlayer(p));

                this.hub.getServer().broadcastMessage(JUKEBOX_TAG + ChatColor.GOLD + this.currentPlaylist.getPlayedBy() + ChatColor.YELLOW + " joue " + ChatColor.GOLD + ChatColor.ITALIC + this.currentPlaylist.getSong().getTitle() + ChatColor.YELLOW + " de " + ChatColor.GOLD + this.currentPlaylist.getSong().getAuthor());
                this.hub.getServer().broadcastMessage(JUKEBOX_TAG + ChatColor.GRAY + ChatColor.ITALIC + "Tapez " + ChatColor.GREEN + "/woot" + ChatColor.GRAY + ChatColor.ITALIC + " pour apprécier ou " + ChatColor.RED + "/meh" + ChatColor.GRAY + ChatColor.ITALIC + " pour indiquer que vous n'aimez pas la musique jouée actuellement et la couper.");

                if(this.barTask == null)
                {
                    this.barTask = this.hub.getServer().getScheduler().runTaskTimerAsynchronously(this.hub, new Runnable()
                    {
                        private ChatColor[] colors = {ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.DARK_AQUA};
                        private int i = 0;

                        @Override
                        public void run()
                        {
                            ChatColor randomizedColor = this.colors[new Random().nextInt(this.colors.length)];

                            hub.getServer().getOnlinePlayers().stream().filter(barPlayer -> !mutedPlayers.contains(barPlayer.getUniqueId())).forEach(barPlayer ->
                            {
                                BossBarAPI.removeBar(barPlayer);

                                if (this.i <= 4)
                                    BossBarAPI.getBar(randomizedColor + "♫" + ChatColor.YELLOW + " " + ChatColor.GOLD + currentPlaylist.getSong().getTitle() + ChatColor.YELLOW + " jouée par " + ChatColor.GOLD + currentPlaylist.getPlayedBy() + " " + randomizedColor + "♪", BarColor.YELLOW, BarStyle.SOLID, currentPlaylist.getFormattedSecondsRemaining()).getValue().addPlayer(barPlayer);
                                else
                                    BossBarAPI.getBar(randomizedColor + "♫" + ChatColor.GREEN + " " + currentPlaylist.getWoots() + " Woot" + (currentPlaylist.getWoots() > 1 ? "s" : "") + ChatColor.YELLOW + " et " + ChatColor.RED + currentPlaylist.getMehs() + " Meh" + (currentPlaylist.getMehs() > 1 ? "s" : "") + " " + randomizedColor + "♪", BarColor.YELLOW, BarStyle.SOLID, currentPlaylist.getFormattedSecondsRemaining()).getValue().addPlayer(barPlayer);
                            });

                            this.i++;

                            if(this.i == 10)
                                this.i = 0;
                        }
                    }, 20L, 20L);
                }

                this.lockFlag = false;

                return true;
            }

            this.lockFlag = false;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.nextSong();
        }
        finally
        {
            this.lockFlag = false;
        }

        return false;
    }

    public String skipSong(boolean becauseMehs)
    {
        if (this.currentPlaylist != null && this.currentPlaylist.getPlayer().isPlaying())
        {
            if(becauseMehs)
                this.hub.getServer().broadcastMessage(JUKEBOX_TAG + ChatColor.RED + "La musique est passée car elle a reçu plus de 33% de votes négatifs.");

            this.currentPlaylist.getPlayer().setPlaying(false);

            if (this.nextSong())
                return JUKEBOX_TAG + ChatColor.GREEN + "Musique suivante jouée.";
            else
                return JUKEBOX_TAG + ChatColor.GOLD + "La musique a été arrêtée mais la playlist est vide.";
        }
        else
        {
            return JUKEBOX_TAG + ChatColor.RED + "Aucun son n'est lu actuellement.";
        }
    }

    public void clear()
    {
        this.playlists.clear();
    }

    public void addPlayer(Player player)
    {
        if (this.currentPlaylist != null)
            this.currentPlaylist.getPlayer().addPlayer(player);
    }

    public void removePlayer(Player player)
    {
        if (this.currentPlaylist != null)
            this.currentPlaylist.getPlayer().removePlayer(player);
    }

    public void mute(Player player, boolean flag)
    {
        if(flag)
        {
            this.mutedPlayers.add(player.getUniqueId());
            this.removePlayer(player);
        }
        else
        {
            this.mutedPlayers.remove(player.getUniqueId());
            this.addPlayer(player);
        }
    }

    public void toggleLock()
    {
        this.isLocked = !this.isLocked;
    }

    public JukeboxSong getCurrentSong()
    {
        return this.currentPlaylist;
    }

    public LinkedList<JukeboxSong> getPlaylists()
    {
        return this.playlists;
    }

    public boolean containsSongFrom(String playedBy)
    {
        for (JukeboxSong song : this.playlists)
            if (song.getPlayedBy().equals(playedBy))
                return true;

        return false;
    }

    public boolean containsSong(String title)
    {
        for (JukeboxSong song : this.playlists)
            if (song.getSong().getTitle().equals(title))
                return true;

        return false;
    }

    public boolean wasRecentlyPlayed(String title)
    {
        for (JukeboxSong song : this.recentsPlaylists)
            if (song.getSong().getTitle().equals(title))
                return true;

        return false;
    }

    public boolean isLocked()
    {
        return this.isLocked;
    }
}
