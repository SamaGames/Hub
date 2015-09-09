package net.samagames.hub.gui.cosmetics;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiCosmeticsCategory<T extends AbstractCosmetic> extends AbstractGui
{
    private final String title;
    private final AbstractCosmeticManager<T> manager;
    private final boolean canBeRemoved;

    public GuiCosmeticsCategory(String title, AbstractCosmeticManager<T> manager, boolean canBeRemoved)
    {
        this.title = title;
        this.manager = manager;
        this.canBeRemoved = canBeRemoved;
    }

    @Override
    public void display(Player player)
    {
        int lines = 1;
        int slot = 0;

        for(AbstractCosmetic cosmetic : this.manager.getRegistry().getElements().values())
        {
            if(!cosmetic.canView(player))
                continue;

            slot++;

            if(slot == 8)
            {
                slot = 0;
                lines++;
            }
        }

        this.inventory = Bukkit.createInventory(null, 9 + (lines * 9) + (9 * 2), this.title);

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int lines = 0;
        int slot = 0;

        for (AbstractCosmetic cosmetic : this.manager.getRegistry().getElements().values())
        {
            if(!cosmetic.canView(player))
                continue;

            this.setSlotData(cosmetic.getIcon(player), (baseSlots[slot] + (lines * 9)), "cosmetic_" + cosmetic.getKey());

            slot++;

            if (slot == 7)
            {
                slot = 0;
                lines++;
            }
        }

        this.setSlotData(ChatColor.AQUA + "Vous avez " + SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getStars() + " étoiles", Material.NETHER_STAR, this.inventory.getSize() - 6, null, "none");

        if (this.canBeRemoved)
        {
            this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");
            this.setSlotData(ChatColor.RED + "Supprimer votre effet actuel", Material.FLINT_AND_STEEL, this.inventory.getSize() - 4, null, "delete");
        }
        else
        {
            this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 4, "back");
        }
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("cosmetic_"))
        {
            String cosmetic = action.split("_")[1];
            this.manager.enableCosmetic(player, this.manager.getRegistry().getElementByStorageName(cosmetic));
        }
        else if(action.equals("delete"))
        {
            this.manager.disableCosmetic(player, false);
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiCosmetics());
        }
    }
}
