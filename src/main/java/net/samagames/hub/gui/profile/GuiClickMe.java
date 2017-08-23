package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.RankUtils;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
public class GuiClickMe extends AbstractGui
{
    private final String name;
    private final UUID uuid;

    public GuiClickMe(Hub hub, String name, UUID uuid)
    {
        super(hub);

        this.name = name;
        this.uuid = uuid;
    }

    public GuiClickMe(Hub hub, Player who)
    {
        this(hub, SamaGamesAPI.get().getPlayerManager().getPlayerData(who.getUniqueId()).getDisplayName(), who.getUniqueId());
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "ClickMe de " + this.name);

        this.setSlotData(ChatColor.GOLD + this.name, this.getPlayerHead(), 13, new String[] {
                ChatColor.GRAY + "Rang : " + RankUtils.getFormattedRank(this.uuid, false),
                ""
        }, "none");

        this.setSlotData(this.getCoinsItemTitle(), Material.GOLD_INGOT, 20, null, "none");
        this.setSlotData(ChatColor.GREEN + "Envoyer une demande d'ami", new ItemStack(Material.RAW_FISH, 1, (short) 3), 21, null, "friend");
        this.setSlotData(ChatColor.GREEN + "Envoyer un message privé", Material.PAPER, 22, null, "mp");
        this.setSlotData(ChatColor.GREEN + "Envoyer une demande de groupe", Material.LEASH, 23, null, "party");
        this.setSlotData(this.getPowderItemTitle(), Material.NETHER_STAR, 24, null, "none");
        this.setSlotData(ChatColor.GREEN + "Statistiques", Material.ENCHANTED_BOOK, 31, null, "stats");

        this.setSlotData(getBackIcon(), 49, "back");

        this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        switch (action)
        {
            case "friend":
                new FancyMessage(ChatColor.YELLOW + "Cliquez sur ").then("[Inviter]").color(ChatColor.GREEN).suggest("/friends add " + this.name).then(" pour inviter ce joueur en ami.").color(ChatColor.YELLOW).send(player);
                this.hub.getGuiManager().closeGui(player);
                break;

            case "mp":
                new FancyMessage(ChatColor.YELLOW + "Cliquez sur ").then("[Envoyer]").color(ChatColor.GREEN).suggest("/msg " + this.name + " ").then(" pour envoyer un message privé à " + this.name + ".").color(ChatColor.YELLOW).send(player);
                this.hub.getGuiManager().closeGui(player);
                break;

            case "party":
                new FancyMessage(ChatColor.YELLOW + "Cliquez sur ").then("[Inviter]").color(ChatColor.GREEN).suggest("/party invite " + this.name).then(" pour inviter ce joueur dans votre partie.").color(ChatColor.YELLOW).send(player);
                this.hub.getGuiManager().closeGui(player);
                break;

            case "stats":
                this.hub.getGuiManager().openGui(player, new GuiStatistics(this.hub, this.hub.getServer().getPlayer(this.uuid)));
                break;

            case "back":
                this.hub.getGuiManager().closeGui(player);
                break;
        }
    }

    private ItemStack getPlayerHead()
    {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(this.name);
        head.setItemMeta(meta);

        return head;
    }

    private String getCoinsItemTitle()
    {
        if (SamaGamesAPI.get().getSettingsManager().getSettings(this.uuid).isAllowCoinsOnClick())
            return ChatColor.GOLD + String.valueOf(SamaGamesAPI.get().getPlayerManager().getPlayerData(this.uuid).getCoins()) + " pièces";
        else
            return ChatColor.RED + "Cette information est privée !";
    }

    private String getPowderItemTitle()
    {
        if (SamaGamesAPI.get().getSettingsManager().getSettings(this.uuid).isAllowPowdersOnClick())
            return ChatColor.AQUA + String.valueOf(SamaGamesAPI.get().getPlayerManager().getPlayerData(this.uuid).getPowders()) + " poussières d'\u272F";
        else
            return ChatColor.RED + "Cette information est privée !";
    }
}
