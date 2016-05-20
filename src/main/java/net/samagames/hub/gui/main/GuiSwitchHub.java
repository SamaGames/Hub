package net.samagames.hub.gui.main;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.refresh.JsonHub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiSwitchHub extends AbstractGui
{
    private final int page;

    public GuiSwitchHub(Hub hub, int page)
    {
        super(hub);

        this.page = page;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Changer de hub (Page " + this.page + ")");
        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        List<JsonHub> hubs = this.hub.getHubRefresher().getHubs();
        int[] baseSlots = { 10, 11, 12, 13, 14, 15, 16 };
        int line = 0;
        int slot = 0;

        this.inventory.clear();

        int i = 0;
        boolean more = false;

        for(JsonHub hub : hubs)
        {
            if (i < (7 * (this.page - 1)))
            {
                i++;
                continue;
            }
            else if (i > ((7 * (this.page - 1) + (baseSlots.length * 3))))
            {
                more = true;
                break;
            }

            this.setSlotData(this.getHubItem(hub), baseSlots[slot] + (9 * line), "Hub_" + hub.getHubNumber());
            slot++;

            if(slot == baseSlots.length)
            {
                slot = 0;
                line++;
            }

            i++;
        }

        if(this.page > 1)
            this.setSlotData(ChatColor.YELLOW + "« Page " + (this.page - 1), Material.PAPER, this.inventory.getSize() - 9, null, "page_back");

        if(more)
            this.setSlotData(ChatColor.YELLOW + "Page " + (this.page + 1) + " »", Material.PAPER, this.inventory.getSize() - 1, null, "page_next");

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("Hub_"))
        {
            if(action.split("_")[1].equals(SamaGamesAPI.get().getServerName().split("_")[1]))
            {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas aller sur votre hub actuel !");
                return;
            }
            else if(this.hub.getHubRefresher().getHubByID(Integer.parseInt(action.split("_")[1])).getConnectedPlayers() >= 120)
            {
                player.sendMessage(ChatColor.RED + "Le hub sélectionné est plein !");
                return;
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(action);
            player.sendPluginMessage(this.hub, "BungeeCord", out.toByteArray());

            // TODO: Use API
            // SamaGamesAPI.get().getPlayerManager().connectToServer(player.getUniqueId(), action);
        }
        else if(action.equals("page_back"))
        {
            this.hub.getGuiManager().openGui(player, new GuiSwitchHub(this.hub, (this.page - 1)));
        }
        else if(action.equals("page_next"))
        {
            this.hub.getGuiManager().openGui(player, new GuiSwitchHub(this.hub, (this.page + 1)));
        }
        else if(action.equals("back"))
        {
            this.hub.getGuiManager().openGui(player, new GuiMain(this.hub));
        }
    }

    private ItemStack getHubItem(JsonHub hub)
    {
        ItemStack glass = new ItemStack(Material.STAINED_GLASS, 1);
        ItemMeta meta = glass.getItemMeta();
        String baseName = "Hub " + hub.getHubNumber() + " (" + hub.getConnectedPlayers() + " joueurs)";

        if(hub.getHubNumber() == Integer.parseInt(SamaGamesAPI.get().getServerName().split("_")[1]))
        {
            glass.setDurability(DyeColor.LIGHT_BLUE.getData());
            meta.setDisplayName(ChatColor.AQUA + baseName);
        }
        else if(hub.getConnectedPlayers() <= 40)
        {
            glass.setDurability(DyeColor.GREEN.getData());
            meta.setDisplayName(ChatColor.GREEN + baseName);
        }
        else if(hub.getConnectedPlayers() <= 80)
        {
            glass.setDurability(DyeColor.YELLOW.getData());
            meta.setDisplayName(ChatColor.YELLOW + baseName);
        }
        else if(hub.getConnectedPlayers() < 100)
        {
            glass.setDurability(DyeColor.RED.getData());
            meta.setDisplayName(ChatColor.RED + baseName);
        }
        else
        {
            glass.setDurability(DyeColor.RED.getData());
            meta.setDisplayName(ChatColor.RED + baseName);
        }

        List<String> lore = new ArrayList<>();

        for(String group : hub.getPlayersDetails().keySet())
        {
            int value = hub.getPlayersDetails().get(group);
            String finalGroup = (value <= 1 ? group : group.substring(0, group.length() - 1));

            lore.add(finalGroup.replace("[", "").replace("]", "") + ": " + value);
        }

        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "▶ Cliquez pour être téléporté");

        meta.setLore(lore);
        glass.setItemMeta(meta);

        return glass;
    }
}
