package net.samagames.hub.games.shop;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.games.IGame;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopItem
{
    private final IGame game;
    private final String databaseName;
    private final ItemStack icon;
    private final Object[][] levels;

    public ShopItem(IGame game, String databaseName, String displayName, ItemStack icon)
    {
        this.game = game;
        this.databaseName = databaseName;
        this.icon = icon;
        this.levels = new Object[][] {};

        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + displayName);
        this.icon.setItemMeta(meta);
    }

    public void addLevel(int cost, String[] description, String value)
    {
        int level = this.levels.length + 1;

        this.levels[level] = new Object[] {
                cost,
                description,
                value
        };
    }

    public boolean buy(Player player, int level)
    {
        if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins((Integer) this.levels[level][0]))
            return false;

        SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins((Integer) this.levels[level][0]);
        SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).addOwnedItem(player, this.databaseName, String.valueOf(this.levels[level][2]));
        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);

        return true;
    }

    public String getDatabaseName()
    {
        return this.databaseName;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public Object[][] getLevels()
    {
        return this.levels;
    }
}
