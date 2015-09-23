package net.samagames.hub.gui.interactions;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            this.setSlotData(this.makeHeadOf(friend), (baseSlots[slot] + (lines * 9)), "friend_" + friend.toString());

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
        if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().closeGui(player);
        }
        else if(action.equals("page_back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiFriends((this.page - 1)));
        }
        else if(action.equals("page_next"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiFriends((this.page + 1)));
        }
        else
        {
            UUID friend = UUID.fromString(action.split("_")[1]);

            if(clickType == ClickType.LEFT)
            {
                String name = SamaGamesAPI.get().getUUIDTranslator().getName(friend);

                //TODO: Clickable message
                //player.sendMessage(new ComponentBuilder("Cliquez ici pour envoyer un message à " + name).color(net.md_5.bungee.api.ChatColor.YELLOW).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name)).create());
            }
            else if(clickType == ClickType.RIGHT)
            {
                //TODO: Not implemented yet in RestAPI
            }
        }
    }

    private ItemStack makeHeadOf(UUID friend)
    {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        String name = SamaGamesAPI.get().getUUIDTranslator().getName(friend);

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
