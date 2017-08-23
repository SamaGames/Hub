package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import net.samagames.hub.Hub;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
public class JukeboxSong
{
    private final Hub hub;
    private final JukeboxDiskCosmetic disk;
    private final Song song;
    private final SongPlayer player;
    private final String playedBy;
    private final List<UUID> wooters;
    private final List<UUID> mehers;
    private final int initialSeconds;
    private int secondsRemaining;

    JukeboxSong(Hub hub, JukeboxDiskCosmetic disk, String playedBy)
    {
        this.hub = hub;
        this.disk = disk;
        this.song = disk.getSong();
        this.playedBy = playedBy;
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
        if (this.playedBy.equals(player.getName()))
            return false;

        if (this.mehers.contains(player.getUniqueId()))
        {
            this.mehers.remove(player.getUniqueId());
            this.player.addPlayer(player);
        }

        if (!this.wooters.contains(player.getUniqueId()))
        {
            this.wooters.add(player.getUniqueId());
            return true;
        }

        return false;
    }

    public boolean meh(Player player)
    {
        if (this.playedBy.equals(player.getName()))
            return false;

        if (this.wooters.contains(player.getUniqueId()))
            this.wooters.remove(player.getUniqueId());

        if (!this.mehers.contains(player.getUniqueId()))
        {
            this.mehers.add(player.getUniqueId());
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

    public List<UUID> getWooters()
    {
        return this.wooters;
    }

    public List<UUID> getMehers()
    {
        return this.mehers;
    }
}
