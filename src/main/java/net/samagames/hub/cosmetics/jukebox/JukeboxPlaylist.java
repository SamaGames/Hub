package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class JukeboxPlaylist
{
    private final JukeboxDiskCosmetic disk;
    private final Song song;
    private final SongPlayer player;
    private final String playedBy;
    private final ArrayList<String> wooters;
    private final ArrayList<String> mehers;

    public JukeboxPlaylist(JukeboxDiskCosmetic disk, String playerBy)
    {
        this.disk = disk;
        this.song = disk.getSong();
        this.playedBy = playerBy;

        this.player = new RadioSongPlayer(disk.getSong());
        this.wooters = new ArrayList<>();
        this.mehers = new ArrayList<>();
    }

    public boolean woot(Player player)
    {
        if (this.mehers.contains(player.getName()))
        {
            this.mehers.remove(player.getName());
            this.player.addPlayer(player);
        }

        if (!this.wooters.contains(player.getName()))
        {
            this.wooters.add(player.getName());
            return true;
        }

        return false;
    }

    public boolean meh(Player player)
    {
        if (this.wooters.contains(player.getName()))
            this.wooters.remove(player.getName());

        if (!this.mehers.contains(player.getName()))
        {
            this.mehers.add(player.getName());
            this.player.removePlayer(player);

            if (this.getMehs() > Bukkit.getOnlinePlayers().size() / 3.0)
                Hub.getInstance().getCosmeticManager().getJukeboxManager().skipSong(true);

            return true;
        }

        return false;
    }

    public JukeboxDiskCosmetic getDisk()
    {
        return this.disk;
    }

    public String getPlayedBy()
    {
        return playedBy;
    }

    public Song getSong()
    {
        return this.song;
    }

    public SongPlayer getPlayer()
    {
        return this.player;
    }

    public int getWoots() {
        return this.wooters.size();
    }

    public int getMehs() {
        return this.mehers.size();
    }

    public ArrayList<String> getWooters()
    {
        return this.wooters;
    }

    public ArrayList<String> getMehers()
    {
        return this.mehers;
    }
}
