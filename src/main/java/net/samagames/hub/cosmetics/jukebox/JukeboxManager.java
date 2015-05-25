package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class JukeboxManager extends AbstractCosmeticManager<JukeboxDiskCosmetic>
{
    private final String jukeboxTag;
    private final ParticleEffect wootEffect;
    private final ParticleEffect mehEffect;
    private final LinkedList<JukeboxPlaylist> playlists;
    private final LinkedList<JukeboxPlaylist> recentsPlaylists;
    private final HashMap<UUID, Integer> recentDJs;
    private final ArrayList<UUID> mutedPlayers;

    private JukeboxPlaylist currentPlaylist;
    private boolean isLocked;

    public JukeboxManager(Hub hub)
    {
        super(hub, new JukeboxRegistry());

        new NoteBlockPlayerMain();
        this.jukeboxTag = ChatColor.AQUA + "[" + ChatColor.DARK_AQUA + "Jukebox" + ChatColor.AQUA + "] ";
        this.wootEffect = ParticleEffect.NOTE;
        this.mehEffect = ParticleEffect.VILLAGER_ANGRY;

        this.playlists = new LinkedList<>();
        this.recentsPlaylists = new LinkedList<>();
        this.recentDJs = new HashMap<>();
        this.mutedPlayers = new ArrayList<>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this.hub, () ->
        {
            if (this.currentPlaylist == null || !this.currentPlaylist.getPlayer().isPlaying())
                nextSong();
        }, 20, 20);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this.hub, () ->
        {
            if (this.currentPlaylist != null)
            {
                for (String wooter : this.currentPlaylist.getWooters())
                {
                    Player player = Bukkit.getPlayerExact(wooter);

                    if (player != null)
                    {
                        Location playerLocation = player.getLocation().clone();
                        playerLocation.setY(playerLocation.getY() + 2);
                        this.wootEffect.display(new ParticleEffect.NoteColor(new Random().nextInt(10)), playerLocation, 160.0);
                    }
                }

                for (String meher : this.currentPlaylist.getMehers())
                {
                    Player player = Bukkit.getPlayerExact(meher);

                    if (player != null)
                    {
                        Location playerLocation = player.getLocation().clone();
                        playerLocation.setY(playerLocation.getY() + 2);
                        this.mehEffect.display(0F, 1F, 0F, 1, 1, playerLocation, 160.0);
                    }
                }
            }
        }, 6, 6);
    }

    @Override
    public void enableCosmetic(Player player, JukeboxDiskCosmetic cosmetic)
    {
        if (cosmetic.isOwned(player))
            this.play(cosmetic, player);
        else
            cosmetic.buy(player);
    }

    @Override
    public void disableCosmetic(Player player, boolean logout) {}

    @Override
    public void restoreCosmetic(Player player) {}

    @Override
    public void update() {}

    public void play(JukeboxDiskCosmetic disk, Player playedBy)
    {
        Song song = disk.getSong();
        JukeboxPlaylist jukeboxPlaylist = new JukeboxPlaylist(disk, playedBy.getName());
        boolean canBypassLimit = SamaGamesAPI.get().getPermissionsManager().hasPermission(playedBy, "hub.jukebox.limitbypass");

        if ((this.containsSongFrom(playedBy.getName()) || (this.currentPlaylist != null && this.currentPlaylist.getPlayedBy().equals(playedBy.getName()))) && !canBypassLimit)
        {
            playedBy.sendMessage(ChatColor.RED + "La playlist contient déjà une musique de vous.");
            return;
        }
        else if (this.recentDJs.containsKey(playedBy.getUniqueId()) && !canBypassLimit)
        {
            playedBy.sendMessage(ChatColor.RED + "Vous ne pouvez proposer un titre que dans " + this.recentDJs.get(playedBy.getUniqueId()) + " secondes.");
            return;
        }
        else if ((containsSong(song.getTitle()) || (this.currentPlaylist != null && this.currentPlaylist.getSong().getTitle().equals(song.getTitle()))) && !canBypassLimit)
        {
            playedBy.sendMessage(ChatColor.RED + "Cette musique est déjà dans la liste d'attente.");
            return;
        }
        else if ((wasRecentlyPlayed(song.getTitle())) && !canBypassLimit)
        {
            playedBy.sendMessage(ChatColor.RED + "Cette musique a déjà été jouée récement. Merci d'attendre avant de l'ajouter à nouveau.");
            return;
        }
        else if (this.isLocked && !canBypassLimit)
        {
            playedBy.sendMessage(ChatColor.RED + "La playlist est actuellement vérouillée.");
            return;
        }

        this.recentDJs.put(playedBy.getUniqueId(), 600);

        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.hub, () ->
        {
            if (this.recentDJs.containsKey(playedBy.getUniqueId())) {
                int now = this.recentDJs.get(playedBy.getUniqueId()) - 1;
                this.recentDJs.put(playedBy.getUniqueId(), now);
            }
        }, 20L, 20L);

        Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () ->
        {
            this.recentDJs.remove(playedBy.getUniqueId());
            task.cancel();
        }, (20L * 60 * 10) + 5);

        this.playlists.addLast(jukeboxPlaylist);

        playedBy.sendMessage(ChatColor.GREEN + "Votre musique sera jouée prochainement. Position dans la playlist : " + ChatColor.AQUA + "#" + this.playlists.size());
    }

    public boolean nextSong()
    {
        if (this.currentPlaylist != null)
        {
            this.recentsPlaylists.addLast(this.currentPlaylist);

            if (this.recentsPlaylists.size() > 3)
                this.recentsPlaylists.removeFirst();

            int woots = this.currentPlaylist.getWoots();
            int mehs = this.currentPlaylist.getMehs();
            Bukkit.broadcastMessage(this.jukeboxTag + ChatColor.GOLD + this.currentPlaylist.getPlayedBy() + ChatColor.YELLOW + " a reçu " + ChatColor.GREEN + woots + " Woot" + ChatColor.YELLOW + " et " + ChatColor.RED + mehs + " Meh" + ChatColor.YELLOW + ".");

            Player player = Bukkit.getPlayerExact(this.currentPlaylist.getPlayedBy());

            if (player != null)
            {
                SamaGamesAPI.get().getStatsManager("hub").increase(player.getUniqueId(), "woots", woots);
                SamaGamesAPI.get().getStatsManager("hub").increase(player.getUniqueId(), "mehs", mehs);
            }

            this.currentPlaylist = null;
        }

        if (this.playlists.size() == 0)
            return false;

        JukeboxPlaylist nextSong = this.playlists.pollFirst();

        if (nextSong != null)
        {
            SongPlayer player = nextSong.getPlayer();
            Bukkit.getOnlinePlayers().stream().filter(p -> !this.mutedPlayers.contains(p.getUniqueId())).forEach(p -> player.addPlayer(p.getUniqueId()));
            //Bukkit.getOnlinePlayers().stream().filter(p -> !this.mutedPlayers.contains(p.getUniqueId())).forEach(player::addPlayer);

            player.setPlaying(true);

            this.currentPlaylist = nextSong;
            this.currentPlaylist.getPlayer().setPlaying(true);

            Bukkit.broadcastMessage(this.jukeboxTag + ChatColor.GOLD + this.currentPlaylist.getPlayedBy() + ChatColor.YELLOW + " joue " + ChatColor.GOLD + ChatColor.ITALIC + this.currentPlaylist.getSong().getTitle() + ChatColor.YELLOW + " de " + ChatColor.GOLD + this.currentPlaylist.getSong().getAuthor());
            Bukkit.broadcastMessage(this.jukeboxTag + ChatColor.GRAY + ChatColor.ITALIC + "Tapez " + ChatColor.GREEN + "/woot" + ChatColor.GRAY + ChatColor.ITALIC + " pour apprécier ou " + ChatColor.RED + "/meh" + ChatColor.GRAY + ChatColor.ITALIC + " pour indiquer que vous n'aimez pas la musique jouée actuellement et la couper.");

            return true;
        }

        return false;
    }

    public String skipSong(boolean becauseMehs)
    {
        if (this.currentPlaylist != null && this.currentPlaylist.getPlayer().isPlaying())
        {
            if(becauseMehs)
                Bukkit.broadcastMessage(this.jukeboxTag + ChatColor.RED + "La musique est passée car elle a reçu plus de 33% de votes négatifs.");

            this.currentPlaylist.getPlayer().setPlaying(false);

            if (this.nextSong())
                return this.jukeboxTag + ChatColor.GREEN + "Musique suivante jouée.";
            else
                return this.jukeboxTag + ChatColor.GOLD + "La musique a été arrêtée mais la playlist est vide.";
        }
        else
        {
            return this.jukeboxTag + ChatColor.RED + "Aucun son n'est lu actuellement.";
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

    public JukeboxPlaylist getCurrentSong()
    {
        return this.currentPlaylist;
    }

    public LinkedList<JukeboxPlaylist> getPlaylists()
    {
        return this.playlists;
    }

    public String getJukeboxTag()
    {
        return this.jukeboxTag;
    }

    public boolean containsSongFrom(String playedBy)
    {
        for (JukeboxPlaylist song : this.playlists)
            if (song.getPlayedBy().equals(playedBy))
                return true;

        return false;
    }

    public boolean containsSong(String title)
    {
        for (JukeboxPlaylist song : this.playlists)
            if (song.getSong().getTitle().equals(title))
                return true;

        return false;
    }

    public boolean wasRecentlyPlayed(String title)
    {
        for (JukeboxPlaylist song : this.recentsPlaylists)
            if (song.getSong().getTitle().equals(title))
                return true;

        return false;
    }

    public boolean isLocked()
    {
        return this.isLocked;
    }

    @Override
    public String getName() { return "JukeboxManager"; }
}
