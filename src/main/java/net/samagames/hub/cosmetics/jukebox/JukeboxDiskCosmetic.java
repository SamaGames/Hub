package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.Song;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JukeboxDiskCosmetic extends AbstractCosmetic
{
    private final JukeboxAlbum album;
    private final Song song;

    public JukeboxDiskCosmetic(JukeboxAlbum album, String databaseName, ItemStack icon, Song song)
    {
        super("jukebox." + databaseName, song.getTitle(), icon, null);

        System.out.println("DISKOBJECT:" + databaseName + ":" + song.getTitle() + ":" + this);

        this.album = album;
        this.song = song;

        if(album != null)
            album.addDisk(this);
    }

    @Override
    public ItemStack getIcon(Player player)
    {
        ItemStack icon = super.getIcon(player);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(this.album.getName().substring(0, 2) + this.song.getTitle());

        icon.setItemMeta(meta);

        return icon;
    }

    public Song getSong()
    {
        return this.song;
    }
}
