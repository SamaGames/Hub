package net.samagames.hub.parkours.types;

import net.samagames.hub.Hub;
import net.samagames.hub.parkours.Parkour;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

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
public class WhitelistBasedParkour extends Parkour
{
    private final List<Material> whitelist;

    public WhitelistBasedParkour(Hub hub, String prefix, String winPrefix, String parkourName, Location spawn, Location end, Location fail, int minimalHeight, int lifesToAdd, List<Location> checkpoints, List<Material> whitelist, int difficulty, int achievementId)
    {
        super(hub, prefix, winPrefix, parkourName, spawn, end, fail, minimalHeight, lifesToAdd, checkpoints, difficulty, achievementId);

        this.whitelist = whitelist;

        this.whitelist.add(Material.AIR);
        this.whitelist.add(Material.TORCH);
        this.whitelist.add(Material.REDSTONE_TORCH_ON);
        this.whitelist.add(Material.REDSTONE_TORCH_OFF);
        this.whitelist.add(Material.IRON_PLATE);
        this.whitelist.add(Material.GOLD_PLATE);
        this.whitelist.add(Material.STONE_PLATE);
        this.whitelist.add(Material.WOOD_PLATE);
        this.whitelist.add(Material.SIGN);
        this.whitelist.add(Material.SIGN_POST);
        this.whitelist.add(Material.WALL_SIGN);
    }

    public boolean isWhitelisted(Material material)
    {
        return this.whitelist.contains(material);
    }
}
