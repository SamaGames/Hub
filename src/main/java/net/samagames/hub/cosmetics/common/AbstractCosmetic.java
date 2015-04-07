package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.permissionsbukkit.PermissionsBukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCosmetic
{
    private enum BuyMethod { FREE, COINS, STARS, PERMISSION };

    protected final String databaseName;
    protected final ItemStack icon;

    private BuyMethod buyMethod;
    private String permissionNeeded;
    private int coinsCost;
    private int starsCost;

    public AbstractCosmetic(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        this.databaseName = databaseName;
        this.icon = icon;
        this.buyMethod = BuyMethod.FREE;

        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + displayName);

        if(description != null)
        {
            ArrayList<String> lores = new ArrayList<>();

            for (String str : description)
                lores.add(ChatColor.GRAY + str);

            meta.setLore(lores);
        }

        this.icon.setItemMeta(meta);
    }

    public void buy(Player player)
    {
        if(this.buyMethod == BuyMethod.FREE)
            return;

        if(this.buyMethod == BuyMethod.COINS)
        {
            if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(this.coinsCost))
            {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cela.");
                return;
            }

            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.coinsCost);
        }
        else if(this.buyMethod == BuyMethod.STARS)
        {
            if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughStars(this.starsCost))
            {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'étoiles pour acheter cela.");
                return;
            }

            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawStars(this.starsCost);
        }

        SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).set("cosmetics." + this.databaseName, "yes");
        Hub.getInstance().getGuiManager().getPlayerGui(player).update(player);
        player.sendMessage(ChatColor.GREEN + "Cosmétique achetée ! Re-cliquez pour l'utiliser.");
    }

    public void buyableWithCoins(int cost)
    {
        this.buyMethod = BuyMethod.COINS;
        this.coinsCost = cost;
    }

    public void buyableWithStars(int cost)
    {
        this.buyMethod = BuyMethod.STARS;
        this.starsCost = cost;
    }

    public void permissionNeeded(String permissionNeeded)
    {
        this.buyMethod = BuyMethod.PERMISSION;
        this.permissionNeeded = permissionNeeded;
    }

    public String getDatabaseName()
    {
        return this.databaseName;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public ItemStack getIcon(Player player)
    {
        ItemStack icon = this.icon.clone();
        ItemMeta meta = icon.getItemMeta();

        List<String> lores = meta.getLore();

        if(lores == null)
            lores = new ArrayList<>();

        lores.add("");

        if(this.isOwned(player))
            lores.add(ChatColor.GREEN + "Possédé");
        else if(this.buyMethod == BuyMethod.FREE)
            lores.add(ChatColor.GREEN + "Gratuit ! :)");
        else if(this.buyMethod == BuyMethod.COINS)
            lores.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.coinsCost + " pièces");
        else if(this.buyMethod == BuyMethod.STARS)
            lores.add(ChatColor.GRAY + "Prix : " + ChatColor.AQUA + this.starsCost + " étoiles");
        else if(this.buyMethod == BuyMethod.PERMISSION)
            lores.add(ChatColor.RED + "Votre grade ne vous permet pas d'utiliser cela.");

        meta.setLore(lores);
        icon.setItemMeta(meta);

        return icon;
    }

    public boolean isOwned(Player player)
    {
        if(this.buyMethod == BuyMethod.PERMISSION)
            return PermissionsBukkit.hasPermission(player, this.permissionNeeded);
        else
            return SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).contains("cosmetics." + this.databaseName);
    }
}
