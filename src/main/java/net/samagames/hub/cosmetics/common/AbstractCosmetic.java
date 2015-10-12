package net.samagames.hub.cosmetics.common;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.AbstractShopsManager;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.shop.GuiConfirm;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCosmetic
{
    private enum BuyMethod
    {
        FREE, COINS, STARS, PERMISSION
    }

    protected final String category;
    protected final String key;
    protected final String displayName;
    protected final ItemStack icon;
    private BuyMethod buyMethod;
    private String permissionNeeded;
    private String permissionNeededToView;
    private int coinsCost;
    private int starsCost;
    private final AbstractShopsManager shopsManager;

    public AbstractCosmetic(String category, String key, String displayName, ItemStack icon, String[] description)
    {
        this.category = category;
        this.key = key;
        this.displayName = ChatColor.RESET + "" + ChatColor.GOLD + displayName;
        this.icon = icon;
        this.buyMethod = BuyMethod.FREE;
        this.permissionNeededToView = null;

        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + displayName);

        if (description != null)
        {
            ArrayList<String> lores = new ArrayList<>();

            for (String str : description)
                lores.add(ChatColor.GRAY + str);

            meta.setLore(lores);
        }

        this.icon.setItemMeta(meta);
        this.shopsManager = SamaGamesAPI.get().getShopsManager("cosmetic");
    }

    public void buy(Player player)
    {
        if (this.buyMethod == BuyMethod.FREE)
            return;

        if (this.buyMethod == BuyMethod.COINS)
        {
            if (!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(this.coinsCost))
            {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cela.");
                return;
            }

            GuiConfirm confirm = new GuiConfirm(Hub.getInstance().getGuiManager().getPlayerGui(player), () ->
            {
                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.coinsCost);
                this.buyCallback(player, false);
            });

            Hub.getInstance().getGuiManager().openGui(player, confirm);
        } else if (this.buyMethod == BuyMethod.STARS)
        {
            if (!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughStars(this.starsCost))
            {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'étoiles pour acheter cela.");
                return;
            }

            GuiConfirm confirm = new GuiConfirm(Hub.getInstance().getGuiManager().getPlayerGui(player), () ->
            {
                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawStars(this.starsCost);
                this.buyCallback(player, false);
            });

            Hub.getInstance().getGuiManager().openGui(player, confirm);
        }
    }

    public void buyCallback(Player player, boolean album)
    {
        shopsManager.addOwnedLevel(player, category, key);
        Hub.getInstance().getGuiManager().getPlayerGui(player).update(player);

        if(!album)
            player.spigot().sendMessage(getBuyResponse());
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

    public void permissionNeededToView(String permissionNeededToView)
    {
        this.permissionNeededToView = permissionNeededToView;
    }

    public String getCategory()
    {
        return this.category;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public ItemStack getIcon(Player player)
    {
        ItemStack icon = this.icon.clone();
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(this.displayName);

        List<String> lores = meta.getLore();

        if (lores == null)
            lores = new ArrayList<>();

        lores.add("");

        if (this.isOwned(player))
            lores.add(ChatColor.GREEN + "Possédé");
        else if (this.buyMethod == BuyMethod.FREE)
            lores.add(ChatColor.GREEN + "Gratuit ! :)");
        else if (this.buyMethod == BuyMethod.COINS)
            lores.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.coinsCost + " pièces");
        else if (this.buyMethod == BuyMethod.STARS)
            lores.add(ChatColor.GRAY + "Prix : " + ChatColor.AQUA + this.starsCost + " étoiles");
        else if (this.buyMethod == BuyMethod.PERMISSION)
            lores.add(ChatColor.RED + "Votre grade ne vous permet pas d'utiliser cela.");

        meta.setLore(lores);
        icon.setItemMeta(meta);

        return icon;
    }

    public int getCoinsCost()
    {
        return this.coinsCost;
    }

    public int getStarsCost()
    {
        return this.starsCost;
    }

    public boolean canView(Player player)
    {
        if (this.permissionNeededToView == null)
            return true;
        else
            return SamaGamesAPI.get().getPermissionsManager().hasPermission(player, this.permissionNeededToView);
    }

    public boolean isOwned(Player player)
    {
        if (this.buyMethod == BuyMethod.FREE)
            return true;
        else if (this.buyMethod == BuyMethod.PERMISSION)
            return SamaGamesAPI.get().getPermissionsManager().hasPermission(player, this.permissionNeeded);
        List<String> owned = shopsManager.getOwnedLevels(player, category);
        return owned != null && owned.contains(key);
    }

    public String getKey()
    {
        return key;
    }

    public BaseComponent getBuyResponse()
    {
        TextComponent txt = new TextComponent("Vous possédez désormais " + this.displayName + " ! Re-cliquez pour l'utiliser.");
        txt.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        return txt;
    }
}
