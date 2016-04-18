package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.Song;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

class JukeboxDiskCosmetic extends AbstractCosmetic
{
    private static final Material[] RECORDS = {
            Material.GOLD_RECORD, Material.GREEN_RECORD, Material.RECORD_3, Material.RECORD_4,
            Material.RECORD_5, Material.RECORD_6, Material.RECORD_7, Material.RECORD_8,
            Material.RECORD_9, Material.RECORD_10, Material.RECORD_11, Material.RECORD_12
    };

    private final Song song;
    private final int seconds;

    JukeboxDiskCosmetic(Hub hub, String key, int stars, CosmeticRarity rarity, Song song, int seconds)
    {
        super(hub, "jukebox", key, song.getTitle(), getRandomDisk(), stars, rarity, CosmeticAccessibility.VIP, null);

        this.song = song;
        this.seconds = seconds;
    }

    public Song getSong()
    {
        return this.song;
    }

    public int getSeconds()
    {
        return this.seconds;
    }

    private static ItemStack getRandomDisk()
    {
        return new ItemStack(RECORDS[new Random().nextInt(RECORDS.length)], 1);
    }
}
