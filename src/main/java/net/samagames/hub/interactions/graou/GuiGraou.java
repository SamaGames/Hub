package net.samagames.hub.interactions.graou;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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
class GuiGraou extends AbstractGui
{
    private static final ItemStack NONE_STACK;

    private final Graou parent;
    private final Map<UUID, Pearl> pearls;

    GuiGraou(Hub hub, Graou parent)
    {
        super(hub);

        this.parent = parent;
        this.pearls = new HashMap<>();
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Graou");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player)
    {
        this.pearls.clear();

        if (!this.hub.getInteractionManager().getGraouManager().getPlayerPearls(player.getUniqueId()).isEmpty())
        {
            int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
            int lines = 0;
            int slot = 0;

            for (Pearl pearl : this.hub.getInteractionManager().getGraouManager().getPlayerPearls(player.getUniqueId()))
            {
                if (this.pearls.size() == 24)
                    break;

                this.setSlotData(this.hub.getInteractionManager().getGraouManager().getPearlLogic().getIcon(pearl), baseSlots[slot] + (lines * 9), "pearl_" + pearl.getUUID());

                slot++;

                if (slot == 7)
                {
                    slot = 0;
                    lines++;
                }

                this.pearls.put(pearl.getUUID(), pearl);
            }
        }
        else
        {
            this.setSlotData(NONE_STACK, 22, "none");
        }

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClose(Player player)
    {
        this.parent.stop(player);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("pearl_"))
        {
            Pearl pearl = this.pearls.get(UUID.fromString(action.split("_")[1]));

            if (pearl.getStars() == 4 && !SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "network.vip"))
            {
                player.sendMessage(Graou.TAG + ChatColor.GREEN + "Vous n'avez pas le grade nécéssaire pour échanger cette perle ! (" + ChatColor.GREEN + "VIP" + ChatColor.RED + ")");
                return;
            }
            else if (pearl.getStars() == 5 && !SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "network.vipplus"))
            {
                player.sendMessage(Graou.TAG + ChatColor.GREEN + "Vous n'avez pas le grade nécéssaire pour échanger cette perle ! (" + ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+" + ChatColor.RED + ")");
                return;
            }

            this.parent.openBox(player, this.pearls.get(UUID.fromString(action.split("_")[1])));
            this.hub.getGuiManager().closeGui(player);
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().closeGui(player);
            this.parent.stop(player);
        }
    }

    static
    {
        NONE_STACK = new ItemStack(Material.IRON_FENCE, 1);
        ItemMeta meta = NONE_STACK.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Vous n'avez aucune perle :(");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Jouez à nos différents jeux pour");
        lore.add(ChatColor.GRAY + "obtenir des perles.");

        NONE_STACK.setItemMeta(meta);
    }
}
