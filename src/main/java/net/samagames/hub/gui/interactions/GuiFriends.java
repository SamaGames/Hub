package net.samagames.hub.gui.interactions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiFriends extends AbstractGui
{
    private final int page;
    private final int startAt;

    public GuiFriends(int page)
    {
        this.page = page;
        this.startAt = (page - 1) * 27;
    }

    @Override
    public void display(Player player)
    {
        List<UUID> friends = SamaGamesAPI.get().getFriendsManager().uuidFriendsList(player.getUniqueId());
        ArrayList<UUID> toDisplay = new ArrayList<>();
        boolean more = (friends.size() > (this.startAt + 27));

        for(int i = this.startAt; i < (this.startAt + 27); i++)
        {
            if(friends.size() <= i)
                break;

            toDisplay.add(friends.get(i));
        }

        this.inventory = Bukkit.createInventory(null, 54, "Amis");

        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int lines = 0;
        int slot = 0;

        for(UUID friend : toDisplay)
        {
            String friendName = SamaGamesAPI.get().getUUIDTranslator().getName(friend);

            // Avoid Unknown players
            if (friendName == null)
                continue;
            this.setSlotData(this.makeHeadOf(friendName), (baseSlots[slot] + (lines * 9)), "friend_" + friend);

            slot++;

            if (slot == 7)
            {
                slot = 0;
                lines++;
            }
        }

        if(this.page > 1)
            this.setSlotData(ChatColor.YELLOW + "« Page " + (this.page - 1), Material.PAPER, this.inventory.getSize() - 9, null, "page_back");

        if(more)
            this.setSlotData(ChatColor.YELLOW + "Page " + (this.page + 1) + " »", Material.PAPER, this.inventory.getSize() - 9, null, "page_next");

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        switch (action)
        {
            case "back":
                Hub.getInstance().getGuiManager().closeGui(player);
                break;
            case "page_back":
                Hub.getInstance().getGuiManager().openGui(player, new GuiFriends((this.page - 1)));
                break;
            case "page_next":
                Hub.getInstance().getGuiManager().openGui(player, new GuiFriends((this.page + 1)));
                break;
            default:
                UUID friend = UUID.fromString(action.split("_")[1]);
                String name = SamaGamesAPI.get().getUUIDTranslator().getName(friend);
                if (clickType == ClickType.LEFT)
                {
                    //TODO: Clickable message
                    //player.sendMessage(new ComponentBuilder("Cliquez ici pour envoyer un message à " + name).color(net.md_5.bungee.api.ChatColor.YELLOW).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name)).create());
                } else if (clickType == ClickType.RIGHT)
                {
                    if (SamaGamesAPI.get().getFriendsManager().removeFriend(player.getUniqueId(), friend))
                        player.spigot().sendMessage(new ComponentBuilder("Vous avez supprimé " + name + " de votre liste d'amis").color(ChatColor.GREEN).create());
                    else
                        player.spigot().sendMessage(new ComponentBuilder("Erreur lors de la suppression de " + name + " de votre liste d'amis").color(ChatColor.RED).create());
                }
                break;
        }
    }

    private ItemStack makeHeadOf(String name)
    {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setOwner(name);
        meta.setDisplayName(ChatColor.GOLD + name);

        ArrayList<String> lores = new ArrayList<>();
        lores.add(ChatColor.GREEN + "» Clic gauche pour envoyer un message");
        lores.add(ChatColor.RED + "» Clic droit pour supprimer de la liste d'amis");

        meta.setLore(lores);

        head.setItemMeta(meta);

        return head;
    }
}
