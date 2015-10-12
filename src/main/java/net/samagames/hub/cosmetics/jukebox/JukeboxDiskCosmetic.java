package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.Song;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JukeboxDiskCosmetic extends AbstractCosmetic
{
    private final ChatColor color;
    private final JukeboxAlbum album;
    private final Song song;
    private final short data;

    public JukeboxDiskCosmetic(JukeboxAlbum album, String key, ChatColor color, ItemStack icon, Song song)
    {
        super("jukebox", key, song.getTitle(), icon, null);

        this.color = color;
        this.album = album;
        this.song = song;
        this.data = icon.getDurability();

        if(album != null)
            album.addDisk(this);
    }

    @Override
    public ItemStack getIcon(Player player)
    {
        ItemStack icon = super.getIcon(player);
        icon.setDurability(this.data);

        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(this.color + this.song.getTitle());

        icon.setItemMeta(meta);

        return icon;
    }

    public Song getSong()
    {
        return this.song;
    }

    @Override
    public BaseComponent getBuyResponse()
    {
        TextComponent txt = new TextComponent("Disque acheté ! Re-cliquez pour l'ajouter à la liste d'attente.");
        txt.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        return txt;
    }
}
