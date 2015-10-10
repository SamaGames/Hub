package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.AbstractPlayerData;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.profile.stats.GuiStats;
import net.samagames.hub.utils.GuiUtils;
import net.samagames.tools.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class GuiProfile extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 45, "Profil");

        this.setSlotData(this.createPlayerHead(player), 13, "none");
        this.setSlotData(ChatColor.GOLD + "Statistiques", Material.ENCHANTED_BOOK, 21, new String[] { ChatColor.GRAY + "Retrouvez vos scores et classements !" }, "stats");
        this.setSlotData(ChatColor.GOLD + "Objectifs", Material.DIAMOND, 22, new String[] { ChatColor.GRAY + "Allez-vous réussir à tous les", ChatColor.GRAY + "compléter ?", "", ChatColor.RED + "  Prochainement ..." }, "achievements");
        this.setSlotData(ChatColor.GOLD + "Paramètres", Material.DIODE, 23, new String[] { ChatColor.GRAY + "Vos préférences sur le serveur" }, "settings");

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        switch (action)
        {
            case "stats":
                Hub.getInstance().getGuiManager().openGui(player, new GuiStats(player));
                break;
            case "achievements":
                //Hub.getInstance().getGuiManager().openGui(player, new GuiAchievements());
                player.sendMessage(ChatColor.RED + "Prochainement...");
                break;
            case "settings":
                Hub.getInstance().getGuiManager().openGui(player, new GuiSettings());
                break;
            case "back":
                Hub.getInstance().getGuiManager().closeGui(player);
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

        ArrayList<String> lores = new ArrayList<>();
        lores.add(ChatColor.GRAY + "Pièces : " + ChatColor.GOLD + playerData.getCoins());
        lores.add(ChatColor.GRAY + "Etoiles : " + ChatColor.AQUA + playerData.getStars());

        meta.setLore(lores);
        stack.setItemMeta(meta);

        return stack;
    }
}
