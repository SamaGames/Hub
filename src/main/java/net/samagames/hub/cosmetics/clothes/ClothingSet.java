package net.samagames.hub.cosmetics.clothes;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.PersistanceUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 18/01/2017
 */
public class ClothingSet
{
    private final int storageId;
    private final ItemStack icon;
    private final ClothCosmetic[] set;

    ClothingSet(Hub hub, int storageId, ClothCosmetic[] set) throws Exception
    {
        this.storageId = storageId;
        this.icon = PersistanceUtils.makeStack(hub, SamaGamesAPI.get().getShopsManager().getItemDescription(storageId));
        this.set = set;
    }

    public ItemStack getIcon(Player player)
    {
        ItemStack personalIcon = this.icon.clone();
        ItemMeta meta = personalIcon.getItemMeta();

        List<String> lore = meta.getLore();

        if (lore == null)
            lore = new ArrayList<>();
        else
            lore.add("");

        lore.add(ChatColor.DARK_GRAY + "Parties débloquées : " + this.getFormattedUnlockedPieces(player));

        meta.setLore(lore);
        personalIcon.setItemMeta(meta);

        return personalIcon;
    }

    public int getStorageId()
    {
        return this.storageId;
    }

    public String getName()
    {
        return ChatColor.stripColor(this.icon.getItemMeta().getDisplayName());
    }

    public ClothCosmetic[] getSet()
    {
        return this.set;
    }

    private String getFormattedUnlockedPieces(Player player)
    {
        int unlocked = 0;

        for (ClothCosmetic clothCosmetic : this.set)
            if (clothCosmetic.isOwned(player))
                unlocked++;

        if (unlocked == 0)
            return ChatColor.RED + "0/4";
        else if (unlocked == 4)
            return ChatColor.GREEN + "4/4";
        else
            return ChatColor.YELLOW + "" + unlocked + "/4";
    }
}
