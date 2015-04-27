package net.samagames.hub.gui.shop;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
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

public class GuiRanksShop extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 45, "Acheter un rang");

        this.drawRank(ChatColor.GREEN, "VIP", "vip", Material.IRON_BOOTS, DyeColor.GREEN, 20, 1000, 6500);
        this.drawRank(ChatColor.AQUA, "VIP+", "vipplus", Material.GOLD_BOOTS, DyeColor.LIGHT_BLUE, 24, 2000, 13500);

        this.setSlotData(ChatColor.AQUA + "Vous avez " + SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getStars() + " étoiles", Material.NETHER_STAR, this.inventory.getSize() - 6, null, "none");
        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 4, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("buy_"))
        {
            String[] data = action.split("_");
            String rankName = data[1];
            String rank = data[2];
            int costMonth = Integer.valueOf(data[3]);
            int costLife = Integer.valueOf(data[4]);

            Hub.getInstance().getGuiManager().openGui(player, new GuiRankShop(rankName, rank, costMonth, costLife));
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiShop());
        }
    }

    private void drawRank(ChatColor rankColor, String rankName, String inDatabase, Material boots, DyeColor glassColor, int baseSlot, int costMonth, int costLife)
    {
        ItemStack rankBoots = new ItemStack(boots, 1);
        ItemMeta rankBootsMeta = rankBoots.getItemMeta();
        rankBootsMeta.setDisplayName(rankColor + rankName);

        ArrayList<String> lores = new ArrayList<>();
        lores.add(rankColor + "Acheter un rang vous permet");
        lores.add(rankColor + "d'obtenir des fonctionnalités");
        lores.add(rankColor + "inédites :");
        lores.add("");
        lores.add(rankColor + "Liste des fonctionnalités");
        lores.add(rankColor + "disponnible sur la boutique :");
        lores.add(rankColor + "http://samagames.net/boutique/");
        lores.add("");
        lores.add(ChatColor.GOLD + "1 mois : " + rankColor + costMonth + " étoiles");
        lores.add(ChatColor.GOLD + "Définitif : " + rankColor + costLife + " étoiles");

        rankBootsMeta.setLore(lores);
        rankBoots.setItemMeta(rankBootsMeta);

        this.setSlotData(rankBoots, baseSlot, "buy_" + rankName + "_" + inDatabase + "_" + costMonth + "_" + costLife);

        int originSlot = baseSlot;

        while(originSlot > 9)
            originSlot -= 9;

        while(originSlot <= 45)
        {
            if(this.inventory.getItem(originSlot) == null)
                this.setSlotData(ChatColor.GRAY + "", new ItemStack(Material.STAINED_GLASS_PANE, 1, glassColor.getData()), originSlot, null, "buy_" + rankName + "_" + inDatabase + "_" + costMonth + "_" + costLife);

            originSlot += 9;
        }
    }
}
