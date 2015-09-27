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
import java.util.HashMap;
import java.util.UUID;

public class GuiParty extends AbstractGui
{
    private final int page;
    private final int startAt;

    public GuiParty(int page)
    {
        this.page = page;
        this.startAt = (page - 1) * 27;
    }

    @Override
    public void display(Player player)
    {
        HashMap<UUID, String> partyMembersMap = SamaGamesAPI.get().getPartiesManager().getPlayersInParty(SamaGamesAPI.get().getPartiesManager().getPlayerParty(player.getUniqueId()));
        ArrayList<UUID> partyMembers = new ArrayList<>();
        ArrayList<UUID> toDisplay = new ArrayList<>();
        boolean more = (partyMembers.size() > (this.startAt + 27));

        partyMembers.addAll(partyMembersMap.keySet());

        for(int i = this.startAt; i < (this.startAt + 27); i++)
        {
            if(partyMembers.size() <= i)
                break;

            toDisplay.add(partyMembers.get(i));
        }

        this.inventory = Bukkit.createInventory(null, 54, "Partie");

        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int lines = 0;
        int slot = 0;

        for(UUID friend : toDisplay)
        {
            this.setSlotData(this.makeHeadOf(friend), (baseSlots[slot] + (lines * 9)), "party_" + friend.toString());

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
                Hub.getInstance().getGuiManager().openGui(player, new GuiParty((this.page - 1)));
                break;
            case "page_next":
                Hub.getInstance().getGuiManager().openGui(player, new GuiParty((this.page + 1)));
                break;
            default:
                UUID friend = UUID.fromString(action.split("_")[1]);

                if (clickType == ClickType.LEFT)
                {
                    String name = SamaGamesAPI.get().getUUIDTranslator().getName(friend);
                    //TODO
                }
                break;
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
        lores.add(ChatColor.RED + "» Clic pour supprimer de la partie");

        meta.setLore(lores);

        head.setItemMeta(meta);

        return head;
    }
}
