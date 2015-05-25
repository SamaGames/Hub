package net.samagames.hub.gui.shop;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;

public class GuiRankShop extends AbstractGui
{
    private final String rankName;
    private final String inDatabase;
    private final int costMonth;
    private final int costLife;

    public GuiRankShop(String rankName, String inDatabase, int costMonth, int costLife)
    {
        this.rankName = rankName;
        this.inDatabase = inDatabase;
        this.costMonth = costMonth;
        this.costLife = costLife;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 36, "Acheter un rang " + this.rankName);

        this.setSlotData(ChatColor.AQUA + "1 mois", new ItemStack(Material.NETHER_STAR, 1), 11, new String[] {
                ChatColor.GRAY + "Acheter le grade " + this.rankName,
                ChatColor.GRAY + "pour une durée de 1 mois",
                "",
                ChatColor.GOLD + "Prix : " + this.costMonth + " étoiles"
        }, "month");

        this.setSlotData(ChatColor.AQUA + "Définitif", new ItemStack(Material.NETHER_STAR, 64), 15, new String[] {
                ChatColor.GRAY + "Acheter le grade " + this.rankName,
                ChatColor.GRAY + "pour une durée très très",
                ChatColor.GRAY + "longue (éternelle)",
                "",
                ChatColor.GOLD + "Prix : " + this.costLife + " étoiles"
        }, "life");

        this.setSlotData(ChatColor.AQUA + "Vous avez " + SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getStars() + " étoiles", Material.NETHER_STAR, this.inventory.getSize() - 6, null, "none");
        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 4, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("month"))
        {
            if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughStars(this.costMonth))
            {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'étoiles pour acheter ce grade.");
                return;
            }

            if(clickType == ClickType.LEFT)
            {
                player.sendMessage(ChatColor.GREEN + "Faites un clic droit pour valider. " + ChatColor.RED + "Attention ! Aucun remboursement sera possible !");
                return;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);

            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawStars(this.costMonth);

            SamaGamesAPI.get().getPermissionsManager().getApi().getUser(player.getUniqueId()).addParent(SamaGamesAPI.get().getPermissionsManager().getApi().getGroup(this.inDatabase), calendar.getTime());
            SamaGamesAPI.get().getPermissionsManager().getApi().getManager().refresh();

            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
            player.sendMessage(ChatColor.GREEN + "Votre grade à bien été acheté. Celui-ci sera effectif d'ici 5 minutes. Une reconnexion est nécéssaire pour voir le grade.");
        }
        else if(action.equals("life"))
        {
            if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughStars(this.costLife))
            {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'étoiles pour acheter ce grade.");
                return;
            }

            if(clickType == ClickType.LEFT)
            {
                player.sendMessage(ChatColor.GREEN + "Faites un clic droit pour valider. " + ChatColor.RED + "Attention ! Aucun remboursement sera possible !");
                return;
            }

            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawStars(this.costLife);

            SamaGamesAPI.get().getPermissionsManager().getApi().getUser(player.getUniqueId()).addParent(SamaGamesAPI.get().getPermissionsManager().getApi().getGroup(this.inDatabase));
            SamaGamesAPI.get().getPermissionsManager().getApi().getManager().refresh();

            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
            player.sendMessage(ChatColor.GREEN + "Votre grade à bien été acheté. Celui-ci sera effectif d'ici 5 minutes. Une reconnexion est nécéssaire pour voir le grade.");
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiRanksShop());
        }
    }
}
