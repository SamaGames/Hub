package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.profile.stats.GuiStats;
import net.samagames.hub.utils.GuiUtils;
import net.samagames.hub.utils.RankUtils;
import net.samagames.tools.chat.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class GuiClickMe extends AbstractGui
{
    private final String name;
    private final UUID uuid;

    public GuiClickMe(String name, UUID uuid)
    {
        this.name = name;
        this.uuid = uuid;
    }

    public GuiClickMe(Player who)
    {
        this(who.getName(), who.getUniqueId());
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 54, "ClickMe de " + this.name);

        this.setSlotData(ChatColor.GOLD + this.name, this.getPlayerHead(), 13, new String[] {
                ChatColor.GRAY + "Rang : " + RankUtils.getFormattedRank(this.uuid),
                "",
                ChatColor.GRAY + "Idée du ClickMe par " + ChatColor.GOLD + "zyuiop",
                ChatColor.GRAY + "Intégré par " + ChatColor.GOLD + "IamBlueSlime"
        }, "none");

        this.setSlotData(this.getCoinsItemTitle(), Material.GOLD_INGOT, 20, null, "none");
        this.setSlotData(ChatColor.GREEN + "Envoyer une demande d'ami", new ItemStack(Material.RAW_FISH, 1, (short) 3), 21, null, "friend");
        this.setSlotData(ChatColor.GREEN + "Envoyer un message privé", Material.PAPER, 22, null, "mp");
        this.setSlotData(ChatColor.GREEN + "Envoyer une demande de groupe", Material.LEASH, 23, null, "party");
        this.setSlotData(this.getStarsItemTitle(), Material.NETHER_STAR, 24, null, "none");
        this.setSlotData(ChatColor.GREEN + "Statistiques", Material.ENCHANTED_BOOK, 31, null, "stats");

        this.setSlotData(GuiUtils.getBackItem(), 49, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        switch (action)
        {
            case "friend":
                break;
            case "mp":
                new FancyMessage(ChatColor.YELLOW + "Cliquez sur ").then("[Envoyer]").color(ChatColor.GREEN).suggest("/msg " + this.name + " ").then(" pour envoyer un message privé à " + this.name).color(ChatColor.YELLOW).send(player);
                Hub.getInstance().getGuiManager().closeGui(player);
                break;
            case "party":
                break;
            case "stats":
                Hub.getInstance().getGuiManager().openGui(player, new GuiStats(this.name, this.uuid, true));
                break;
            case "back":
                Hub.getInstance().getGuiManager().closeGui(player);
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
        if(SamaGamesAPI.get().getSettingsManager().isEnabled(this.uuid, "clickme-coins", true))
            return ChatColor.GOLD + String.valueOf(SamaGamesAPI.get().getPlayerManager().getPlayerData(this.uuid).getCoins()) + " pièces";
        else
            return ChatColor.RED + "Cette information est privée !";
    }

    private String getStarsItemTitle()
    {
        if(SamaGamesAPI.get().getSettingsManager().isEnabled(this.uuid, "clickme-stars", true))
            return ChatColor.AQUA + String.valueOf(SamaGamesAPI.get().getPlayerManager().getPlayerData(this.uuid).getStars()) + " étoiles";
        else
            return ChatColor.RED + "Cette information est privée !";
    }
}
