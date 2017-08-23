package net.samagames.hub.cosmetics.jukebox;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;

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
        JukeboxDiskCosmetic miiChannelDisk = new JukeboxDiskCosmetic(this.hub, 103, NBSDecoder.parse("MiiChannel"), 102);
        JukeboxDiskCosmetic starfox64Disk = new JukeboxDiskCosmetic(this.hub, 104, NBSDecoder.parse("StarFox64Theme"), 45);
        JukeboxDiskCosmetic neverGonnaGiveYouUpDisk = new JukeboxDiskCosmetic(this.hub, 105, NBSDecoder.parse("NeverGonnaGiveYouUp"), 324);
        JukeboxDiskCosmetic callMeMaybeDisk = new JukeboxDiskCosmetic(this.hub, 106, NBSDecoder.parse("CallMeMaybe"), 100);
        JukeboxDiskCosmetic ghostBustersDisk = new JukeboxDiskCosmetic(this.hub, 108, NBSDecoder.parse("GhostBusters"), 249);
        JukeboxDiskCosmetic hitTheRoadJackDisk = new JukeboxDiskCosmetic(this.hub, 109, NBSDecoder.parse("HitTheRoadJack"), 114);
        JukeboxDiskCosmetic indianaJonesDisk = new JukeboxDiskCosmetic(this.hub, 110, NBSDecoder.parse("IndianaJones"), 130);
        JukeboxDiskCosmetic marseillaiseDisk = new JukeboxDiskCosmetic(this.hub, 111, NBSDecoder.parse("Marseillaise"), 45);
        JukeboxDiskCosmetic nyanCatDisk = new JukeboxDiskCosmetic(this.hub, 112, NBSDecoder.parse("NyanCat"), 76);
        JukeboxDiskCosmetic superMarioDisk = new JukeboxDiskCosmetic(this.hub, 113, NBSDecoder.parse("SuperMarioBrosMainTheme"), 77);
        JukeboxDiskCosmetic theLostOneWeepingDisk = new JukeboxDiskCosmetic(this.hub, 114, NBSDecoder.parse("TheLostOneWeeping"), 212);
        JukeboxDiskCosmetic amenoDisk = new JukeboxDiskCosmetic(this.hub, 141, NBSDecoder.parse("AmenoEra"), 232);
        JukeboxDiskCosmetic gameOfThrones = new JukeboxDiskCosmetic(this.hub, 176, NBSDecoder.parse("GameOfThrones"), 122);

        this.registerElement(guilesThemeDisk);
        this.registerElement(ryusThemeDisk);
        this.registerElement(kensThemeDisk);
        this.registerElement(routeOneDisk);
        this.registerElement(centerTheme);
        this.registerElement(gymBattle);
        this.registerElement(gerudoValleyDisk);
        this.registerElement(windmillDisk);
        this.registerElement(lostWoodsDisk);
        this.registerElement(miiChannelDisk);
        this.registerElement(starfox64Disk);
        this.registerElement(neverGonnaGiveYouUpDisk);
        this.registerElement(callMeMaybeDisk);
        this.registerElement(ghostBustersDisk);
        this.registerElement(hitTheRoadJackDisk);
        this.registerElement(indianaJonesDisk);
        this.registerElement(marseillaiseDisk);
        this.registerElement(nyanCatDisk);
        this.registerElement(superMarioDisk);
        this.registerElement(theLostOneWeepingDisk);
        this.registerElement(amenoDisk);
        this.registerElement(gameOfThrones);
    }
}
