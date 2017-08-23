package net.samagames.hub.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.lang.model.type.NullType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
public class ClothManager extends AbstractCosmeticManager<ClothCosmetic>
{
    private final Map<UUID, ClothPreviewTask> previewers;

    public ClothManager(Hub hub)
    {
        super(hub, new ClothRegistry(hub));

        this.previewers = new HashMap<>();
    }

    @Override
    public void enableCosmetic(Player player, ClothCosmetic cosmetic, ClickType clickType, boolean login)
    {
        if (clickType == ClickType.RIGHT)
            this.startPreview(player, cosmetic);
        else
            super.enableCosmetic(player, cosmetic, null, login);
    }

    @Override
    public void enableCosmetic(Player player, ClothCosmetic cosmetic, ClickType clickType, boolean login, NullType useless)
    {
        if (this.getEquippedCosmetics(player) != null)
            this.getEquippedCosmetics(player).stream().filter(c -> cosmetic.getSlot() == c.getSlot()).forEach(c -> this.disableCosmetic(player, c, false, true));

        cosmetic.getSlot().equip(player, cosmetic.getPiece());

        if (!login)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Vous brillez de mille feux avec votre nouvel habit !");
    }

    @Override
    public void disableCosmetic(Player player, ClothCosmetic cosmetic, boolean logout, boolean replace, NullType useless)
    {
        if (this.previewers.containsKey(player.getUniqueId()))
            this.stopPreview(player);

        cosmetic.getSlot().equip(player, null);

        if (!logout && !replace)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Vous retirez votre vêtement...");
    }

    @Override
    public void update() { /** Not needed **/ }

    @Override
    public boolean restrictToOne()
    {
        return false;
    }

    public void startPreview(Player player, ClothCosmetic cosmetic)
    {
        ItemStack[] armorContent = new ItemStack[4];

        if (this.getEquippedCosmetics(player) != null)
            for (ClothCosmetic equipped : this.getEquippedCosmetics(player))
                armorContent[equipped.getSlot().ordinal()] = equipped.getPiece();

        armorContent[cosmetic.getSlot().ordinal()] = cosmetic.getPiece();

        ClothPreviewTask clothPreviewTask = new ClothPreviewTask(this.hub, player, armorContent);

        this.hub.getGuiManager().closeGui(player);

        player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.YELLOW + "Appuyez sur votre touche " + ChatColor.ITALIC + "sneak" + ChatColor.YELLOW + " pour quitter la prévisualisation.");

        this.hub.getPlayerManager().addHider(player);
        this.previewers.put(player.getUniqueId(), clothPreviewTask);
    }

    public void stopPreview(Player player)
    {
        if (this.previewers.containsKey(player.getUniqueId()))
        {
            this.previewers.get(player.getUniqueId()).stop();
            this.previewers.remove(player.getUniqueId());

            this.hub.getPlayerManager().removeHider(player);
        }
    }

    public boolean isPreviewing(Player player)
    {
        return this.previewers.containsKey(player.getUniqueId());
    }
}
