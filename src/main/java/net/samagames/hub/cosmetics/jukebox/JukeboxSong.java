package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import net.samagames.hub.Hub;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

class JukeboxSong
{
    private final Hub hub;
    private final JukeboxDiskCosmetic disk;
    private final Song song;
    private final SongPlayer player;
    private final String playedBy;
    private final List<String> wooters;
    private final List<String> mehers;
    private final int initialSeconds;
    private int secondsRemaining;

    JukeboxSong(Hub hub, JukeboxDiskCosmetic disk, String playerBy)
    {
        this.hub = hub;
        this.disk = disk;
        this.song = disk.getSong();
        this.playedBy = playerBy;
        this.initialSeconds = disk.getSeconds();
        this.secondsRemaining = disk.getSeconds();

        this.player = new RadioSongPlayer(disk.getSong());
        this.wooters = new ArrayList<>();
        this.mehers = new ArrayList<>();
    }

    public void decrese()
    {
        this.secondsRemaining--;
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

            if (this.getMehs() > this.hub.getServer().getOnlinePlayers().size() / 3.0)
                this.hub.getCosmeticManager().getJukeboxManager().skipSong(true);

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

    public int getWoots()
    {
        return this.wooters.size();
    }

    public int getMehs()
    {
        return this.mehers.size();
    }

    public double getFormattedSecondsRemaining()
    {
        return this.initialSeconds == -1 ? 100.0D : this.secondsRemaining * 100.0D / this.initialSeconds;
    }

    public List<String> getWooters()
    {
        return this.wooters;
    }

    public List<String> getMehers()
    {
        return this.mehers;
    }
}
