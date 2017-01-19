package net.samagames.hub.gui.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiCosmeticsCategory<COSMETIC extends AbstractCosmetic> extends AbstractGui
{
    private final String title;
    private final AbstractCosmeticManager<COSMETIC> manager;
    private final boolean canBeRemoved;

    public GuiCosmeticsCategory(Hub hub, String title, AbstractCosmeticManager<COSMETIC> manager, boolean canBeRemoved)
    {
        super(hub);

        this.title = title;
        this.manager = manager;
        this.canBeRemoved = canBeRemoved;
    }

    @Override
    public void display(Player player)
    {
        int lines = 1;
        int slot = 0;

        for (AbstractCosmetic cosmetic : this.manager.getRegistry().getElements().values())
        {
            if (!cosmetic.canView(player))
                continue;

            slot++;

            if (slot == 8)
            {
                slot = 0;
                lines++;
            }
        }

        this.inventory = this.hub.getServer().createInventory(null, 9 + (lines * 9) + (9 * 2), this.title);

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
            if (!cosmetic.canView(player))
                continue;

            this.setSlotData(cosmetic.getIcon(player), (baseSlots[slot] + (lines * 9)), "cosmetic_" + cosmetic.getStorageId());

            slot++;

            if (slot == 7)
            {
                slot = 0;
                lines++;
            }
        }

        if (this.canBeRemoved)
        {
            this.setSlotData(getBackIcon(), this.inventory.getSize() - 4, "back");

            boolean plural = !this.manager.restrictToOne();

            this.setSlotData(ChatColor.RED + "Désactiver " + (plural ? "vos" : "votre") + " cosmétique" + (plural ? "s" : "") + " actuel" + (plural ? "s" : ""), Material.FLINT_AND_STEEL, this.inventory.getSize() - 6, null, "delete");
        }
        else
        {
            this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
        }
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("cosmetic_"))
        {
            int cosmetic = Integer.parseInt(action.split("_")[1]);
            this.manager.enableCosmetic(player, this.manager.getRegistry().getElementByStorageId(cosmetic), clickType);
        }
        else if (action.equals("delete"))
        {
            this.manager.disableCosmetics(player, false);
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().openGui(player, new GuiCosmetics(this.hub));
        }
    }
}
