package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.Song;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.inventory.ItemStack;

public class JukeboxDiskCosmetic extends AbstractCosmetic
{
    private final Song song;

    public JukeboxDiskCosmetic(String databaseName, ItemStack icon, Song song)
    {
        super("jukebox." + databaseName, song.getTitle(), icon, null);

        this.song = song;
    }

    public Song getSong()
    {
        return this.song;
    }
}
