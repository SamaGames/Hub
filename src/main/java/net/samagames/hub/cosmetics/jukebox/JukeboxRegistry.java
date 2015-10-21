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

        JukeboxDiskCosmetic guilesThemeDisk = new JukeboxDiskCosmetic(streetFighterAlbum, "guilestheme", ChatColor.GOLD, streetFighterAlbum.getSimpleIcon(), NBSDecoder.parse("GuilesTheme"));
        guilesThemeDisk.buyableWithStars(500);

        JukeboxDiskCosmetic ryusThemeDisk = new JukeboxDiskCosmetic(streetFighterAlbum, "ryustheme", ChatColor.GOLD, streetFighterAlbum.getSimpleIcon(), NBSDecoder.parse("RyusTheme"));
        ryusThemeDisk.buyableWithStars(500);

        JukeboxDiskCosmetic kensThemeDisk = new JukeboxDiskCosmetic(streetFighterAlbum, "kenstheme", ChatColor.GOLD, streetFighterAlbum.getSimpleIcon(), NBSDecoder.parse("KensTheme"));
        kensThemeDisk.buyableWithStars(500);

        this.registerElement(guilesThemeDisk);
        this.registerElement(ryusThemeDisk);
        this.registerElement(kensThemeDisk);

        this.albumsTemp.put(streetFighterAlbum.getIdentifier(), streetFighterAlbum);

        // --

        JukeboxAlbum zeldaAlbum = new JukeboxAlbum("zelda", ChatColor.GREEN + "Zelda", new ItemStack(Material.STAINED_GLASS, 1, (short) 13));

        JukeboxDiskCosmetic gerudoValleyDisk = new JukeboxDiskCosmetic(zeldaAlbum, "gerudovalley", ChatColor.GREEN, zeldaAlbum.getSimpleIcon(), NBSDecoder.parse("GerudoValley"));
        gerudoValleyDisk.buyableWithStars(500);

        JukeboxDiskCosmetic windmillDisk = new JukeboxDiskCosmetic(zeldaAlbum, "windmill", ChatColor.GREEN, zeldaAlbum.getSimpleIcon(), NBSDecoder.parse("Windmill"));
        windmillDisk.buyableWithStars(500);

        JukeboxDiskCosmetic lostWoodsDisk = new JukeboxDiskCosmetic(zeldaAlbum, "lostwoods", ChatColor.GREEN, zeldaAlbum.getSimpleIcon(), NBSDecoder.parse("LostWoods"));
        lostWoodsDisk.buyableWithStars(500);

        this.registerElement(gerudoValleyDisk);
        this.registerElement(windmillDisk);
        this.registerElement(lostWoodsDisk);

        this.albumsTemp.put(zeldaAlbum.getIdentifier(), zeldaAlbum);

        // --

        JukeboxAlbum pokemonAlbum = new JukeboxAlbum("pokemon", ChatColor.BLUE + "Pokémon", new ItemStack(Material.STAINED_GLASS, 1, (short) 11));

        JukeboxDiskCosmetic routeOneDisk = new JukeboxDiskCosmetic(pokemonAlbum, "routeone", ChatColor.BLUE, pokemonAlbum.getSimpleIcon(), NBSDecoder.parse("RouteOne"));
        routeOneDisk.buyableWithStars(500);

        JukeboxDiskCosmetic centerTheme = new JukeboxDiskCosmetic(pokemonAlbum, "centertheme", ChatColor.BLUE, pokemonAlbum.getSimpleIcon(), NBSDecoder.parse("PokemonCenterTheme"));
        centerTheme.buyableWithStars(500);

        JukeboxDiskCosmetic gymBattle = new JukeboxDiskCosmetic(pokemonAlbum, "gymbattle", ChatColor.BLUE, pokemonAlbum.getSimpleIcon(), NBSDecoder.parse("GymBattle"));
        gymBattle.buyableWithStars(500);

        this.registerElement(routeOneDisk);
        this.registerElement(centerTheme);
        this.registerElement(gymBattle);

        this.albumsTemp.put(pokemonAlbum.getIdentifier(), pokemonAlbum);

        // --
    }

    public HashMap<String, JukeboxAlbum> getAlbums()
    {
        return this.albumsTemp;
    }
}
