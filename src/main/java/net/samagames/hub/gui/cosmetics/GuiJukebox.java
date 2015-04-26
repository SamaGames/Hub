package net.samagames.hub.gui.cosmetics;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.jukebox.JukeboxDiskCosmetic;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxPlaylist;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

public class GuiJukebox extends AbstractGui
{
    private int loopId;

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 54, "Jukebox");

        this.update(player);
        this.loopId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hub.getInstance(), () -> this.update(player), 20L, 20L);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        Random random = new Random();
        JukeboxPlaylist song = Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong();
        DyeColor randomizedDye = (song != null ? DyeColor.values()[random.nextInt(DyeColor.values().length)] : DyeColor.GRAY);

        this.drawActualDiskLines(song, randomizedDye);

        int[] baseSlots = new int[] { 19, 20, 21, 22, 23, 24, 25 };
        int lines = 0;
        int slot = 0;

        for (JukeboxDiskCosmetic disk : Hub.getInstance().getCosmeticManager().getJukeboxManager().getRegistry().getElements().values())
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
    public void onClose(Player player)
    {
        Bukkit.getScheduler().cancelTask(this.loopId);
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
            Hub.getInstance().getGuiManager().openGui(player, new GuiCosmetics());
        }
    }

    private void drawActualDiskLines(JukeboxPlaylist song, DyeColor randomizedDye)
    {
        for(int i = 9; i < 18; i++)
        {
            if(i == 13)
            {
                if(song == null)
                {
                    this.setSlotData(ChatColor.RED + "Aucun son actuellement !", Material.BARRIER, i, null, "none");
                    continue;
                }

                ItemStack icon = song.getDisk().getIcon().clone();
                ItemMeta meta = icon.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "En cours : " + song.getSong().getTitle());

                ArrayList<String> lores = new ArrayList<>();
                lores.add(ChatColor.GRAY + "par " + ChatColor.GOLD + song.getSong().getAuthor());
                lores.add(ChatColor.GRAY + "joué par " + ChatColor.GOLD + song.getPlayedBy());
                lores.add("");
                lores.add(ChatColor.GREEN + "Morceaux à venir :");

                for(int j = 0; j < 5; j++)
                {
                    if(Hub.getInstance().getCosmeticManager().getJukeboxManager().getPlaylists().isEmpty())
                    {
                        lores.add(ChatColor.RED + "Aucun :(");
                        break;
                    }
                    else if(Hub.getInstance().getCosmeticManager().getJukeboxManager().getPlaylists().size() <= j)
                    {
                        continue;
                    }

                    JukeboxPlaylist next = Hub.getInstance().getCosmeticManager().getJukeboxManager().getPlaylists().get(j);
                    lores.add(ChatColor.GOLD + String.valueOf((j + 1)) + ChatColor.GRAY + " : " + ChatColor.GOLD + next.getSong().getTitle() + ChatColor.GRAY + " joué par " + ChatColor.GOLD + next.getPlayedBy());
                }

                meta.setLore(lores);
                icon.setItemMeta(meta);
                this.setSlotData(icon, i, "none");
            }
            else
            {
                this.setSlotData(ChatColor.GRAY + "", new ItemStack(Material.STAINED_GLASS_PANE, 1, randomizedDye.getData()), i, null, "none");
            }
        }
    }
}
