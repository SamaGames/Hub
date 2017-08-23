package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.AbstractPlayerData;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.achievements.GuiAchievements;
import net.samagames.hub.utils.NumberUtils;
import net.samagames.tools.PlayerUtils;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
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
public class GuiProfile extends AbstractGui
{
    public GuiProfile(Hub hub)
    {
        super(hub);
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 45, "Profil");

        this.setSlotData(this.createPlayerHead(player), 13, "none");
        this.setSlotData(ChatColor.GOLD + "Statistiques", Material.ENCHANTED_BOOK, 20, new String[] { ChatColor.GRAY + "Retrouvez vos scores et classements !" }, "stats");
        this.setSlotData(ChatColor.GOLD + "Objectifs", Material.DIAMOND, 22, new String[] { ChatColor.GRAY + "Allez-vous réussir à tous les", ChatColor.GRAY + "compléter ?" }, "achievements");
        this.setSlotData(ChatColor.GOLD + "Paramètres", Material.DIODE, 24, new String[] { ChatColor.GRAY + "Vos préférences sur le serveur" }, "settings");

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        switch (action)
        {
            case "stats":
                this.hub.getGuiManager().openGui(player, new GuiStatistics(this.hub, player));
                break;

            case "achievements":
                this.hub.getGuiManager().openGui(player, new GuiAchievements(this.hub, null, 0));
                break;

            case "settings":
                this.hub.getGuiManager().openGui(player, new GuiSettings(this.hub, 1));
                break;

            case "back":
                this.hub.getGuiManager().closeGui(player);
                break;
        }
    }

    private ItemStack createPlayerHead(Player player)
    {
        AbstractPlayerData playerData = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId());

        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(PlayerUtils.getFullyFormattedPlayerName(player));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Pièces : " + ChatColor.GOLD + NumberUtils.format(playerData.getCoins()));
        lore.add(ChatColor.GRAY + "Perles : " + ChatColor.GREEN + NumberUtils.format(this.hub.getInteractionManager().getGraouManager().getPlayerPearls(player.getUniqueId()).size()));
        lore.add(ChatColor.GRAY + "Poussière d'" + ChatColor.AQUA + "\u272F" + ChatColor.GRAY + " : " + ChatColor.AQUA + NumberUtils.format(playerData.getPowders()));

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }
}
