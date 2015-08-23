package net.samagames.hub.gui.cosmetics;

import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.jukebox.JukeboxAlbum;
import net.samagames.hub.cosmetics.jukebox.JukeboxDiskCosmetic;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiJukeboxAlbum extends AbstractGui
{
    private final JukeboxAlbum album;

    public GuiJukeboxAlbum(JukeboxAlbum album)
    {
        this.album = album;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 54, "Jukebox");

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        int[] baseSlots = new int[] { 10, 11, 12, 13, 14, 15, 16 };
        int lines = 0;
        int slot = 0;

        for (JukeboxDiskCosmetic disk : this.album.getDisks())
        {
            this.setSlotData(disk.getIcon(player), (baseSlots[slot] + (lines * 9)), "disk_" + disk.getDatabaseName());

            slot++;

            if (slot == baseSlots.length)
            {
                slot = 0;
                lines++;
            }
        }

        this.setSlotData(ChatColor.AQUA + "Vous avez " + SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getStars() + " étoiles", Material.NETHER_STAR, this.inventory.getSize() - 6, null, "none");
        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 4, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("disk_"))
        {
            String disk = action.split("_")[1];
            JukeboxManager manager = Hub.getInstance().getCosmeticManager().getJukeboxManager();
            manager.enableCosmetic(player, manager.getRegistry().getElementByStorageName(disk));
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiJukebox());
        }
    }
}
