package net.samagames.hub.cosmetics.pets;

import com.dsh105.echopet.api.EchoPetAPI;
import com.dsh105.echopet.compat.api.entity.IPet;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.lang.model.type.NullType;

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
public class PetManager extends AbstractCosmeticManager<PetCosmetic>
{
    public PetManager(Hub hub)
    {
        super(hub, new PetRegistry(hub));
    }

    @Override
    public void enableCosmetic(Player player, PetCosmetic cosmetic, ClickType clickType, boolean login, NullType useless)
    {
        IPet pet = EchoPetAPI.getAPI().givePet(player, cosmetic.getPetType(), false);
        pet.setPetName(player.getName(), false);

        cosmetic.applyCustomization(pet);

        if (!login)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre animal vient de sortir de l'écurie !");
    }

    @Override
    public void disableCosmetic(Player player, PetCosmetic cosmetic, boolean logout, boolean replace, NullType useless)
    {
        if (EchoPetAPI.getAPI().hasPet(player))
        {
            EchoPetAPI.getAPI().removePet(player, false, false);

            if (!logout && !replace)
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre animal disparait dans l'ombre...");
        }
    }

    @Override
    public void update() {}

    @Override
    public boolean restrictToOne()
    {
        return true;
    }
}
