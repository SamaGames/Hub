package net.samagames.hub.gui.stalker;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.network.IProxiedPlayer;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GuiStalkerFriends2000 extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        List<UUID> friendListRaw = SamaGamesAPI.get().getFriendsManager().uuidFriendsList(player.getUniqueId());
        List<IProxiedPlayer> friendList = friendListRaw.stream().filter(friend -> !SamaGamesAPI.get().getProxyDataManager().getProxiedPlayer(friend).getServer().equals("Inconnu")).map(friend -> SamaGamesAPI.get().getProxyDataManager().getProxiedPlayer(friend)).collect(Collectors.toList());

        int[] slots = new int[] { 10, 11, 12, 13, 14, 15, 16, 17 };
        int slot = 0;
        int lines = 1;

        for(IProxiedPlayer friendData : friendList)
        {
            slot++;

            if(slot == slots.length)
            {
                slot = 0;
                lines++;
            }
        }

        this.inventory = Bukkit.createInventory(null, 9 + (9 * lines) + (9 * 2), "Stalker 2000 - Amis");

        slot = 0;
        lines = 0;

        for(IProxiedPlayer friendData : friendList)
        {
            String formattedUsername = PlayerUtils.getFullyFormattedPlayerName(friendData.getUUID());
            String username = friendData.getName();
            String server = friendData.getServer();

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.setOwner(username);
            headMeta.setDisplayName(formattedUsername);

            ArrayList<String> lores = new ArrayList<>();

            String formattedServer;

            formattedServer = ChatColor.GRAY + server.split("_")[0] + " " + server.split("_")[1];
            if(Hub.getInstance().getGameManager().getGameByIdentifier(server.split("_")[0]) != null)
                if (Hub.getInstance().getGameManager().getGameByIdentifier(server.split("_")[0]).getCodeName().equals("beta_staff"))
                    formattedServer = ChatColor.RED + "Secret :o";



            lores.add(ChatColor.GOLD + "Actuellement sur : " + formattedServer);
            lores.add("");
            lores.add(ChatColor.GRAY + "▶ Clique pour rejoindre le joueur");

            headMeta.setLore(lores);
            head.setItemMeta(headMeta);

            this.setSlotData(head, slots[slot] + (9 * lines), "server:" + username);
            slot++;

            if(slot == slots.length)
            {
                slot = 0;
                lines++;
            }
        }

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("server:"))
        {
            String server = action.split(":")[1];
            SamaGamesAPI.get().getProxyDataManager().getProxiedPlayer(player.getUniqueId()).connect(server);
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiStalker2000(1));
        }
    }
}
