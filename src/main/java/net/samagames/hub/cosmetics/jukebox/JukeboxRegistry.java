package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class JukeboxRegistry extends AbstractCosmeticRegistry<JukeboxDiskCosmetic>
{
    @Override
    public void register()
    {
        JukeboxDiskCosmetic whatIsLoveDisk = new JukeboxDiskCosmetic("whatislove", new ItemStack(Material.RECORD_4, 1), NBSDecoder.parse("WhatIsLove"));
        whatIsLoveDisk.buyableWithStars(500);

        JukeboxDiskCosmetic songOfStormsDisk = new JukeboxDiskCosmetic("songofstorm", new ItemStack(Material.RECORD_12, 1), NBSDecoder.parse("SongOfStorms"));
        songOfStormsDisk.buyableWithStars(500);

        JukeboxDiskCosmetic caraMiaDisk = new JukeboxDiskCosmetic("caramia", new ItemStack(Material.GOLD_RECORD, 1), NBSDecoder.parse("CaraMia"));
        caraMiaDisk.buyableWithStars(500);

        JukeboxDiskCosmetic gangnamStyleDisk = new JukeboxDiskCosmetic("gangnamstyle", new ItemStack(Material.RECORD_3, 1), NBSDecoder.parse("GangnamStyle"));
        gangnamStyleDisk.buyableWithStars(500);

        JukeboxDiskCosmetic indianaJonesThemeDisk = new JukeboxDiskCosmetic("indianajones", new ItemStack(Material.RECORD_5, 1), NBSDecoder.parse("IndianaJonesTheme"));
        indianaJonesThemeDisk.buyableWithStars(500);

        JukeboxDiskCosmetic pokemonCenterTheme = new JukeboxDiskCosmetic("pokemoncenter", new ItemStack(Material.RECORD_6, 1), NBSDecoder.parse("PokemonCenterTheme"));
        pokemonCenterTheme.buyableWithStars(500);

        JukeboxDiskCosmetic positiveForceDisk = new JukeboxDiskCosmetic("positiveforce", new ItemStack(Material.RECORD_7, 1), NBSDecoder.parse("PositiveForce"));
        positiveForceDisk.buyableWithStars(500);

        JukeboxDiskCosmetic tetrisAThemeDisk = new JukeboxDiskCosmetic("tetrisa", new ItemStack(Material.RECORD_8, 1), NBSDecoder.parse("TetrisATheme"));
        tetrisAThemeDisk.buyableWithStars(500);

        JukeboxDiskCosmetic djGotUsDisk = new JukeboxDiskCosmetic("djgotus", new ItemStack(Material.GREEN_RECORD, 1), NBSDecoder.parse("DJGotUsFallininLove"));
        djGotUsDisk.buyableWithStars(500);

        JukeboxDiskCosmetic hesAPirateDisk = new JukeboxDiskCosmetic("hesapirate", new ItemStack(Material.RECORD_9, 1), NBSDecoder.parse("HesAPirate"));
        hesAPirateDisk.buyableWithStars(500);

        JukeboxDiskCosmetic darudeSandstormDisk = new JukeboxDiskCosmetic("sandstorm", new ItemStack(Material.SANDSTONE, 1), NBSDecoder.parse("DarudeSandstorm"));
        darudeSandstormDisk.buyableWithStars(500);

        JukeboxDiskCosmetic lostWoodsDisk = new JukeboxDiskCosmetic("lostwoods", new ItemStack(Material.RECORD_11, 1), NBSDecoder.parse("LostWoods"));
        lostWoodsDisk.buyableWithStars(500);

        JukeboxDiskCosmetic hammerToFallDisk = new JukeboxDiskCosmetic("hammertofall", new ItemStack(Material.RECORD_10, 1), NBSDecoder.parse("HammerToFall"));
        hammerToFallDisk.buyableWithStars(500);

        JukeboxDiskCosmetic takeMeOutDisk = new JukeboxDiskCosmetic("takemeout", new ItemStack(Material.RECORD_4, 1), NBSDecoder.parse("TakeMeOut"));
        takeMeOutDisk.buyableWithStars(500);

        this.registerElement(whatIsLoveDisk);
        this.registerElement(songOfStormsDisk);
        this.registerElement(caraMiaDisk);
        this.registerElement(gangnamStyleDisk);
        this.registerElement(indianaJonesThemeDisk);
        this.registerElement(pokemonCenterTheme);
        this.registerElement(positiveForceDisk);
        this.registerElement(tetrisAThemeDisk);
        this.registerElement(djGotUsDisk);
        this.registerElement(hesAPirateDisk);
        this.registerElement(darudeSandstormDisk);
        this.registerElement(lostWoodsDisk);
        this.registerElement(hammerToFallDisk);
        this.registerElement(takeMeOutDisk);
    }
}
