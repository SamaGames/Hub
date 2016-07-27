package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.AbstractPlayerData;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
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
        this.setSlotData(ChatColor.GOLD + "Statistiques", Material.ENCHANTED_BOOK, 20, new String[] { ChatColor.GRAY + "Retrouvez vos scores et classements !", "", ChatColor.RED + "Prochainement..." }, "stats");
        this.setSlotData(ChatColor.GOLD + "Objectifs", Material.DIAMOND, 22, new String[] { ChatColor.GRAY + "Allez-vous réussir à tous les", ChatColor.GRAY + "compléter ?", "", ChatColor.RED + "Prochainement..." }, "achievements");
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
                new FancyMessage(ChatColor.YELLOW + "Cliquez sur ").then("[Accéder]").color(ChatColor.GOLD).style(ChatColor.BOLD).link("https://www.samagames.net/stats/" + player.getName() + ".html").then(" pour accéder à vos statistiques.").color(ChatColor.YELLOW).send(player);
                break;

            case "achievements":
                player.sendMessage(ChatColor.RED + "Prochainement...");
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
        lore.add(ChatColor.GRAY + "Pièces : " + ChatColor.GOLD + playerData.getCoins());
        lore.add(ChatColor.GRAY + "Etoiles : " + ChatColor.AQUA + playerData.getStars());

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }
}
