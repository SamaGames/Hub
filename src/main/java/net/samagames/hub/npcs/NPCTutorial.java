package net.samagames.hub.npcs;

import net.samagames.tools.tutorials.Tutorial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.BiConsumer;

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
class NPCTutorial extends Tutorial
{
    private BiConsumer<Player, Boolean> consumer;

    @Override
    protected void onTutorialEnds(Player player, boolean interrupted)
    {
        this.consumer.accept(player, interrupted);
    }

    public void onTutorialEnds(BiConsumer<Player, Boolean> consumer)
    {
        this.consumer = consumer;
    }
}
