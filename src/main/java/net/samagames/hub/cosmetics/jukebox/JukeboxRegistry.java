package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;

class JukeboxRegistry extends AbstractCosmeticRegistry<JukeboxDiskCosmetic>
{
    JukeboxRegistry(Hub hub)
    {
        super(hub);
    }

    @Override
    public void register() throws Exception
    {
        JukeboxDiskCosmetic guilesThemeDisk = new JukeboxDiskCosmetic(this.hub, 0, NBSDecoder.parse("GuilesTheme"), 141);
        JukeboxDiskCosmetic ryusThemeDisk = new JukeboxDiskCosmetic(this.hub, 1, NBSDecoder.parse("RyusTheme"), 114);
        JukeboxDiskCosmetic kensThemeDisk = new JukeboxDiskCosmetic(this.hub, 2, NBSDecoder.parse("KensTheme"), 147);
        JukeboxDiskCosmetic routeOneDisk = new JukeboxDiskCosmetic(this.hub, 3, NBSDecoder.parse("RouteOne"), 56);
        JukeboxDiskCosmetic centerTheme = new JukeboxDiskCosmetic(this.hub, 4, NBSDecoder.parse("PokemonCenterTheme"), 191);
        JukeboxDiskCosmetic gymBattle = new JukeboxDiskCosmetic(this.hub, 5, NBSDecoder.parse("GymBattle"), 277);
        JukeboxDiskCosmetic gerudoValleyDisk = new JukeboxDiskCosmetic(this.hub, 6, NBSDecoder.parse("GerudoValley"), 153);
        JukeboxDiskCosmetic windmillDisk = new JukeboxDiskCosmetic(this.hub, 7, NBSDecoder.parse("Windmill"), 88);
        JukeboxDiskCosmetic lostWoodsDisk = new JukeboxDiskCosmetic(this.hub, 8, NBSDecoder.parse("LostWoods"), 58);

        this.registerElement(guilesThemeDisk);
        this.registerElement(ryusThemeDisk);
        this.registerElement(kensThemeDisk);
        this.registerElement(routeOneDisk);
        this.registerElement(centerTheme);
        this.registerElement(gymBattle);
        this.registerElement(gerudoValleyDisk);
        this.registerElement(windmillDisk);
        this.registerElement(lostWoodsDisk);
    }
}
