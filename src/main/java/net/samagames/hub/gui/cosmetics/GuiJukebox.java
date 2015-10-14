package net.samagames.hub.gui.cosmetics;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.jukebox.JukeboxAlbum;
import net.samagames.hub.cosmetics.jukebox.JukeboxDiskCosmetic;
import net.samagames.hub.cosmetics.jukebox.JukeboxSong;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.shop.GuiConfirm;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class GuiJukebox extends AbstractGui
{
    private BukkitTask loopTask;

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 54, "Jukebox");

        this.update(player);
        this.loopTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), () -> this.update(player), 20L, 20L);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        JukeboxSong song = Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong();

        this.setSlotData(ChatColor.GREEN + "»", Material.JUKEBOX, 12, null, "none");
        this.drawDisk(song);
        this.setSlotData(ChatColor.GREEN + "«", Material.JUKEBOX, 14, null, "none");

        int[] baseSlots = new int[] { 19, 20, 21, 22, 23, 24, 25 };
        int lines = 0;
        int slot = 0;

        ArrayList<JukeboxAlbum> albums = new ArrayList<>(Hub.getInstance().getCosmeticManager().getJukeboxManager().getAlbums().values());

        for (int i = 0; i < 21; i++)
        {
            if(i >= albums.size())
            {
                this.setSlotData(ChatColor.RED + "Prochainement...", new ItemStack(Material.INK_SACK, 1, (short) 8), (baseSlots[slot] + (lines * 9)), null, "none");
            }
            else
            {
                JukeboxAlbum album = albums.get(i);
                this.setSlotData(album.getIcon(), (baseSlots[slot] + (lines * 9)), "album_" + album.getIdentifier());
            }

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
        if(this.loopTask != null)
            this.loopTask.cancel();
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("album_"))
        {
            String albumIdentifier = action.split("_")[1];
            JukeboxAlbum album = Hub.getInstance().getCosmeticManager().getJukeboxManager().getAlbum(albumIdentifier);

            if(clickType == ClickType.LEFT)
            {
                Hub.getInstance().getGuiManager().openGui(player, new GuiJukeboxAlbum(album));
            }
            else if(clickType == ClickType.RIGHT)
            {
                boolean buyed = true;
                boolean oneBuyed = false;

                for(JukeboxDiskCosmetic disk : album.getDisks())
                {
                    if (!disk.isOwned(player))
                    {
                        buyed = false;
                    }
                    else
                    {
                        oneBuyed = true;
                    }
                }

                if(buyed)
                {
                    player.sendMessage(ChatColor.RED + "L'album est déjà acheté !");
                }
                else if(oneBuyed)
                {
                    player.sendMessage(ChatColor.RED + "Vous ne pouvez acheter un album uniquement quand aucune de ses musique n'est achetée !");
                }
                else
                {
                    if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughStars(album.getCost()))
                    {
                        player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'étoiles pour acheter cela.");
                        return;
                    }

                    GuiConfirm confirm = new GuiConfirm(Hub.getInstance().getGuiManager().getPlayerGui(player), () ->
                    {
                        SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawStars(album.getCost(), (a, b, c) -> {
                            for(JukeboxDiskCosmetic disk : album.getDisks())
                            {
                                disk.buyCallback(player, true);
                            }

                            player.sendMessage(ChatColor.GREEN + "L'album a bien été acheté !");
                        });
                    });

                    Hub.getInstance().getGuiManager().openGui(player, confirm);
                }
            }
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiCosmetics());
        }
    }

    private void drawDisk(JukeboxSong song)
    {
        if(song == null)
        {
            this.setSlotData(ChatColor.RED + "Aucun son actuellement !", Material.BARRIER, 13, null, "none");
            return;
        }

        ItemStack icon = song.getDisk().getIcon().clone();
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "En cours : " + song.getSong().getTitle());

        ArrayList<String> lores = new ArrayList<>();
        lores.add(ChatColor.GRAY + "par " + ChatColor.GOLD + song.getSong().getAuthor());
        lores.add(ChatColor.GRAY + "joué par " + ChatColor.GOLD + song.getPlayedBy());
        lores.add("");
        lores.add(ChatColor.GREEN + "Morceaux à venir :");

        for(int i = 0; i < 5; i++)
        {
            if(Hub.getInstance().getCosmeticManager().getJukeboxManager().getPlaylists().isEmpty())
            {
                lores.add(ChatColor.RED + "Aucun :(");
                break;
            }
            else if(Hub.getInstance().getCosmeticManager().getJukeboxManager().getPlaylists().size() <= i)
            {
                continue;
            }

            JukeboxSong next = Hub.getInstance().getCosmeticManager().getJukeboxManager().getPlaylists().get(i);
            lores.add(ChatColor.GOLD + String.valueOf((i + 1)) + ChatColor.GRAY + " : " + ChatColor.GOLD + next.getSong().getTitle() + ChatColor.GRAY + " joué par " + ChatColor.GOLD + next.getPlayedBy());
        }

        meta.setLore(lores);
        icon.setItemMeta(meta);
        this.setSlotData(icon, 13, "none");
    }
}
