package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class JukeboxRegistry extends AbstractCosmeticRegistry<JukeboxDiskCosmetic>
{
    private HashMap<String, JukeboxAlbum> albumsTemp;

    public JukeboxRegistry()
    {
        this.albumsTemp = new HashMap<>();
    }

    @Override
    public void register()
    {
        // --

        JukeboxAlbum streetFighterAlbum = new JukeboxAlbum("streetfighter", ChatColor.GOLD + "Street Fighter", new ItemStack(Material.STAINED_GLASS, 1, (short) 4));

        JukeboxDiskCosmetic guilesThemeDisk = new JukeboxDiskCosmetic(streetFighterAlbum, "guilestheme", streetFighterAlbum.getSimpleIcon(), NBSDecoder.parse("GuilesTheme"));
        guilesThemeDisk.buyableWithStars(500);

        JukeboxDiskCosmetic ryusTheme = new JukeboxDiskCosmetic(streetFighterAlbum, "ryustheme", streetFighterAlbum.getSimpleIcon(), NBSDecoder.parse("RyusTheme"));
        ryusTheme.buyableWithStars(500);

        JukeboxDiskCosmetic kensTheme = new JukeboxDiskCosmetic(streetFighterAlbum, "kenstheme", streetFighterAlbum.getSimpleIcon(), NBSDecoder.parse("KensTheme"));
        kensTheme.buyableWithStars(500);

        this.registerElement(guilesThemeDisk);
        this.registerElement(ryusTheme);
        this.registerElement(kensTheme);

        this.albumsTemp.put(streetFighterAlbum.getIdentifier(), streetFighterAlbum);

        // --
    }

    public HashMap<String, JukeboxAlbum> getAlbums()
    {
        return this.albumsTemp;
    }
}
