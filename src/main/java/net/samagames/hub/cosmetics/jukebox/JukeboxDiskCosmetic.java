package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.Song;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

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
public class JukeboxDiskCosmetic extends AbstractCosmetic
{
    private final Song song;
    private final int seconds;

    public JukeboxDiskCosmetic(Hub hub, int storageId, Song song, int seconds) throws Exception
    {
        super(hub, "Disque", storageId);

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
}
